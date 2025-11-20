package com.atalaykaan.e_commerce_backend.domain.cart.service;

import com.atalaykaan.e_commerce_backend.common.exception.CartNotFoundException;
import com.atalaykaan.e_commerce_backend.common.exception.FailedUserValidationException;
import com.atalaykaan.e_commerce_backend.common.exception.InvalidItemQuantityException;
import com.atalaykaan.e_commerce_backend.common.exception.ItemAlreadyInCartException;
import com.atalaykaan.e_commerce_backend.domain.cart.dto.request.AddItemToCartRequest;
import com.atalaykaan.e_commerce_backend.domain.cart.dto.request.UpdateCartItemRequest;
import com.atalaykaan.e_commerce_backend.domain.cart.dto.response.CartDTO;
import com.atalaykaan.e_commerce_backend.domain.cart.mapper.CartMapper;
import com.atalaykaan.e_commerce_backend.domain.product.dto.response.ProductDTO;
import com.atalaykaan.e_commerce_backend.domain.user.dto.response.UserDTO;
import com.atalaykaan.e_commerce_backend.domain.cart.model.Cart;
import com.atalaykaan.e_commerce_backend.domain.cart.model.CartItem;
import com.atalaykaan.e_commerce_backend.domain.cart.repository.CartRepository;
import com.atalaykaan.e_commerce_backend.domain.product.service.ProductService;
import com.atalaykaan.e_commerce_backend.domain.user.service.UserService;
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

    private Cart createCart(UUID userId) {

        LocalDateTime dateNow = LocalDateTime.now();

        Cart cart = Cart.builder()
                .userId(userId)
                .cartItems(new ArrayList<>())
                .totalPrice(BigDecimal.ZERO)
                .createdAt(dateNow)
                .updatedAt(dateNow)
                .build();

        return cart;
    }

    public CartDTO findCartById(UUID id) {

        CartDTO cartDTO = cartRepository.findById(id)
                .map(cartMapper::toDto)
                .orElseThrow(() -> new CartNotFoundException("Cart not found with id: " + id));

        return cartDTO;
    }

    public CartDTO findCartByUserEmail(String email) {

        UserDTO userDTO = userService.findUserByEmail(email);

        CartDTO cartDTO = cartRepository.findByUserId(userDTO.getId())
                .map(cartMapper::toDto)
                .orElseThrow(() -> new CartNotFoundException("User with email " + email + " does not have any active carts"));

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

        ProductDTO productDTO = productService.findProductById(cartItem.getProductId());

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

        UserDTO userDTO = userService.findUserByEmail(email);

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

        UserDTO userDTO = userService.findUserByEmail(email);

        CartItem cartItem = cartItemService.getCartItemById(cartItemId);

        Cart cart = cartItem.getCart();

        boolean userIdAndCartUserIdEquals = userDTO.getId().equals(cart.getUserId());

        if(!userIdAndCartUserIdEquals) {

            throw new FailedUserValidationException("Failed to validate user");
        }

        return cartItem;
    }

    @Transactional
    public CartDTO updateCartItemQuantityInCart(String email, UpdateCartItemRequest updateCartItemRequest) {

        CartItem cartItem = validateUser(email, updateCartItemRequest.getCartItemId());

        Cart cart = cartItem.getCart();

        cartItemService.updateCartItemQuantity(cartItem, updateCartItemRequest.getQuantity());

        if(cartItem.getQuantity() == 0) {

            cart.getCartItems().remove(cartItem);
        }

        if(cart.getCartItems().isEmpty()) {

            cartRepository.deleteById(cart.getId());

            return null;
        }
        else {

            LocalDateTime dateNow = LocalDateTime.now();

            cartItem.setUpdatedAt(dateNow);

            cart.setUpdatedAt(dateNow);

            Cart savedCart = cartRepository.save(cart);

            CartDTO cartDTO = cartMapper.toDto(savedCart);

            return cartDTO;
        }
    }

    @Transactional
    public CartDTO removeItemFromCart(String email, UUID cartItemId) {

        CartItem cartItem = validateUser(email, cartItemId);

        Cart cart = cartItem.getCart();

        cart.getCartItems().remove(cartItem);

        if(cart.getCartItems().isEmpty()) {

            cartRepository.deleteById(cart.getId());

            return null;
        }
        else {

            cart.setUpdatedAt(LocalDateTime.now());

            Cart savedCart = cartRepository.save(cart);

            CartDTO cartDTO = cartMapper.toDto(savedCart);

            return cartDTO;
        }
    }

    @Transactional
    public void deleteCartByEmail(String email) {

        UserDTO userDTO = userService.findUserByEmail(email);

        Cart cart = cartRepository.findByUserId(userDTO.getId())
                .orElseThrow(() -> new CartNotFoundException("Cart not found for user with email: " + email));

        cartRepository.deleteById(cart.getId());
    }

    @Transactional
    public void deleteCartById(UUID id) {

        cartRepository.findById(id)
                .orElseThrow(() -> new CartNotFoundException("Cart not found with id: " + id));

        cartRepository.deleteById(id);
    }

    @Transactional
    public void deleteAllCarts() {

        List<Cart> cartList = cartRepository.findAll();

        if(cartList.isEmpty()) {

            throw new CartNotFoundException("No carts were found");
        }

        cartRepository.deleteAll();
    }
}
