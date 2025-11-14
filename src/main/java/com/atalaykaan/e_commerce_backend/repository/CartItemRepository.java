package com.atalaykaan.e_commerce_backend.repository;

import com.atalaykaan.e_commerce_backend.model.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CartItemRepository extends JpaRepository<CartItem, UUID> {
}
