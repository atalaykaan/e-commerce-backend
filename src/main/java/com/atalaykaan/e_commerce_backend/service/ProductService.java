package com.atalaykaan.e_commerce_backend.service;

import com.atalaykaan.e_commerce_backend.dto.request.create.CreateProductRequest;
import com.atalaykaan.e_commerce_backend.dto.request.update.UpdateProductRequest;
import com.atalaykaan.e_commerce_backend.dto.response.ProductDTO;
import com.atalaykaan.e_commerce_backend.exception.ProductNotFoundException;
import com.atalaykaan.e_commerce_backend.mapper.ProductMapper;
import com.atalaykaan.e_commerce_backend.model.Product;
import com.atalaykaan.e_commerce_backend.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductService extends BaseService {

    private final ProductRepository productRepository;

    private final ProductMapper productMapper;

    @Transactional
    @Caching(
            put = @CachePut(value = "product", key = "#result.getId()"),
            evict = @CacheEvict(value = "products", allEntries = true)
    )
    public ProductDTO save(CreateProductRequest createProductRequest) {

        LocalDateTime now = LocalDateTime.now();

        Product createdProduct = productRepository.save(
                Product.builder()
                        .name(createProductRequest.getName())
                        .description(createProductRequest.getDescription())
                        .brand(createProductRequest.getBrand())
                        .price(createProductRequest.getPrice())
                        .stock(createProductRequest.getStock())
                        .createdAt(now)
                        .updatedAt(now)
                        .build()
        );

        return productMapper.toDTO(createdProduct);
    }


    @Cacheable(value = "products")
    public List<ProductDTO> findAll() {

        List<ProductDTO> productDTOList = productRepository.findAll()
                .stream()
                .map(productMapper::toDTO)
                .toList();

        if(productDTOList.isEmpty()) {

            throw new ProductNotFoundException("No products were found");
        }

        return productDTOList;
    }

    @Cacheable(value = "product", key = "#id")
    public ProductDTO findById(UUID id) {

        ProductDTO productDTO = productRepository.findById(id)
                .map(productMapper::toDTO)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with id: " + id));

        return productDTO;
    }

    @Transactional
    @Caching(
            put = @CachePut(value = "product", key = "#id"),
            evict = @CacheEvict(value = "products", allEntries = true)
    )
    public ProductDTO updateById(UUID id, UpdateProductRequest updateProductRequest) {

        Product foundProduct = productRepository.findById(id)
                .map(product -> {
                    updateIfExists(updateProductRequest.getName(), product::setName);
                    updateIfExists(updateProductRequest.getDescription(), product::setDescription);
                    updateIfExists(updateProductRequest.getBrand(), product::setBrand);
                    updateIfExists(updateProductRequest.getPrice(), product::setPrice);
                    updateIfExists(updateProductRequest.getStock(), product::setStock);
                    product.setUpdatedAt(LocalDateTime.now());

                    return product;
                })
                .orElseThrow(() -> new ProductNotFoundException("Product not found with id: " + id));

        Product savedProduct = productRepository.save(foundProduct);

        return productMapper.toDTO(savedProduct);
    }

    @Transactional
    @Caching(
            evict = {
                    @CacheEvict(value = "product", key = "#id"),
                    @CacheEvict(value = "products", allEntries = true)
            }
    )
    public void deleteById(UUID id) {

        productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with id: " + id));

        productRepository.deleteById(id);
    }

    @Transactional
    @Caching(
            evict = {
                    @CacheEvict(value = {"product", "products"}, allEntries = true)
            }
    )
    public void deleteAll() {

        if(productRepository.findAll().isEmpty()) {

            throw new ProductNotFoundException("No products were found");
        }

        productRepository.deleteAll();
    }
}
