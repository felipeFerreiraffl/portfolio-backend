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
        Bandwidth limit = Bandwidth.classic(
                3, // tokens (requisições) máximos
                Refill.intervally(3, Duration.ofSeconds(1))); // recarga

        this.bucket = Bucket.builder().addLimit(limit).build();

    }

    public boolean tryConsume() {
        return bucket.tryConsume(1); // Consome um token
    }


}
