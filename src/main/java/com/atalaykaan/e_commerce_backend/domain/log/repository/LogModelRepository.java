package com.atalaykaan.e_commerce_backend.log.repository;

import com.atalaykaan.e_commerce_backend.log.model.LogModel;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface LogModelRepository extends MongoRepository<LogModel, String> {
}
