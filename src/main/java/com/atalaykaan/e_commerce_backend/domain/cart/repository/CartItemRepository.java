package com.atalaykaan.e_commerce_backend.domain.cart.repository;

import com.atalaykaan.e_commerce_backend.domain.cart.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CartItemRepository extends JpaRepository<CartItem, UUID> {
}
