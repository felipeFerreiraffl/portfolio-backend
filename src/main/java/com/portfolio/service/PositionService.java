package com.portfolio.service;

import com.portfolio.model.Position;
import com.portfolio.repository.PositionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

// Serviço para controller
@Service
public class PositionService {

    @Autowired
    private PositionRepository repo;

    // GET
    public List<Position> findAll() {
        return repo.findAll();
    }

    // GET
    public Position findById(String id) {
        return repo.findById(id).orElseThrow(() -> new RuntimeException("Position not found."));
    }

    // POST
    public Position create(Position pos) {
        return repo.save(pos);
    }

    // PUT
    public Position update(String id, Position pos) {
        Position existing = repo.findById(id).orElseThrow(() -> new RuntimeException("Object not found."));
        existing.setName(pos.getName());
        existing.setAbbr(pos.getAbbr());
        existing.setDesc(pos.getDesc());

        return repo.save(existing);
    }

    // DELETE
    public void delete(String id) {
        if (!repo.existsById(id)) {
            throw new RuntimeException("Object not found.");
        }

        repo.deleteById(id);
    }
}
