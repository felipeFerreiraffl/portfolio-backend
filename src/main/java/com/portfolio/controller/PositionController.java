package com.portfolio.controller;

import com.portfolio.model.Position;
import com.portfolio.service.PositionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/position")
public class PositionController {

    @Autowired
    private PositionService serv;

    @GetMapping
    public List<Position> findAll() {
        return serv.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Position> findById(@PathVariable String id) {
        Position pos = serv.findById(id);
        return ResponseEntity.ok(pos); // HTTP 200
    }

    @PostMapping
    public ResponseEntity<Position> create(@RequestBody @Valid Position pos) {
        Position created = serv.create(pos);
        return ResponseEntity.status(HttpStatus.CREATED).body(created); // HTTP 201
    }

    @PutMapping("/{id}")
    public ResponseEntity<Position> update(@PathVariable String id, @RequestBody @Valid Position pos) {
        Position updated = serv.update(id, pos);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Position> delete(@PathVariable String id) {
        serv.delete(id);
        return ResponseEntity.noContent().build(); // HTTP 204
    }
}
