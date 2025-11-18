package com.atalaykaan.e_commerce_backend.model.dto.response;

import com.atalaykaan.e_commerce_backend.model.entity.Order;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItemDTO {

    private UUID id;

    private UUID productId;

    private Integer quantity;
}
