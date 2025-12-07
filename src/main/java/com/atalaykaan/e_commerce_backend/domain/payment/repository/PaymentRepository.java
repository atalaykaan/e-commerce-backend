package com.atalaykaan.e_commerce_backend.domain.payment.repository;

import com.atalaykaan.e_commerce_backend.domain.payment.model.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface PaymentRepository extends JpaRepository<Payment, UUID> {

    @Query(value = "SELECT p FROM Payment p WHERE p.userId=:userId")
    List<Payment> findAllByUserId(UUID userId);
}
