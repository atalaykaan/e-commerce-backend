package com.atalaykaan.e_commerce_backend.model.dto.request.update;

import com.atalaykaan.e_commerce_backend.model.enums.OrderStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateOrderRequest {

    @NotNull
    private OrderStatus orderStatus;
}
