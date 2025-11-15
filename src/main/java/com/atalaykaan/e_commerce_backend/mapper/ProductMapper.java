package com.atalaykaan.e_commerce_backend.mapper;

import com.atalaykaan.e_commerce_backend.model.dto.response.ProductDTO;
import com.atalaykaan.e_commerce_backend.model.entity.Product;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    ProductDTO toDTO(Product product);
}
