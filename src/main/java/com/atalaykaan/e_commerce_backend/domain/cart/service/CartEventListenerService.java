package com.atalaykaan.e_commerce_backend.domain.cart.service;

import com.atalaykaan.e_commerce_backend.domain.order.model.entity.Order;
import com.atalaykaan.e_commerce_backend.domain.user.model.dto.response.UserDTO;
import com.atalaykaan.e_commerce_backend.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartEventListenerService {

    private final CartService cartService;

    private final UserService userService;

    @KafkaListener(topics = "${spring.kafka.topic.order-created}", groupId = "cart-group")
    public void handleOrderCreatedMessage(Order order) {

        UserDTO userDTO = userService.findUserById(order.getUserId());

        cartService.deleteCartByEmail(userDTO.getEmail());
    }
}
