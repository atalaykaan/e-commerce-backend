package com.atalaykaan.e_commerce_backend.domain.product.service;

import com.atalaykaan.e_commerce_backend.domain.order.model.entity.Order;
import com.atalaykaan.e_commerce_backend.domain.order.model.entity.OrderItem;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductEventListenerService {

    private final ProductService productService;

    @KafkaListener(topics = "${spring.kafka.topic.order-created}", groupId = "product-group")
    public void handleOrderReceivedMessage(Order order) {

        List<OrderItem> orderItems = order.getOrderItems();

        orderItems.forEach(orderItem -> productService.decreaseProductStock(orderItem.getProductId(), orderItem.getQuantity()));
    }
}
