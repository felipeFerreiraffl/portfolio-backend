package com.portfolio.controller;

import com.portfolio.service.JikanService;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/jikan")
public class JikanController {

    private final JikanService serv;

    public JikanController(JikanService serv) {
        this.serv = serv;
    }

    @Cacheable("anime")
    @GetMapping("/anime/{id}")
    public ResponseEntity<String> getAnimeById(@PathVariable int id) {
        String response = serv.getAnimeById(id);
        return ResponseEntity.ok(response);
    }

    @Cacheable("anime-filter")
    @GetMapping("/anime/filter")
    public ResponseEntity<String> getAnimesByFilter(@RequestParam String filter, @RequestParam int limit) {
        String response = serv.getAnimesByFilter(filter, limit);
        return ResponseEntity.ok(response);
    }

    @Cacheable("manga")
    @GetMapping("/manga/{id}")
    public ResponseEntity<String> getMangaById(@PathVariable int id) {
        String response = serv.getMangaById(id);
        return ResponseEntity.ok(response);
    }

    @Cacheable("manga-filter")
    @GetMapping("/manga/filter")
    public ResponseEntity<String> getMangasByFilter(@RequestParam String filter, @RequestParam int limit) {
        String response = serv.getMangasByFilter(filter, limit);
        return ResponseEntity.ok(response);
    }
}
