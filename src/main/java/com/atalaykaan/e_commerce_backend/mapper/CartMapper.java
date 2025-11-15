package com.atalaykaan.e_commerce_backend.mapper;

import com.atalaykaan.e_commerce_backend.model.dto.response.CartDTO;
import com.atalaykaan.e_commerce_backend.model.entity.Cart;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CartMapper {

    CartDTO toDto(Cart cart);
}
