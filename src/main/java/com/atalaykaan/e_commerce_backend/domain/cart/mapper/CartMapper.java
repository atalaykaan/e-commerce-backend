package com.atalaykaan.e_commerce_backend.domain.cart.mapper;

import com.atalaykaan.e_commerce_backend.domain.cart.model.dto.response.CartDTO;
import com.atalaykaan.e_commerce_backend.domain.cart.model.dto.response.CartItemDTO;
import com.atalaykaan.e_commerce_backend.domain.cart.model.entity.Cart;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
@RequiredArgsConstructor
public class CartMapper {

    private final CartItemMapper cartItemMapper;

    public CartDTO toDto(Cart cart) {

        List<CartItemDTO> cartItemDTOList = cart.getCartItems().stream().map(cartItemMapper::toDto).toList();

        return CartDTO.builder()
                .id(cart.getId())
                .userId(cart.getUserId())
                .cartItems(cartItemDTOList)
                .totalPrice(cartItemDTOList
                        .stream()
                        .map(cartItemDTO -> cartItemDTO.getPrice().multiply(BigDecimal.valueOf(cartItemDTO.getQuantity())))
                        .reduce(BigDecimal.ZERO, BigDecimal::add))
                .createdAt(cart.getCreatedAt())
                .updatedAt(cart.getUpdatedAt())
                .build();
    }
}