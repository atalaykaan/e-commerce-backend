package com.atalaykaan.e_commerce_backend.domain.payment.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreatePaymentRequest {

    private UUID orderId;

    private UUID userId;
}
