package com.atalaykaan.e_commerce_backend.domain.product.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import com.atalaykaan.e_commerce_backend.domain.product.mapper.ProductMapper;
import com.atalaykaan.e_commerce_backend.domain.product.model.entity.Product;
import com.atalaykaan.e_commerce_backend.domain.product.model.entity.ProductDocument;
import com.atalaykaan.e_commerce_backend.domain.product.repository.ProductSearchRepository;
import com.atalaykaan.e_commerce_backend.domain.product.util.ESUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

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

    List<ProductDocument> getAllDataFromIndex(String searchIndex) {

        Query query = ESUtil.createMatchQuery();

        if(searchIndex == null || searchIndex.trim().isEmpty()) {

            return Collections.emptyList();
        }

        SearchResponse<ProductDocument> response = null;

        try {
            response = elasticsearchClient.search(
                    q -> q.index(searchIndex).query(query), ProductDocument.class);

            return extractDocumentsFromResponse(response);

        } catch (IOException ex) {

            throw new RuntimeException(ex.getMessage());
        }
    }

    private List<ProductDocument> extractDocumentsFromResponse(SearchResponse<ProductDocument> response) {

        return response
                .hits()
                .hits()
                .stream()
                .map(Hit::source)
                .toList();
    }

    public List<ProductDocument> searchDocumentsByName(String productName) {

        Supplier<Query> query = ESUtil.buildQueryForProductName(productName);

        SearchResponse<ProductDocument> response = null;

        try {
            response = elasticsearchClient.search(q -> q.index("product_index").query(query.get()), ProductDocument.class);
        } catch (IOException ex) {

            throw new RuntimeException(ex);
        }

        return extractDocumentsFromResponse(response);
    }
}
