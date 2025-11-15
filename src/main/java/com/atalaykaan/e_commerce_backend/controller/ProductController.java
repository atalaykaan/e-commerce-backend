package com.atalaykaan.e_commerce_backend.controller;

import com.atalaykaan.e_commerce_backend.model.dto.request.create.CreateProductRequest;
import com.atalaykaan.e_commerce_backend.model.dto.request.update.UpdateProductRequest;
import com.atalaykaan.e_commerce_backend.model.dto.response.ProductDTO;
import com.atalaykaan.e_commerce_backend.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;

import static com.atalaykaan.e_commerce_backend.constants.ApiConstants.*;

@RestController
@RequestMapping(API_PREFIX + API_VERSION + API_PRODUCTS)
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping
    public ResponseEntity<ProductDTO> saveProduct(@Valid @RequestBody CreateProductRequest createProductRequest) {

        ProductDTO savedProductDTO = productService.save(createProductRequest);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedProductDTO.getId())
                .toUri();

        return ResponseEntity.created(location).body(savedProductDTO);
    }

    @GetMapping
    public ResponseEntity<List<ProductDTO>> findAllProducts() {

        List<ProductDTO> productDTOList = productService.findAll();

        return ResponseEntity.ok(productDTOList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> findProductById(@PathVariable UUID id) {

        ProductDTO productDTO = productService.findById(id);

        return ResponseEntity.ok(productDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDTO> updateProductById(@PathVariable UUID id, @Valid @RequestBody UpdateProductRequest updateProductRequest) {

        ProductDTO productDTO = productService.updateById(id, updateProductRequest);

        return ResponseEntity.ok(productDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProductById(@PathVariable UUID id) {

        productService.deleteById(id);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteAllProducts() {

        productService.deleteAll();

        return ResponseEntity.noContent().build();
    }
}
