package com.atalaykaan.e_commerce_backend.domain.cart.dto.response;

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
public class CartItemDTO {

    private UUID id;

    private Long productId;

    private BigDecimal price;

    private Long quantity;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
