package com.atalaykaan.e_commerce_backend.model.dto.request.create;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddItemToCartRequest {

    @NotBlank
    private UUID productId;

    @NotNull
    @Min(value = 1, message = "Quantity must be at least one")
    private Integer quantity;
}
