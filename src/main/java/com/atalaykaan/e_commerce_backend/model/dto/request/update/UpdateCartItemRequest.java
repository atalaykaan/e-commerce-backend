package com.atalaykaan.e_commerce_backend.model.dto.request.update;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateCartItemRequest {

    @NotNull
    private UUID id;

    @NotNull
    @Min(value = 0, message = "Cart item quantity cannot be less than zero")
    private Integer quantity;
}
