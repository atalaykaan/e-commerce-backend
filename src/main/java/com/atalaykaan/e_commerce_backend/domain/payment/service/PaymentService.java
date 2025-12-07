package com.atalaykaan.e_commerce_backend.domain.payment.service;

import com.atalaykaan.e_commerce_backend.common.exception.PaymentFailedException;
import com.atalaykaan.e_commerce_backend.common.exception.PaymentNotFoundException;
import com.atalaykaan.e_commerce_backend.domain.payment.model.dto.response.PaymentDTO;
import com.atalaykaan.e_commerce_backend.domain.payment.enums.PaymentStatus;
import com.atalaykaan.e_commerce_backend.domain.payment.mapper.PaymentMapper;
import com.atalaykaan.e_commerce_backend.domain.payment.model.entity.Payment;
import com.atalaykaan.e_commerce_backend.domain.payment.repository.PaymentRepository;
import com.atalaykaan.e_commerce_backend.domain.user.model.dto.response.UserDTO;
import com.atalaykaan.e_commerce_backend.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;

    private final PaymentMapper paymentMapper;

    private final UserService userService;

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

    public List<PaymentDTO> findPaymentByEmail(String email) {

        UserDTO userDTO = userService.findUserByEmail(email);

        List<PaymentDTO> paymentDTOList = paymentRepository.findAllByUserId(userDTO.getId())
                .stream()
                .map(paymentMapper::toDTO)
                .toList();

        if(paymentDTOList.isEmpty()) {

            throw new PaymentNotFoundException("No payments were found for user with email: " + email);
        }

        return paymentDTOList;
    }

    public PaymentDTO findPaymentById(UUID id) {

        PaymentDTO paymentDTO = paymentRepository.findById(id)
                .map(paymentMapper::toDTO)
                .orElseThrow(() -> new PaymentNotFoundException("Payment not found with id: " + id));

        return paymentDTO;
    }

    void validatePayment(PaymentDTO paymentDTO) {

        if(!paymentDTO.getPaymentStatus().equals(PaymentStatus.APPROVED)) {

            throw new PaymentFailedException("Payment failed");
        }
    }
}
