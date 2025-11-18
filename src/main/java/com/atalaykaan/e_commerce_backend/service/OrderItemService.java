package com.atalaykaan.e_commerce_backend.service;

import com.atalaykaan.e_commerce_backend.model.dto.response.CartItemDTO;
import com.atalaykaan.e_commerce_backend.model.entity.OrderItem;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderItemService {

    protected OrderItem createOrderItem(CartItemDTO cartItemDTO) {

        OrderItem orderItem = OrderItem.builder()
                .productId(cartItemDTO.getProductId())
                .quantity(cartItemDTO.getQuantity())
                .build();

        return orderItem;
    }
}
