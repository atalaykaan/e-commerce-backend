package com.atalaykaan.e_commerce_backend.domain.order.mapper;

import com.atalaykaan.e_commerce_backend.domain.order.model.dto.response.OrderDTO;
import com.atalaykaan.e_commerce_backend.domain.order.model.entity.Order;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    OrderDTO toDto(Order order);
}
