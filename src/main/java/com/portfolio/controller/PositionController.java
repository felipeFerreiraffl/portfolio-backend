package com.portfolio.controller;

import com.portfolio.model.Position;
import com.portfolio.service.PositionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
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
        return ResponseEntity.ok(pos);
    }

    @PostMapping
    public ResponseEntity<Position> create(@RequestBody @Valid Position pos) {
        Position p = serv.create(pos);
        return ResponseEntity.status(HttpStatus.CREATED).body(p);
    }
}
