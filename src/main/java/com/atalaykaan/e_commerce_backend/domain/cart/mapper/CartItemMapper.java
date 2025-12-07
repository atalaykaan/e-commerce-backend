package com.atalaykaan.e_commerce_backend.domain.cart.mapper;

import com.atalaykaan.e_commerce_backend.domain.cart.model.dto.response.CartItemDTO;
import com.atalaykaan.e_commerce_backend.domain.product.model.dto.response.ProductDTO;
import com.atalaykaan.e_commerce_backend.domain.cart.model.entity.CartItem;
import com.atalaykaan.e_commerce_backend.domain.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CartItemMapper {

    private final ProductService productService;

    public CartItemDTO toDto(CartItem cartItem) {

        ProductDTO productDTO = productService.findProductById(cartItem.getProductId());

        return CartItemDTO.builder()
                .id(cartItem.getId())
                .productId(cartItem.getProductId())
                .price(productDTO.getPrice())
                .quantity(cartItem.getQuantity())
                .createdAt(cartItem.getCreatedAt())
                .updatedAt(cartItem.getUpdatedAt())
                .build();
    }
}