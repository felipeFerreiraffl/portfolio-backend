package com.portfolio.repository;

import com.portfolio.model.Position;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PositionRepository extends MongoRepository<Position, String> {
}
