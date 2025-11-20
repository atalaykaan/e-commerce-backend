package com.atalaykaan.e_commerce_backend.domain.order.dto.response;

import com.atalaykaan.e_commerce_backend.domain.order.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderDTO {

    private UUID id;

    private UUID userId;

    private List<OrderItemDTO> orderItems;

    private BigDecimal totalPrice;

    private OrderStatus orderStatus;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
