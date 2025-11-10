package com.atalaykaan.e_commerce_backend.repository;

import com.atalaykaan.e_commerce_backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);
}
