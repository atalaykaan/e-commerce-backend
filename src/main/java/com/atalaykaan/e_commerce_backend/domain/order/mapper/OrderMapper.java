package com.atalaykaan.e_commerce_backend.domain.order.mapper;

import com.atalaykaan.e_commerce_backend.domain.order.dto.OrderDTO;
import com.atalaykaan.e_commerce_backend.domain.order.model.Order;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    OrderDTO toDto(Order order);
}
