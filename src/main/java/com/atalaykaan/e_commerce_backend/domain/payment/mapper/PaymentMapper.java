package com.atalaykaan.e_commerce_backend.domain.payment.mapper;

import com.atalaykaan.e_commerce_backend.domain.payment.dto.response.PaymentDTO;
import com.atalaykaan.e_commerce_backend.domain.payment.model.Payment;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PaymentMapper {

    PaymentDTO toDTO(Payment payment);
}
