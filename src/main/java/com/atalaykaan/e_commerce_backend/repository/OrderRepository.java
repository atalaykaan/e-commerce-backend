package com.atalaykaan.e_commerce_backend.repository;

import com.atalaykaan.e_commerce_backend.model.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, UUID> {
}
