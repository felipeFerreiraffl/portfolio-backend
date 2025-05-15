package com.portfolio.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Optional;

@Service
public class RawgService {

    // Permite a conexão com outras APIs
    private final RestTemplate restTemplate = new RestTemplate();
    @Value("${api.rawg.url}")
    private String rawgApiUrl;
    @Value("${api.rawg.key}")
    private String rawgApiKey;

    // Busca informações de um jogo específico
    public String getGameById(int id) {
        // Cria uma URI
        URI uri = UriComponentsBuilder
                .fromUriString(rawgApiUrl + "/" + id)
                .queryParam("key", rawgApiKey)
                .build()
                .toUri();

        // Converte a URI em String
        return restTemplate.getForObject(uri, String.class);
    }

    // Busca jogos por filtro (popularidade, por nota, etc)
    public String getGamesByFilter(String ordering, int pageSize) {
        URI uri = UriComponentsBuilder
                .fromUriString(rawgApiUrl)
                .queryParamIfPresent("ordering", Optional.ofNullable(ordering)) // Evita valores nulos
                .queryParam("page_size", pageSize)
                .queryParam("key", rawgApiKey)
                .build()
                .toUri();

        return restTemplate.getForObject(uri, String.class);
    }
}
