package com.portfolio.controller;

import com.portfolio.service.JikanService;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/jikan")
public class JikanController {

    private final JikanService serv;

    public JikanController(JikanService serv) {
        this.serv = serv;
    }

    @Cacheable(value = "anime", key = "#id", unless = "#result == null")
    @GetMapping(value = "/anime/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getAnimeById(@PathVariable int id) {
        String response = serv.getAnimeById(id);

        // Definição de header como JSON
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Cache-Control", "public, max-age=3600");

        return ResponseEntity.ok().headers(headers).body(response);
    }

    @Cacheable(value = "anime-filter", key = "#filter + '_' + #limit", unless = "#result == null")
    @GetMapping(value = "/anime/filter", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getAnimesByFilter(@RequestParam String filter, @RequestParam int limit) {
        String response = serv.getAnimesByFilter(filter, limit);

        // Definição de header como JSON
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Cache-Control", "public, max-age=3600");

        return ResponseEntity.ok().headers(headers).body(response);
    }

    @Cacheable(value = "manga", key = "#id", unless = "#result == null")
    @GetMapping(value = "/manga/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getMangaById(@PathVariable int id) {
        String response = serv.getMangaById(id);

        // Definição de header como JSON
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Cache-Control", "public, max-age=3600");

        return ResponseEntity.ok().headers(headers).body(response);
    }

    @Cacheable(value = "manga-filter", key = "#filter + '_' + #limit", unless = "#result == null")
    @GetMapping(value = "/manga/filter", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getMangasByFilter(@RequestParam String filter, @RequestParam int limit) {
        String response = serv.getMangasByFilter(filter, limit);

        // Definição de header como JSON
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Cache-Control", "public, max-age=3600");

        return ResponseEntity.ok().headers(headers).body(response);
    }
}
