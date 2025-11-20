package com.atalaykaan.e_commerce_backend.domain.order.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "order_items")
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private UUID productId;

    private Long quantity;

    @ManyToOne
    @JoinColumn(name = "order_id")
    @ToString.Exclude
    @JsonBackReference
    private Order order;
}
