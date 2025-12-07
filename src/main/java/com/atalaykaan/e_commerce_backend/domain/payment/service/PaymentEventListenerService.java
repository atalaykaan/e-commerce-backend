package com.atalaykaan.e_commerce_backend.domain.payment.service;

import com.atalaykaan.e_commerce_backend.domain.order.model.entity.Order;
import com.atalaykaan.e_commerce_backend.domain.payment.model.dto.response.PaymentDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentEventListenerService {

    private final PaymentService paymentService;

    @KafkaListener(topics = "${spring.kafka.topic.order-created}", groupId = "payment-group")
    public void handleReceivedOrderMessage(Order order) {

        PaymentDTO paymentDTO = paymentService.createPayment(order.getId(), order.getUserId());

        paymentService.validatePayment(paymentDTO);
    }
}
