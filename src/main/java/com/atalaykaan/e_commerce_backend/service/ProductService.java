package com.atalaykaan.e_commerce_backend.service;

import com.atalaykaan.e_commerce_backend.exception.InvalidProductPriceException;
import com.atalaykaan.e_commerce_backend.model.dto.request.create.CreateProductRequest;
import com.atalaykaan.e_commerce_backend.model.dto.request.update.UpdateProductRequest;
import com.atalaykaan.e_commerce_backend.model.dto.response.ProductDTO;
import com.atalaykaan.e_commerce_backend.exception.ProductNotFoundException;
import com.atalaykaan.e_commerce_backend.mapper.ProductMapper;
import com.atalaykaan.e_commerce_backend.model.entity.Product;
import com.atalaykaan.e_commerce_backend.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductService extends BaseService {

    private final ProductRepository productRepository;

    private final ProductMapper productMapper;

    private static final String PRODUCT_CACHE = "product";

    private static final String PRODUCT_LIST_CACHE = "products";

    @Transactional
    @Caching(
            put = @CachePut(value = PRODUCT_CACHE, key = "#result.getId()"),
            evict = @CacheEvict(value = PRODUCT_LIST_CACHE, key = "'allProducts'")
    )
    public ProductDTO save(CreateProductRequest createProductRequest) {

        validateProductPrice(createProductRequest.getPrice());

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

        ProductDTO productDTO = productMapper.toDTO(createdProduct);

        System.out.println("no cache");

        return productDTO;
    }

    @Cacheable(value = PRODUCT_CACHE, key = "#id")
    public ProductDTO findById(UUID id) {

        ProductDTO productDTO = productRepository.findById(id)
                .map(productMapper::toDTO)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with id: " + id));

        System.out.println("no cache");

        return productDTO;
    }

    @Cacheable(value = PRODUCT_LIST_CACHE, key = "'allProducts'")
    public List<ProductDTO> findAll() {

        List<ProductDTO> productDTOList = productRepository.findAll()
                .stream()
                .map(productMapper::toDTO)
                .toList();

        if(productDTOList.isEmpty()) {

            throw new ProductNotFoundException("No products were found");
        }

        System.out.println("no cache");

        return productDTOList;
    }

    @Transactional
    @Caching(
            put = @CachePut(value = PRODUCT_CACHE, key = "#id"),
            evict = @CacheEvict(value = PRODUCT_LIST_CACHE, key = "'allProducts'")
    )
    public ProductDTO updateById(UUID id, UpdateProductRequest updateProductRequest) {

        if(updateProductRequest.getPrice() != null) {

            validateProductPrice(updateProductRequest.getPrice());
        }

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

        ProductDTO productDTO = productMapper.toDTO(savedProduct);

        System.out.println("no cache");

        return productDTO;
    }

    @Transactional
    @Caching(
            evict = {
                    @CacheEvict(value = PRODUCT_CACHE, key = "#id"),
                    @CacheEvict(value = PRODUCT_LIST_CACHE, key = "'allProducts'")
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
                    @CacheEvict(value = {PRODUCT_CACHE, PRODUCT_LIST_CACHE}, allEntries = true)
            }
    )
    public void deleteAll() {

        if(productRepository.findAll().isEmpty()) {

            throw new ProductNotFoundException("No products were found");
        }

        System.out.println("no cache");

        productRepository.deleteAll();
    }

    private void validateProductPrice(BigDecimal price) {

        if(price.compareTo(BigDecimal.ZERO) < 0) {

            throw new InvalidProductPriceException("Product price cannot be lower than zero");
        }
    }
}
