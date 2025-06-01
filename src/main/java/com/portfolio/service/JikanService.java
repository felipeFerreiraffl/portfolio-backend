package com.portfolio.service;

import com.portfolio.config.JikanRateLimit;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Service
public class JikanService {

    private final RestTemplate restTemplate;
    private final JikanRateLimit rateLimit;

    @Value("${api.jikan.url}")
    private String jikanApiUrl;

    public JikanService(JikanRateLimit rateLimit) {
        this.restTemplate = new RestTemplate();
        this.rateLimit = rateLimit;
    }

    // Busca um anime pelo ID
    @Retryable(
            value = {RestClientException.class}, // Classes de exceções que disparam retry
            maxAttempts = 3,
            backoff = @Backoff(delay = 2500) // 2000ms entre cada tentativa
    )
    public String getAnimeById(int id) {
        if (!rateLimit.tryConsume()) {
            throw new ResponseStatusException(HttpStatus.TOO_MANY_REQUESTS, "Limite de requisições");
        }

        // Constrói a URI para o path da Jikan
        URI uri = UriComponentsBuilder
                .fromUriString(jikanApiUrl + "/anime/" + id)
                .build()
                .toUri();

        try {
            return restTemplate.getForObject(uri, String.class);

        } catch (RestClientException e) {
            throw new RuntimeException("Erro ao buscar dados da Jikan pelo ID: " + e.getMessage(), e);
        }
    }

    // Busca animes por filtro (popularidade, nota, etc)
    @Retryable(
            value = {RestClientException.class}, // Classes de exceções que disparam retry
            maxAttempts = 3,
            backoff = @Backoff(delay = 2500) // 2000ms entre cada tentativa
    )
    public String getAnimesByFilter(String filter, int limit) {
        if (!rateLimit.tryConsume()) {
            throw new ResponseStatusException(HttpStatus.TOO_MANY_REQUESTS, "Limite de requisições");
        }

        UriComponentsBuilder uriBuilder = UriComponentsBuilder
                .fromUriString(jikanApiUrl + "/top/anime")
                .queryParam("limit", limit);

        // Inclui filter caso não seja nula, se não, fica vazio (retorna por nota)
        if (filter != null && !filter.isBlank()) {
            uriBuilder.queryParam("filter", filter);
        }

        URI uri = uriBuilder.build().toUri();


        try {
            return restTemplate.getForObject(uri, String.class);

        } catch (RestClientException e) {
            throw new RuntimeException("Erro ao buscar dados da Jikan pelo ID: " + e.getMessage(), e);
        }
    }

    // Busca um mangá pelo ID
    @Retryable(
            value = {RestClientException.class}, // Classes de exceções que disparam retry
            maxAttempts = 3,
            backoff = @Backoff(delay = 2500) // 2000ms entre cada tentativa
    )
    public String getMangaById(int id) {
        if (!rateLimit.tryConsume()) {
            throw new ResponseStatusException(HttpStatus.TOO_MANY_REQUESTS, "Limite de requisições");
        }

        URI uri = UriComponentsBuilder
                .fromUriString(jikanApiUrl + "/manga/" + id)
                .build()
                .toUri();

        try {
            return restTemplate.getForObject(uri, String.class);

        } catch (RestClientException e) {
            throw new RuntimeException("Erro ao buscar dados da Jikan pelo ID: " + e.getMessage(), e);
        }
    }

    // Busca mangá por filtro (popularidade, nota, etc)
    @Retryable(
            value = {RestClientException.class}, // Classes de exceções que disparam retry
            maxAttempts = 3,
            backoff = @Backoff(delay = 2500) // 2000ms entre cada tentativa
    )
    public String getMangasByFilter(String filter, int limit) {
        if (!rateLimit.tryConsume()) {
            throw new ResponseStatusException(HttpStatus.TOO_MANY_REQUESTS, "Limite de requisições");
        }

        UriComponentsBuilder uriBuilder = UriComponentsBuilder
                .fromUriString(jikanApiUrl + "/top/manga")
                .queryParam("limit", limit);

        // Inclui filter caso não seja nula, se não, fica vazio (retorna por nota)
        if (filter != null && !filter.isBlank()) {
            uriBuilder.queryParam("filter", filter);
        }

        URI uri = uriBuilder.build().toUri();

        try {
            return restTemplate.getForObject(uri, String.class);

        } catch (RestClientException e) {
            throw new RuntimeException("Erro ao buscar dados da Jikan pelo ID: " + e.getMessage(), e);
        }
    }

    // Fallback caso o retry falhe
    public String recover(RestClientException e, String filter, int limit) {
        return "{\"message\" : \"API temporariamente indisponível. Tente novamente mais tarde.\"}";
    }
}
