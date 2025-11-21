package com.atalaykaan.e_commerce_backend.domain.payment.dto.response;

import com.atalaykaan.e_commerce_backend.domain.payment.enums.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentDTO {

    private UUID id;

    private PaymentStatus paymentStatus;
}
