package com.atalaykaan.e_commerce_backend.domain.order.mapper;

import com.atalaykaan.e_commerce_backend.domain.order.dto.OrderItemDTO;
import com.atalaykaan.e_commerce_backend.domain.order.model.OrderItem;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OrderItemMapper {

    OrderItemDTO toDto(OrderItem orderItem);
}
