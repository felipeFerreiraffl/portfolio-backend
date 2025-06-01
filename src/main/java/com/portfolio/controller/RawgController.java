package com.portfolio.controller;

import com.portfolio.service.RawgService;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/rawg")
public class RawgController {
    private final RawgService serv;

    // Injeta a dependência
    public RawgController(RawgService serv) {
        this.serv = serv;
    }

    @Cacheable(value = "games", key = "#id")
    @GetMapping("/{id}")
    public ResponseEntity<String> getGameById(@PathVariable int id) {
        String response = serv.getGameById(id);
        return ResponseEntity.ok(response);
    }

    @Cacheable(value = "games-filter", key = "#ordering + '_' + #pageSize")
    @GetMapping("/filter")
    public ResponseEntity<String> getGamesByFilter(@RequestParam String ordering, @RequestParam int pageSize) {
        String response = serv.getGamesByFilter(ordering, pageSize);
        return ResponseEntity.ok(response);
    }

}
