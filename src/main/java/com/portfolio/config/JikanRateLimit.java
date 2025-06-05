package com.portfolio.config;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.concurrent.locks.ReentrantLock;

@Component
public class JikanRateLimit {

    private final Bucket bucket;
    private final ReentrantLock lock = new ReentrantLock();
    private long lastRequestTime = 0;
    private static final long MIN_INTERVAL_MS = 1000;

    // Limite de requisições da API da Jikan
    public JikanRateLimit() {
        // Configuração de limite
        // Primeira configuração
        Bandwidth firstLimit = Bandwidth.classic(
                1, // tokens (requisições) máximos
                Refill.intervally(1, Duration.ofSeconds(1)));

        // Limite para evitar sobrecarga (60/min)
        Bandwidth secondLimit = Bandwidth.classic(
                30, // tokens (requisições) máximos
                Refill.intervally(30, Duration.ofMinutes(1))); // recarga

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
        lock.lock();
        try {
            // Intervalo mínimo entre requisições
            long currentTime = System.currentTimeMillis();
            long timeSinceLastReq = currentTime - lastRequestTime;

            if (timeSinceLastReq < MIN_INTERVAL_MS) {
                long sleepTime = MIN_INTERVAL_MS - timeSinceLastReq;

                try {
                    Thread.sleep(sleepTime);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return false;
                }
            }

            boolean consumed = bucket.asBlocking().tryConsume(1, Duration.ofMillis(timeoutMs));

            return consumed;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return false;
        } finally {
            lock.unlock();
        }
    }

    // Verifica quantos tokens tem dispovíneis
    public long getAvailableTokens() {
        return bucket.getAvailableTokens();
    }

    // Verifica se pode fazer uma requisição sem consumir um token
    public boolean canConsume() {
        return bucket.getAvailableTokens() > 0;
    }

    // Esperar até que um token esteja disponível
    public void waitForToken() throws InterruptedException {
        while (!canConsume()) {
            Thread.sleep(100);
        }
    }
}
