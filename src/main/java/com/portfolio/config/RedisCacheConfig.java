package com.portfolio.config;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableCaching
public class RedisCacheConfig {
    public CacheManager cacheManager(RedisConnectionFactory connectionFactory) {
        // Configuração default
        RedisCacheConfiguration defaultConfig = RedisCacheConfiguration
                .defaultCacheConfig()
                .entryTtl(Duration.ofHours(2))
                .serializeValuesWith(RedisSerializationContext.SerializationPair
                        .fromSerializer(new GenericJackson2JsonRedisSerializer()));

        // Configuração específica por cache
        Map<String, RedisCacheConfiguration> cacheConfigurationMap = new HashMap<>();

        // COnfigurações expecíficas para cada tema (filtro -> TTL médio, ID -> TTL longo)
        cacheConfigurationMap.put("anime", defaultConfig.entryTtl(Duration.ofHours(24)));
        cacheConfigurationMap.put("manga", defaultConfig.entryTtl(Duration.ofHours(24)));
        cacheConfigurationMap.put("anime-filter", defaultConfig.entryTtl(Duration.ofHours(12)));
        cacheConfigurationMap.put("manga-filter", defaultConfig.entryTtl(Duration.ofHours(12)));
        cacheConfigurationMap.put("game", defaultConfig.entryTtl(Duration.ofHours(24)));
        cacheConfigurationMap.put("game-filter", defaultConfig.entryTtl(Duration.ofHours(12)));

        return RedisCacheManager.builder(connectionFactory)
                .cacheDefaults(defaultConfig)
                .withInitialCacheConfigurations(cacheConfigurationMap)
                .build();
    }
}
