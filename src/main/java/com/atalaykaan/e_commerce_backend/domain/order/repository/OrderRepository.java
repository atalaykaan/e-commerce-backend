package com.atalaykaan.e_commerce_backend.domain.order.repository;

import com.atalaykaan.e_commerce_backend.domain.order.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, UUID> {

    @Query(value = "SELECT o FROM Order o WHERE o.userId=:userId")
    List<Order> findAllByUserId(UUID userId);
}
