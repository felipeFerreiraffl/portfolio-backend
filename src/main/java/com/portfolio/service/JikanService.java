package com.portfolio.service;

import com.portfolio.config.JikanRateLimit;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
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
            throw new RuntimeException("Limite de requisições atingido. Tente novamente mais tarde.");
        }

        URI uri = UriComponentsBuilder
                .fromUriString(jikanApiUrl + "/anime/" + id)
                .build()
                .toUri();

        return restTemplate.getForObject(uri, String.class);
    }

    // Busca animes por filtro (popularidade, nota, etc)
    @Retryable(
            value = {RestClientException.class}, // Classes de exceções que disparam retry
            maxAttempts = 3,
            backoff = @Backoff(delay = 2500) // 2000ms entre cada tentativa
    )
    public String getAnimesByFilter(String filter, int limit) {
        if (!rateLimit.tryConsume()) {
            throw new RuntimeException("Limite de requisições atingido. Tente novamente mais tarde.");
        }

        URI uri = UriComponentsBuilder
                .fromUriString(jikanApiUrl + "/top/anime")
                .queryParam("filter", filter)
                .queryParam("limit", limit)
                .build()
                .toUri();


        return restTemplate.getForObject(uri, String.class);
    }

    // Busca um mangá pelo ID
    @Retryable(
            value = {RestClientException.class}, // Classes de exceções que disparam retry
            maxAttempts = 3,
            backoff = @Backoff(delay = 2500) // 2000ms entre cada tentativa
    )
    public String getMangaById(int id) {
        if (!rateLimit.tryConsume()) {
            throw new RuntimeException("Limite de requisições atingido. Tente novamente mais tarde.");
        }

        URI uri = UriComponentsBuilder
                .fromUriString(jikanApiUrl + "/manga/" + id)
                .build()
                .toUri();

        return restTemplate.getForObject(uri, String.class);
    }

    // Busca mangá por filtro (popularidade, nota, etc)
    @Retryable(
            value = {RestClientException.class}, // Classes de exceções que disparam retry
            maxAttempts = 3,
            backoff = @Backoff(delay = 2500) // 2000ms entre cada tentativa
    )
    public String getMangasByFilter(String filter, int limit) {
        if (!rateLimit.tryConsume()) {
            throw new RuntimeException("Limite de requisições atingido. Tente novamente mais tarde.");
        }

        URI uri = UriComponentsBuilder
                .fromUriString(jikanApiUrl + "/top/manga")
                .queryParam("filter", filter)
                .queryParam("limit", limit)
                .build()
                .toUri();

        return restTemplate.getForObject(uri, String.class);
    }

    // Fallback caso o retry falhe
    public String recover(RestClientException e, String filter, int limit) {
        return "{\"message\" : \"API temporariamente indisponível. Tente novamente mais tarde.\"}";
    }
}
