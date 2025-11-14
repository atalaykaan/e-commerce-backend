package com.atalaykaan.e_commerce_backend.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartItemDTO {

    private UUID productId;

    private Integer quantity;

    private CartDTO cart;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
