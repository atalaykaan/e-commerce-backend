package com.atalaykaan.e_commerce_backend.domain.product.mapper;

import com.atalaykaan.e_commerce_backend.domain.product.model.dto.response.ProductDTO;
import com.atalaykaan.e_commerce_backend.domain.product.model.entity.Product;
import com.atalaykaan.e_commerce_backend.domain.product.model.entity.ProductDocument;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {

    public ProductDTO toDTO(Product product) {

        return ProductDTO.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .brand(product.getBrand())
                .price(product.getPrice())
                .stock(product.getStock())
                .createdAt(product.getCreatedAt())
                .updatedAt(product.getUpdatedAt())
                .build();
    }

    public ProductDocument toDocument(Product product) {

        return ProductDocument.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .brand(product.getBrand())
                .price(product.getPrice())
                .stock(product.getStock())
                .createdAt(product.getCreatedAt())
                .updatedAt(product.getUpdatedAt())
                .build();
    }

    public ProductDTO documentToDTO(ProductDocument productDocument) {

        return ProductDTO.builder()
                .id(productDocument.getId())
                .name(productDocument.getName())
                .description(productDocument.getDescription())
                .brand(productDocument.getBrand())
                .price(productDocument.getPrice())
                .stock(productDocument.getStock())
                .createdAt(productDocument.getCreatedAt())
                .updatedAt(productDocument.getUpdatedAt())
                .build();
    }
}
