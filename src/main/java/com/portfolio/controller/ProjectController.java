package com.portfolio.controller;

import com.portfolio.model.Project;
import com.portfolio.service.DrawingService;
import com.portfolio.service.ProjectService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/project")
public class ProjectController {

    @Autowired
    private ProjectService serv;

    @GetMapping
    public List<Project> findAll() {
        return serv.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Project> findById(@PathVariable String id) {
        Project proj = serv.findById(id);
        return ResponseEntity.ok(proj); // HTTP 200
    }

    @PostMapping
    public ResponseEntity<Project> create(@RequestBody @Valid Project proj) {
        Project created = serv.create(proj);
        return ResponseEntity.status(HttpStatus.CREATED).body(created); // HTTP 201
    }

    @PutMapping("/{id}")
    public ResponseEntity<Project> update(@PathVariable String id, @RequestBody @Valid Project proj) {
        Project updated = serv.update(id, proj);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Project> delete(@PathVariable String id) {
        serv.delete(id);
        return ResponseEntity.noContent().build(); // HTTP 204
    }
}
