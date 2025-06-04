package com.portfolio.config;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
public class JikanRateLimit {

    private final Bucket bucket;

    // Limite de requisições da API da Jikan
    public JikanRateLimit() {
        // Configuração de limite
        // Primeira configuração
        Bandwidth firstLimit = Bandwidth.classic(
                3, // tokens (requisições) máximos
                Refill.intervally(3, Duration.ofSeconds(1)));

        // Limite para evitar sobrecarga (60/min)
        Bandwidth secondLimit = Bandwidth.classic(
                60, // tokens (requisições) máximos
                Refill.intervally(3, Duration.ofMinutes(1))); // recarga

        this.bucket = Bucket.builder()
                .addLimit(firstLimit)
                .addLimit(secondLimit)
                .build();

    }

    public boolean tryConsume() {
        return bucket.tryConsume(1); // Consome um token
    }


    // Métoddo com timeout
    public boolean tryConsume(long timeoutMs) {
        try {
            return bucket.asBlocking().tryConsume(1, Duration.ofMillis(timeoutMs));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}
