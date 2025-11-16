package com.atalaykaan.e_commerce_backend.service;

import com.atalaykaan.e_commerce_backend.exception.*;
import com.atalaykaan.e_commerce_backend.model.dto.request.create.AddItemToCartRequest;
import com.atalaykaan.e_commerce_backend.model.dto.request.update.UpdateCartItemRequest;
import com.atalaykaan.e_commerce_backend.model.dto.response.CartDTO;
import com.atalaykaan.e_commerce_backend.mapper.CartMapper;
import com.atalaykaan.e_commerce_backend.model.dto.response.ProductDTO;
import com.atalaykaan.e_commerce_backend.model.dto.response.UserDTO;
import com.atalaykaan.e_commerce_backend.model.entity.Cart;
import com.atalaykaan.e_commerce_backend.model.entity.CartItem;
import com.atalaykaan.e_commerce_backend.repository.CartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;

    private final CartMapper cartMapper;

    private final CartItemService cartItemService;

    private final UserService userService;

    private final ProductService productService;

    @Transactional
    private Cart createCart(UUID userId) {

        LocalDateTime dateNow = LocalDateTime.now();

        Cart cart = Cart.builder()
                .userId(userId)
                .cartItems(new ArrayList<>())
                .totalPrice(BigDecimal.ZERO)
                .createdAt(dateNow)
                .updatedAt(dateNow)
                .build();

        return cartRepository.save(cart);
    }

    public CartDTO findCartById(UUID id) {

        CartDTO cartDTO = cartRepository.findById(id)
                .map(cartMapper::toDto)
                .orElseThrow(() -> new CartNotFoundException("Cart not found with id: " + id));

        return cartDTO;
    }

    public CartDTO findCartByUserEmail(String email) {

        UserDTO userDTO = userService.findByEmail(email);

        CartDTO cartDTO = findCartByUserId(userDTO.getId());

        return cartDTO;
    }

    public CartDTO findCartByUserId(UUID userId) {

        CartDTO cartDTO = cartRepository.findByUserId(userId)
                .map(cartMapper::toDto)
                .orElseThrow(() -> new CartNotFoundException("User with id " + userId + " does not have any active carts"));

        return cartDTO;
    }

    public List<CartDTO> findAllCarts() {

        List<CartDTO> cartDTOList = cartRepository.findAll()
                .stream()
                .map(cartMapper::toDto)
                .toList();

        if(cartDTOList.isEmpty()){

            throw new CartNotFoundException("No carts were found");
        }

        return cartDTOList;
    }

    private void appendItemToCart(CartItem cartItem, Cart cart) {

        ProductDTO productDTO = productService.findById(cartItem.getProductId());

        if(productDTO.getStock() < cartItem.getQuantity()) {

            throw new InvalidItemQuantityException("Item quantity cannot exceed the product stock");
        }

        List<CartItem> cartItemList = cart.getCartItems();

        boolean hasDuplicateItem = cartItemList
                .stream()
                .anyMatch(listItem -> listItem.getProductId().equals(cartItem.getProductId()));

        if(hasDuplicateItem) {

            throw new ItemAlreadyInCartException("This item is already in the cart");
        }

        cartItem.setCart(cart);
        cartItemList.add(cartItem);
    }

    @Transactional
    public CartDTO addItemToCart(String email, AddItemToCartRequest addItemToCartRequest) {

        UserDTO userDTO = userService.findByEmail(email);

        Cart cart = cartRepository.findByUserId(userDTO.getId())
                .orElse(createCart(userDTO.getId()));

        CartItem cartItem = cartItemService.createCartItem(addItemToCartRequest);

        appendItemToCart(cartItem, cart);

        LocalDateTime dateNow = LocalDateTime.now();

        cartItem.setUpdatedAt(dateNow);
        cart.setUpdatedAt(dateNow);

        Cart savedCart = cartRepository.save(cart);

        CartDTO cartDTO = cartMapper.toDto(savedCart);

        return cartDTO;
    }

    private CartItem validateUser(String email, UUID cartItemId) {

        UserDTO userDTO = userService.findByEmail(email);

        CartItem cartItem = cartItemService.getById(cartItemId);

        Cart cart = cartItem.getCart();

        if(userDTO.getId() != cart.getUserId()) {

            throw new FailedUserValidationException("Failed to validate user");
        }

        return cartItem;
    }

    @Transactional
    public CartDTO updateCartItemQuantity(String email, UpdateCartItemRequest updateCartItemRequest) {

        CartItem cartItem = validateUser(email, updateCartItemRequest.getId());

        Cart cart = cartItem.getCart();

        cartItemService.updateCartItemQuantity(cartItem, updateCartItemRequest.getQuantity());

        LocalDateTime dateNow = LocalDateTime.now();

        if(cartItem.getQuantity() == 0) {

            cart.getCartItems().remove(cartItem);
        }
        else {
            cartItem.setUpdatedAt(dateNow);
        }

        cart.setUpdatedAt(dateNow);

        Cart savedCart = cartRepository.save(cart);

        CartDTO cartDTO = cartMapper.toDto(savedCart);

        return cartDTO;
    }

    private void checkIfCartIsEmpty(Cart cart) {

        if (cart.getCartItems().isEmpty()) {

            throw new CartItemNotFoundException("Cart is already empty");
        }
    }

    @Transactional
    public void deleteAllItemsFromCartByEmail(String email) {

        UserDTO userDTO = userService.findByEmail(email);

        Cart cart = cartRepository.findByUserId(userDTO.getId())
                .orElseThrow(() -> new CartNotFoundException("Cart not found for user with email: " + email));

        checkIfCartIsEmpty(cart);

        cart.getCartItems().forEach(cartItem -> cartItemService.deleteCartItem(cartItem.getId()));

        cart.getCartItems().clear();

        cart.setUpdatedAt(LocalDateTime.now());

        cartRepository.save(cart);
    }

    @Transactional
    public void deleteAllItemsFromCartById(UUID id) {

        Cart cart = cartRepository.findById(id)
                .orElseThrow(() -> new CartNotFoundException("Cart not found with id: " + id));

        checkIfCartIsEmpty(cart);

        cart.getCartItems().forEach(cartItem -> cartItemService.deleteCartItem(cartItem.getId()));

        cart.getCartItems().clear();

        cart.setUpdatedAt(LocalDateTime.now());

        cartRepository.save(cart);
    }

    public void deleteAllCarts() {

        userService.findAll()
                .forEach(user -> deleteAllItemsFromCartById(user.getId()));
    }
}
