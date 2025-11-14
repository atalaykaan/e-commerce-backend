package com.atalaykaan.e_commerce_backend.repository;

import com.atalaykaan.e_commerce_backend.model.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface CartRepository extends JpaRepository<Cart, UUID> {

    @Query(value = "SELECT c FROM Cart c WHERE c.userId =: userId")
    Optional<Cart> findByUserId(UUID userId);
}
