package com.atalaykaan.e_commerce_backend.domain.product.dto;

import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateProductRequest {

    private String name;

    private String description;

    private String brand;

    @Min(value = 0, message = "Product price cannot be less than zero")
    private BigDecimal price;

    @Min(value = 0, message = "Product stock cannot be less than zero")
    private Long stock;
}
