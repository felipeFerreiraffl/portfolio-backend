package com.portfolio.controller;

import com.portfolio.model.Player;
import com.portfolio.service.PlayerService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/player")
public class PlayerController {

    @Autowired
    private PlayerService serv;

    @GetMapping
    public List<Player> findAll() {
        return serv.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Player> findById(@PathVariable String id) {
        Player pyr = serv.findById(id);
        return ResponseEntity.ok(pyr); // HTTP 200
    }

    @PostMapping
    public ResponseEntity<Player> create(@RequestBody @Valid Player pyr) {
        Player created = serv.create(pyr);
        return ResponseEntity.status(HttpStatus.CREATED).body(created); // HTTP 201
    }

    @PutMapping("/{id}")
    public ResponseEntity<Player> update(@PathVariable String id, @RequestBody @Valid Player pyr) {
        Player updated = serv.update(id, pyr);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Player> delete(@PathVariable String id) {
        serv.delete(id);
        return ResponseEntity.noContent().build(); // HTTP 204
    }
}
