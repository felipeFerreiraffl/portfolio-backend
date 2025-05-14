package com.portfolio.controller;

import com.portfolio.model.Drawing;
import com.portfolio.service.DrawingService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/drawing")
public class DrawingController {

    @Autowired
    private DrawingService serv;

    @GetMapping
    public List<Drawing> findAll() {
        return serv.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Drawing> findById(@PathVariable String id) {
        Drawing draw = serv.findById(id);
        return ResponseEntity.ok(draw); // HTTP 200
    }

    @PostMapping
    public ResponseEntity<Drawing> create(@RequestBody @Valid Drawing draw) {
        Drawing created = serv.create(draw);
        return ResponseEntity.status(HttpStatus.CREATED).body(created); // HTTP 201
    }

    @PutMapping("/{id}")
    public ResponseEntity<Drawing> update(@PathVariable String id, @RequestBody @Valid Drawing draw) {
        Drawing updated = serv.update(id, draw);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Drawing> delete(@PathVariable String id) {
        serv.delete(id);
        return ResponseEntity.noContent().build(); // HTTP 204
    }
}
