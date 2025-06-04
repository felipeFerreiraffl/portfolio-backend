package com.portfolio.controller;

import com.portfolio.service.RawgService;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
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

    @Cacheable(value = "games", key = "#id", unless = "#result == null")
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getGameById(@PathVariable int id) {
        String response = serv.getGameById(id);

        // Definição de header como JSON
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Cache-Control", "public, max-age=3600");

        return ResponseEntity.ok().headers(headers).body(response);
    }

    @Cacheable(value = "games-filter", key = "#ordering + '_' + #pageSize", unless = "#result == null")
    @GetMapping(value = "/filter", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getGamesByFilter(@RequestParam String ordering, @RequestParam int pageSize) {
        String response = serv.getGamesByFilter(ordering, pageSize);

        // Definição de header como JSON
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Cache-Control", "public, max-age=3600");

        return ResponseEntity.ok().headers(headers).body(response);
    }

}
