package com.portfolio.service;

import com.portfolio.model.Position;
import com.portfolio.repository.PositionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PositionService {

    @Autowired
    private PositionRepository repo;

    public List<Position> findAll() {
        return repo.findAll();
    }

    public Position findById(String id) {
        return repo.findById(id).orElseThrow(() -> new RuntimeException("Position not found."));
    }

    public Position create(Position pos) {
        return repo.save(pos);
    }

    public void update() {
    }

    public void delete() {
    }
}
