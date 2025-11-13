package com.atalaykaan.e_commerce_backend.mapper;

import com.atalaykaan.e_commerce_backend.dto.response.ProductDTO;
import com.atalaykaan.e_commerce_backend.model.Product;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    ProductDTO toDTO(Product product);

    Product toProduct(ProductDTO productDTO);
}
