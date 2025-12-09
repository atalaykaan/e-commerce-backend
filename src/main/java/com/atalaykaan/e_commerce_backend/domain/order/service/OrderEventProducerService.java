package com.atalaykaan.e_commerce_backend.domain.order.service;

import com.atalaykaan.e_commerce_backend.domain.order.model.entity.Order;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderEventProducerService {

    @Value("${spring.kafka.topic.order-created}")
    private String orderCreatedTopic;

    @Value("${spring.kafka.topic.order-updated}")
    private String orderUpdatedTopic;

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void sendOrderCreatedMessage(Order order) {

        kafkaTemplate.send(orderCreatedTopic, order);

        log.info("Order received: {}", order);
    }

    public void sendOrderUpdatedMessage(Order order) {

        kafkaTemplate.send(orderUpdatedTopic, order);

        log.info("Order updated: {}", order);
    }
}
