package com.portfolio.controller;

import com.portfolio.model.Position;
import com.portfolio.service.PositionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "/api/position")
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
}
