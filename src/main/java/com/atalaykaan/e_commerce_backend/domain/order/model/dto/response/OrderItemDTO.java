package com.atalaykaan.e_commerce_backend.domain.order.model.dto.response;

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

    private Long productId;

    private Integer quantity;
}
