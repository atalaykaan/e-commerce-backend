package com.atalaykaan.e_commerce_backend.domain.product.repository;

import com.atalaykaan.e_commerce_backend.domain.product.model.ProductDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface ProductSearchRepository extends ElasticsearchRepository<ProductDocument, Long> {
}
