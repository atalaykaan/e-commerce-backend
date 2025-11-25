package com.atalaykaan.e_commerce_backend.domain.product.model;

import jakarta.persistence.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Document(indexName = "product_index")
public class ProductDocument {

    @Id
    private Long id;

    private String name;

    private BigDecimal price;

    private String description;

    private String brand;

    private Long stock;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
