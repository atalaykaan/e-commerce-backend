package com.atalaykaan.e_commerce_backend.domain.product.controller;

import com.atalaykaan.e_commerce_backend.domain.product.model.dto.request.CreateProductRequest;
import com.atalaykaan.e_commerce_backend.domain.product.model.dto.request.UpdateProductRequest;
import com.atalaykaan.e_commerce_backend.domain.product.model.dto.response.ProductDTO;
import com.atalaykaan.e_commerce_backend.domain.product.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

import static com.atalaykaan.e_commerce_backend.common.constants.ApiConstants.*;

@RestController
@RequestMapping(API_PREFIX + API_VERSION + API_PRODUCTS)
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping
    public ResponseEntity<ProductDTO> saveProduct(@Valid @RequestBody CreateProductRequest createProductRequest) {

        ProductDTO savedProductDTO = productService.saveProduct(createProductRequest);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedProductDTO.getId())
                .toUri();

        return ResponseEntity.created(location).body(savedProductDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> findProductById(@PathVariable Long id) {

        ProductDTO productDTO = productService.findProductById(id);

        return ResponseEntity.ok(productDTO);
    }

    @GetMapping
    public ResponseEntity<List<ProductDTO>> findAllProducts() {

        List<ProductDTO> productDTOList = productService.findAllProducts();

        return ResponseEntity.ok(productDTOList);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDTO> updateProductById(@PathVariable Long id, @Valid @RequestBody UpdateProductRequest updateProductRequest) {

        ProductDTO productDTO = productService.updateProductById(id, updateProductRequest);

        return ResponseEntity.ok(productDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProductById(@PathVariable Long id) {

        productService.deleteProductById(id);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteAllProducts() {

        productService.deleteAllProducts();

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/index/{searchIndex}")
    public ResponseEntity<List<ProductDTO>> getAllDataFromIndex(@PathVariable String searchIndex) {

        List<ProductDTO> productDocument = productService.getAllDataFromIndex(searchIndex);

        return ResponseEntity.ok(productDocument);
    }

    @GetMapping("/search")
    public ResponseEntity<List<ProductDTO>> searchProductsByName(@RequestParam String name) {

        List<ProductDTO> productDocument = productService.searchProductsByName(name);

        return ResponseEntity.ok(productDocument);
    }
}
