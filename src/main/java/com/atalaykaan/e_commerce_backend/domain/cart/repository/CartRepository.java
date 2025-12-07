package com.atalaykaan.e_commerce_backend.domain.cart.repository;

import com.atalaykaan.e_commerce_backend.domain.cart.model.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.UUID;

public interface CartRepository extends JpaRepository<Cart, UUID> {

    Optional<Cart> findByUserId(UUID id);
}
