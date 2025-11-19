package com.atalaykaan.e_commerce_backend.service;

import com.atalaykaan.e_commerce_backend.model.entity.Order;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaProducerService {

    @Value("${spring.kafka.topic}")
    private String topic;

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void sendOrderCreatedMessage(Order order) {

        kafkaTemplate.send(topic, order);

        log.info("Order received: {}", order);
    }

    public void sendOrderUpdatedMessage(Order order) {

        kafkaTemplate.send(topic, order);

        log.info("Order updated: {}", order);
    }
}
