package com.atalaykaan.e_commerce_backend.domain.payment.repository;

import com.atalaykaan.e_commerce_backend.domain.payment.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PaymentRepository extends JpaRepository<Payment, UUID> {
}
