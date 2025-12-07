package com.atalaykaan.e_commerce_backend.domain.payment.controller;

import com.atalaykaan.e_commerce_backend.domain.payment.model.dto.response.PaymentDTO;
import com.atalaykaan.e_commerce_backend.domain.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

import static com.atalaykaan.e_commerce_backend.common.constants.ApiConstants.*;

@RestController
@RequestMapping(API_PREFIX + API_VERSION + API_PAYMENTS)
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @GetMapping
    public ResponseEntity<List<PaymentDTO>> findCurrentUserPayments(Authentication authentication) {

        String email = authentication.getName();

        List<PaymentDTO> paymentDTOList = paymentService.findPaymentByEmail(email);

        return ResponseEntity.ok(paymentDTOList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaymentDTO> findPaymentById(@PathVariable UUID id) {

        PaymentDTO paymentDTO = paymentService.findPaymentById(id);

        return ResponseEntity.ok(paymentDTO);
    }
}
