package com.atalaykaan.e_commerce_backend.domain.order.repository;

import com.atalaykaan.e_commerce_backend.domain.order.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface OrderItemRepository extends JpaRepository<OrderItem, UUID> {
}
