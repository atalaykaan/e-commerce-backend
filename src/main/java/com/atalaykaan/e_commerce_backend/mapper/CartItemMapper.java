package com.atalaykaan.e_commerce_backend.mapper;

import com.atalaykaan.e_commerce_backend.model.dto.response.CartItemDTO;
import com.atalaykaan.e_commerce_backend.model.dto.response.ProductDTO;
import com.atalaykaan.e_commerce_backend.model.entity.CartItem;
import com.atalaykaan.e_commerce_backend.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

//@Mapper(componentModel = "spring")
//public interface CartItemMapper {
//
//    CartItemDTO toDto(CartItem cartItem);
//}

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