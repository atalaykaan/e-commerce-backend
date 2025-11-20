package com.atalaykaan.e_commerce_backend.domain.product.repository;

import com.atalaykaan.e_commerce_backend.domain.product.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID> {

}
