package com.atalaykaan.e_commerce_backend.domain.user.repository;

import com.atalaykaan.e_commerce_backend.domain.user.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {

    Optional<User> findByEmail(String email);
}
