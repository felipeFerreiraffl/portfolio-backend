package com.portfolio.service;

import com.portfolio.config.JikanRateLimit;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    // Verifica se um JSON é válido
    private boolean isValidJson(String jsonString) {
        if (jsonString == null || jsonString.trim().isEmpty()) {
            return false;
        }

        try {
            jsonString = jsonString.trim();
            return (jsonString.startsWith("{") && jsonString.endsWith("}") ||
                    jsonString.startsWith("[") && jsonString.endsWith("]"));
        } catch (Exception e) {
            return false;
        }
    }

    private String makeJikanRequest(URI uri, String context) {
        try {
            ResponseEntity<String> response = restTemplate.getForEntity(uri, String.class);
            String responseBody = response.getBody();

            if (!isValidJson(responseBody)) {
                throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "Resposta inválida da API externa");
            }

            return responseBody;
        } catch (HttpClientErrorException.TooManyRequests e) { // Erro 429
            throw new ResponseStatusException(HttpStatus.TOO_MANY_REQUESTS, "API externa com rate limit. Aguarde alguns segundos.");
        } catch (HttpClientErrorException.NotFound e) { // Erro 404
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, context + " não foi encontrado.");
        } catch (HttpServerErrorException e) { // Erro 503
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "Erro na API externa. Tente novamente mais tarde.");
        } catch (RestClientException e) { // Erro 500
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Erro interno ao buscar anime: " + e.getMessage());
        }
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

        return makeJikanRequest(uri, "Anime com ID " + id);
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


        return makeJikanRequest(uri, "Anime pelo filtro " + filter);
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

        return makeJikanRequest(uri, "Mangá com ID " + id);
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

        return makeJikanRequest(uri, "Mangás pelo filtro " + filter);
    }

    // Fallback caso o retry falhe para IDs
    @Recover
    public String recover(Exception e, int id) {
        String errorMessage = "API temporariamente indisponível. Tente novamente mais tarde.";
        return String.format("{\"error\": \"%s\", \"message\": \"%s\", \"id\": %d}",
                errorMessage, e.getMessage().replace("\"", "\\\""), id);
    }

    // Fallback caso o retry falhe para filtros
    @Recover
    public String recover(Exception e, String filter, int limit) {
        String errorMessage = "API temporariamente indisponível. Tente novamente mais tarde.";
        return String.format("{\"error\": \"%s\", \"message\": \"%s\", \"filter\": \"%s\", \"limit\": %d}",
                errorMessage, e.getMessage().replace("\"", "\\\""), filter, limit);
    }
}
