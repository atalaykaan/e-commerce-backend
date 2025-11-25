package com.atalaykaan.e_commerce_backend.domain.product.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import com.atalaykaan.e_commerce_backend.domain.product.mapper.ProductMapper;
import com.atalaykaan.e_commerce_backend.domain.product.model.Product;
import com.atalaykaan.e_commerce_backend.domain.product.model.ProductDocument;
import com.atalaykaan.e_commerce_backend.domain.product.repository.ProductSearchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductSearchService {

    private final ProductSearchRepository productSearchRepository;

    private final ProductMapper productMapper;

    private final ElasticsearchClient elasticsearchClient;

    public void indexProduct(Product product) {

        ProductDocument productDocument = productMapper.toDocument(product);

        productSearchRepository.save(productDocument);
    }

    public void deleteProduct(Long productId) {

        productSearchRepository.deleteById(productId);
    }

    public void deleteAllProducts() {

        productSearchRepository.deleteAll();
    }

    List<ProductDocument> searchByText(String searchText) {

        if(searchText == null || searchText.trim().isEmpty()) {

            return Collections.emptyList();
        }

        try {
            SearchResponse<ProductDocument> response = elasticsearchClient.search(s -> s
                    .index("product_index")
                    .query(qb -> qb
                            .match(m -> m
                                    .field("name")
                                    .query(searchText)
                    )
            ), ProductDocument.class);

            return extractItemsFromResponse(response);

        } catch (IOException ex) {

            throw new RuntimeException();
        }
    }

    private List<ProductDocument> extractItemsFromResponse(SearchResponse<ProductDocument> response) {

        return response
                .hits()
                .hits()
                .stream()
                .map(Hit::source)
                .toList();
    }
}
