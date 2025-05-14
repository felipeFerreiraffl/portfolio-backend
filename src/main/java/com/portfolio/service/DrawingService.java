package com.portfolio.service;

import com.portfolio.model.Drawing;
import com.portfolio.repository.DrawingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DrawingService {

    @Autowired
    private DrawingRepository repo;

    // GET
    public List<Drawing> findAll() {
        return repo.findAll();
    }

    // GET
    public Drawing findById(String id) {
        return repo.findById(id).orElseThrow(() -> new RuntimeException("Drawing not found."));
    }

    // POST
    public Drawing create(Drawing draw) {
        return repo.save(draw);
    }

    // PUT
    public Drawing update(String id, Drawing draw) {
        Drawing existing = repo.findById(id).orElseThrow(() -> new RuntimeException("Object not found."));
        existing.setImgSrc(draw.getImgSrc());

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
