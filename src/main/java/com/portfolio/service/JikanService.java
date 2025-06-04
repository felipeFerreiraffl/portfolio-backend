package com.portfolio.service;

import com.portfolio.config.JikanRateLimit;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
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
            value = {RestClientException.class, HttpServerErrorException.class}, // Classes de exceções que disparam retry
            maxAttempts = 3,
            backoff = @Backoff(delay = 1000, multiplier = 2) // Backoff exponencial
    )
    public String getAnimeById(int id) {
        if (!rateLimit.tryConsume(2000)) {
            throw new ResponseStatusException(
                    HttpStatus.TOO_MANY_REQUESTS,
                    "Limite de requisições. Tente novamente em alguns segundos.");
        }

        // Constrói a URI para o path da Jikan
        URI uri = UriComponentsBuilder
                .fromUriString(jikanApiUrl + "/anime/" + id)
                .build()
                .toUri();

        try {
            return restTemplate.getForObject(uri, String.class);

        } catch (HttpClientErrorException.TooManyRequests e) { // Erro 429
            throw new ResponseStatusException(HttpStatus.TOO_MANY_REQUESTS, "API externa com rate limit. Aguarde alguns segundos.");
        } catch (HttpClientErrorException.NotFound e) { // Erro 404
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Anime com ID " + id + " não foi encontrado.");
        } catch (HttpServerErrorException e) { // Erro 503
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "Erro na API externa. Tente novamente mais tarde.");
        } catch (RestClientException e) { // Erro 500
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Erro interno ao buscar anime: " + e.getMessage());
        }
    }

    // Busca animes por filtro (popularidade, nota, etc)
    @Retryable(
            value = {RestClientException.class, HttpServerErrorException.class}, // Classes de exceções que disparam retry
            maxAttempts = 3,
            backoff = @Backoff(delay = 1000, multiplier = 2)
    )
    public String getAnimesByFilter(String filter, int limit) {
        if (!rateLimit.tryConsume(2000)) {
            throw new ResponseStatusException(HttpStatus.TOO_MANY_REQUESTS, "Limite de requisições. Tente novamente em alguns segundos. Tente novamente em alguns segundos");
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

        } catch (HttpClientErrorException.TooManyRequests e) { // Erro 429
            throw new ResponseStatusException(HttpStatus.TOO_MANY_REQUESTS, "API externa com rate limit. Aguarde alguns segundos.");
        } catch (HttpServerErrorException e) { // Erro 503
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "Erro na API externa. Tente novamente mais tarde.");
        } catch (RestClientException e) { // Erro 500
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Erro interno ao buscar animes pelo filtro " + filter + " :" + e.getMessage());
        }
    }

    // Busca um mangá pelo ID
    @Retryable(
            value = {RestClientException.class, HttpServerErrorException.class}, // Classes de exceções que disparam retry
            maxAttempts = 3,
            backoff = @Backoff(delay = 1000, multiplier = 2)
    )
    public String getMangaById(int id) {
        if (!rateLimit.tryConsume()) {
            throw new ResponseStatusException(HttpStatus.TOO_MANY_REQUESTS, "Limite de requisições. Tente novamente em alguns segundos");
        }

        URI uri = UriComponentsBuilder
                .fromUriString(jikanApiUrl + "/manga/" + id)
                .build()
                .toUri();

        try {
            return restTemplate.getForObject(uri, String.class);

        } catch (HttpClientErrorException.TooManyRequests e) { // Erro 429
            throw new ResponseStatusException(HttpStatus.TOO_MANY_REQUESTS, "API externa com rate limit. Aguarde alguns segundos.");
        } catch (HttpClientErrorException.NotFound e) { // Erro 404
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Mangá com ID " + id + " não foi encontrado.");
        } catch (HttpServerErrorException e) { // Erro 503
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "Erro na API externa. Tente novamente mais tarde.");
        } catch (RestClientException e) { // Erro 500
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Erro interno ao buscar mangá: " + e.getMessage());
        }
    }

    // Busca mangá por filtro (popularidade, nota, etc)
    @Retryable(
            value = {RestClientException.class, HttpServerErrorException.class}, // Classes de exceções que disparam retry
            maxAttempts = 3,
            backoff = @Backoff(delay = 1000, multiplier = 2)
    )
    public String getMangasByFilter(String filter, int limit) {
        if (!rateLimit.tryConsume()) {
            throw new ResponseStatusException(HttpStatus.TOO_MANY_REQUESTS, "Limite de requisições. Tente novamente em alguns segundos");
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

        } catch (HttpClientErrorException.TooManyRequests e) { // Erro 429
            throw new ResponseStatusException(HttpStatus.TOO_MANY_REQUESTS, "API externa com rate limit. Aguarde alguns segundos.");
        } catch (HttpServerErrorException e) { // Erro 503
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "Erro na API externa. Tente novamente mais tarde.");
        } catch (RestClientException e) { // Erro 500
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Erro interno ao buscar mangás pelo filtro " + filter + " :" + e.getMessage());
        }
    }

    // Fallback caso o retry falhe para IDs
    @Recover
    public String recover(Exception e, int id) {
        return "{\"error\" : \"API temporariamente indisponível. Tente novamente mais tarde.\", \"message\" : \"" + e.getMessage() + "\"}";
    }

    // Fallback caso o retry falhe para filtros
    @Recover
    public String recover(Exception e, String filter, int limit) {
        return "{\"error\" : \"API temporariamente indisponível. Tente novamente mais tarde.\", \"message\" : \"" + e.getMessage() + "\"}";
    }
}
