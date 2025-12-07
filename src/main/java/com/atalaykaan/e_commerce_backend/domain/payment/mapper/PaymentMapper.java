package com.atalaykaan.e_commerce_backend.domain.payment.mapper;

import com.atalaykaan.e_commerce_backend.domain.payment.model.dto.response.PaymentDTO;
import com.atalaykaan.e_commerce_backend.domain.payment.model.entity.Payment;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PaymentMapper {

    PaymentDTO toDTO(Payment payment);
}
