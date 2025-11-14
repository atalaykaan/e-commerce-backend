package com.atalaykaan.e_commerce_backend.model.dto.request.create;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateProductRequest {

    @NotBlank
    private String name;

    @NotBlank
    private String description;

    @NotBlank
    private String brand;

    @NotBlank
    @Min(value = 0, message = "Product price cannot be less than zero")
    private BigDecimal price;

    @NotBlank
    @Min(value = 0, message = "Product stock cannot be less than zero")
    private Long stock;
}
