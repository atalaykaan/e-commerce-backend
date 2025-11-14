package com.atalaykaan.e_commerce_backend.service;

import com.atalaykaan.e_commerce_backend.exception.CartItemNotFoundException;
import com.atalaykaan.e_commerce_backend.exception.InvalidItemQuantityException;
import com.atalaykaan.e_commerce_backend.mapper.CartItemMapper;
import com.atalaykaan.e_commerce_backend.model.dto.request.create.AddItemToCartRequest;
import com.atalaykaan.e_commerce_backend.model.dto.response.CartItemDTO;
import com.atalaykaan.e_commerce_backend.model.entity.CartItem;
import com.atalaykaan.e_commerce_backend.repository.CartItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CartItemService {

    private final CartItemRepository cartItemRepository;

    private final CartItemMapper cartItemMapper;

    protected CartItem createCartItem(AddItemToCartRequest addItemToCartRequest) {

        LocalDateTime dateNow = LocalDateTime.now();

        return CartItem.builder()
                .productId(addItemToCartRequest.getProductId())
                .quantity(addItemToCartRequest.getQuantity())
                .createdAt(dateNow)
                .updatedAt(dateNow)
                .build();
    }

    public CartItemDTO findById(UUID id) {

        CartItemDTO cartItemDTO = cartItemRepository.findById(id)
                .map(cartItemMapper::toDto)
                .orElseThrow(() -> new CartItemNotFoundException("Cart item not found with id: " + id));

        return cartItemDTO;
    }

    protected CartItem updateCartItemQuantity(UUID id, Integer quantity) {

        CartItem cartItem = cartItemRepository.findById(id)
                .orElseThrow(() -> new CartItemNotFoundException("Cart item not found with id: " + id));

        if(quantity < 0) {

            throw new InvalidItemQuantityException("Decrease amount cannot exceed the cart item amount");
        }

        cartItem.setQuantity(quantity);

        return cartItem;
    }
}
