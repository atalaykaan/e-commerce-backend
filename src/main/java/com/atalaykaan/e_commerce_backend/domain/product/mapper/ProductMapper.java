package com.atalaykaan.e_commerce_backend.domain.product.mapper;

import com.atalaykaan.e_commerce_backend.domain.product.dto.response.ProductDTO;
import com.atalaykaan.e_commerce_backend.domain.product.model.Product;
import com.atalaykaan.e_commerce_backend.domain.product.model.ProductDocument;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    ProductDTO toDTO(Product product);

    ProductDocument toDocument(Product product);
}
