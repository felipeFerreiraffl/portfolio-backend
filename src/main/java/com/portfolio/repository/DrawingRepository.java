package com.portfolio.repository;

import com.portfolio.model.Drawing;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface DrawingRepository extends MongoRepository<Drawing, String> {
}
