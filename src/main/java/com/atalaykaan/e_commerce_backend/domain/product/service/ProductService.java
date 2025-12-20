package com.atalaykaan.e_commerce_backend.domain.product.service;

import com.atalaykaan.e_commerce_backend.common.exception.InvalidProductPriceException;
import com.atalaykaan.e_commerce_backend.common.exception.InvalidProductQuantityException;
import com.atalaykaan.e_commerce_backend.domain.product.model.dto.request.CreateProductRequest;
import com.atalaykaan.e_commerce_backend.domain.product.model.dto.request.UpdateProductRequest;
import com.atalaykaan.e_commerce_backend.domain.product.model.dto.response.ProductDTO;
import com.atalaykaan.e_commerce_backend.common.exception.ProductNotFoundException;
import com.atalaykaan.e_commerce_backend.domain.product.mapper.ProductMapper;
import com.atalaykaan.e_commerce_backend.domain.product.model.entity.Product;
import com.atalaykaan.e_commerce_backend.domain.product.repository.ProductRepository;
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
import java.util.function.Consumer;

@Service
@RequiredArgsConstructor
public class ProductService{

    private final ProductRepository productRepository;

    private final ProductMapper productMapper;

    private final ProductSearchService productSearchService;

    private final SequenceGeneratorService sequenceGeneratorService;

    private static final String PRODUCT_CACHE = "product";

    private static final String PRODUCT_LIST_CACHE = "products";

    @Transactional
    @Caching(
            put = @CachePut(value = PRODUCT_CACHE, key = "#result.getId()"),
            evict = @CacheEvict(value = PRODUCT_LIST_CACHE, key = "'allProducts'")
    )
    public ProductDTO saveProduct(CreateProductRequest createProductRequest) {

        validateProductPrice(createProductRequest.getPrice());

        LocalDateTime now = LocalDateTime.now();

        Product product = Product.builder()
                .id(sequenceGeneratorService.generateSequence(Product.SEQUENCE_NAME))
                .name(createProductRequest.getName())
                .description(createProductRequest.getDescription())
                .brand(createProductRequest.getBrand())
                .price(createProductRequest.getPrice())
                .stock(createProductRequest.getStock())
                .createdAt(now)
                .updatedAt(now)
                .build();

        productSearchService.indexProduct(product);

        Product createdProduct = productRepository.save(product);

        ProductDTO productDTO = productMapper.toDTO(createdProduct);

        return productDTO;
    }

    @Cacheable(value = PRODUCT_CACHE, key = "#id")
    public ProductDTO findProductById(Long id) {

        ProductDTO productDTO = productRepository.findById(id)
                .map(productMapper::toDTO)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with id: " + id));

        return productDTO;
    }

    @Cacheable(value = PRODUCT_LIST_CACHE, key = "'allProducts'")
    public List<ProductDTO> findAllProducts() {

        List<ProductDTO> productDTOList = productRepository.findAll()
                .stream()
                .map(productMapper::toDTO)
                .toList();

        if(productDTOList.isEmpty()) {

            throw new ProductNotFoundException("No products were found");
        }

        return productDTOList;
    }

    @Transactional
    @Caching(
            put = @CachePut(value = PRODUCT_CACHE, key = "#id"),
            evict = @CacheEvict(value = PRODUCT_LIST_CACHE, key = "'allProducts'")
    )
    public ProductDTO updateProductById(Long id, UpdateProductRequest updateProductRequest) {

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

        productSearchService.indexProduct(foundProduct);

        Product savedProduct = productRepository.save(foundProduct);

        ProductDTO productDTO = productMapper.toDTO(savedProduct);

        return productDTO;
    }

    private <T> void updateIfExists(T value, Consumer<T> setter) {

        if(value != null) {

            setter.accept(value);
        }
    }

    @Transactional
    @Caching(
            put = @CachePut(value = PRODUCT_CACHE, key = "#id"),
            evict = @CacheEvict(value = PRODUCT_LIST_CACHE, key = "'allProducts'")
    )
    ProductDTO decreaseProductStock(Long id, Long quantity) {

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with id: " + id));

        long newStock = product.getStock() - quantity;

        if(newStock < 0) {

            throw new InvalidProductQuantityException("Quantity cannot be less than stock");
        }

        product.setStock(newStock);

        Product savedProduct = productRepository.save(product);

        productSearchService.indexProduct(savedProduct);

        ProductDTO productDTO = productMapper.toDTO(savedProduct);

        return productDTO;
    }

    @Transactional
    @Caching(
            evict = {
                    @CacheEvict(value = PRODUCT_CACHE, key = "#id"),
                    @CacheEvict(value = PRODUCT_LIST_CACHE, key = "'allProducts'")
            }
    )
    public void deleteProductById(Long id) {

        productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with id: " + id));

        productSearchService.deleteProduct(id);

        productRepository.deleteById(id);
    }

    @Transactional
    @Caching(
            evict = {
                    @CacheEvict(value = {PRODUCT_CACHE, PRODUCT_LIST_CACHE}, allEntries = true)
            }
    )
    public void deleteAllProducts() {

        if(productRepository.findAll().isEmpty()) {

            throw new ProductNotFoundException("No products were found");
        }

        productSearchService.deleteAllProducts();

        productRepository.deleteAll();
    }

    private void validateProductPrice(BigDecimal price) {

        if(price.compareTo(BigDecimal.ZERO) < 0) {

            throw new InvalidProductPriceException("Product price cannot be lower than zero");
        }
    }

    public List<ProductDTO> getAllDataFromIndex(String searchIndex) {

        List<ProductDTO> productDocumentList = productSearchService.getAllDataFromIndex(searchIndex)
                .stream()
                .map(productMapper::documentToDTO)
                .toList();

        if(productDocumentList.isEmpty()) {

            throw new ProductNotFoundException("No products were found");
        }

        return productDocumentList;
    }

    @Cacheable(value = PRODUCT_LIST_CACHE, key = "#productName.trim().toLowerCase()", unless = "#result.isEmpty()")
    public List<ProductDTO> searchProductsByName(String productName) {

        List<ProductDTO> productDocumentList = productSearchService.searchDocumentsByName(productName)
                .stream()
                .map(productMapper::documentToDTO)
                .toList();

        if(productDocumentList.isEmpty()) {

            throw new ProductNotFoundException("No products were found");
        }

        return productDocumentList;
    }
}
