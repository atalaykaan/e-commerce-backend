package com.atalaykaan.e_commerce_backend.domain.payment.service;

import com.atalaykaan.e_commerce_backend.domain.payment.dto.response.PaymentDTO;
import com.atalaykaan.e_commerce_backend.domain.payment.enums.PaymentStatus;
import com.atalaykaan.e_commerce_backend.domain.payment.mapper.PaymentMapper;
import com.atalaykaan.e_commerce_backend.domain.payment.model.Payment;
import com.atalaykaan.e_commerce_backend.domain.payment.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;

    private final PaymentMapper paymentMapper;

    public PaymentDTO createPayment(UUID orderId, UUID userId) {

        LocalDateTime dateNow = LocalDateTime.now();

        Payment payment = Payment.builder()
                .orderId(orderId)
                .userId(userId)
                .paymentStatus(PaymentStatus.APPROVED)
                .createdAt(dateNow)
                .updatedAt(dateNow)
                .build();

        Payment createdPayment = paymentRepository.save(payment);

        PaymentDTO paymentDTO = paymentMapper.toDTO(createdPayment);

        return paymentDTO;
    }
}
