package com.atalaykaan.e_commerce_backend.domain.product.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductDTO {

    private Long id;

    private String name;

    private BigDecimal price;

    private String description;

    private String brand;

    private Long stock;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
