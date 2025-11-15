package com.atalaykaan.e_commerce_backend.mapper;

import com.atalaykaan.e_commerce_backend.model.dto.response.CartItemDTO;
import com.atalaykaan.e_commerce_backend.model.entity.CartItem;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CartItemMapper {

    CartItemDTO toDto(CartItem cartItem);
}
