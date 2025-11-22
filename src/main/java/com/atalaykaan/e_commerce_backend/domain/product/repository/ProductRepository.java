package com.atalaykaan.e_commerce_backend.domain.product.repository;

import com.atalaykaan.e_commerce_backend.domain.product.model.Product;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProductRepository extends MongoRepository<Product, Long> {

}
