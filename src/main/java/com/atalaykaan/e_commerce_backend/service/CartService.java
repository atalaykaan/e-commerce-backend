package com.atalaykaan.e_commerce_backend.service;

import com.atalaykaan.e_commerce_backend.exception.ItemAlreadyInCartException;
import com.atalaykaan.e_commerce_backend.model.dto.request.create.AddItemToCartRequest;
import com.atalaykaan.e_commerce_backend.model.dto.request.update.UpdateCartItemRequest;
import com.atalaykaan.e_commerce_backend.model.dto.response.CartDTO;
import com.atalaykaan.e_commerce_backend.exception.CartNotFoundException;
import com.atalaykaan.e_commerce_backend.mapper.CartMapper;
import com.atalaykaan.e_commerce_backend.model.entity.Cart;
import com.atalaykaan.e_commerce_backend.model.entity.CartItem;
import com.atalaykaan.e_commerce_backend.repository.CartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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

    public CartDTO findCartByUserId(UUID userId) {

        CartDTO cartDTO = cartRepository.findByUserId(userId)
                .map(cartMapper::toDto)
                .orElseThrow(() -> new CartNotFoundException("User with id " + userId + " does not have any active carts"));

        return cartDTO;
    }

    public CartDTO findCartById(UUID id) {

        CartDTO cartDTO = cartRepository.findById(id)
                .map(cartMapper::toDto)
                .orElseThrow(() -> new CartNotFoundException("Cart does not exist with id: " + id));

        return cartDTO;
    }

    private void appendItemToCart(CartItem cartItem, Cart cart) {

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

    public CartDTO addItemToCart(AddItemToCartRequest addItemToCartRequest) {

        Cart cart = cartRepository.findByUserId(addItemToCartRequest.getUserId())
                .orElse(createCart(addItemToCartRequest.getUserId()));

        CartItem cartItem = cartItemService.createCartItem(addItemToCartRequest);

        appendItemToCart(cartItem, cart);

        LocalDateTime dateNow = LocalDateTime.now();

        cartItem.setUpdatedAt(dateNow);
        cart.setUpdatedAt(dateNow);

        Cart savedCart = cartRepository.save(cart);

        return cartMapper.toDto(savedCart);
    }

    public CartDTO updateCartItemQuantity(UpdateCartItemRequest updateCartItemRequest) {

        CartItem cartItem = cartItemService.updateCartItemQuantity(updateCartItemRequest.getId(), updateCartItemRequest.getQuantity());

        Cart cart = cartItem.getCart();

        LocalDateTime dateNow = LocalDateTime.now();

        if(cartItem.getQuantity() == 0) {

            cart.getCartItems().remove(cartItem);
        }
        else {
            cartItem.setUpdatedAt(dateNow);
        }

        cart.setUpdatedAt(dateNow);

        Cart savedCart = cartRepository.save(cart);

        return cartMapper.toDto(savedCart);
    }
}
