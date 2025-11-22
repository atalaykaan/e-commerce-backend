package com.atalaykaan.e_commerce_backend.domain.product.model.sequence;

import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document(collection = "database_collections")
public class DatabaseSequence {

    @Id
    private String id;

    private long seq;
}
