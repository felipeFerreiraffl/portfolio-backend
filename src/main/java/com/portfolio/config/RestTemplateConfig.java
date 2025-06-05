package com.portfolio.config;

import org.springframework.context.annotation.Bean;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.time.Duration;

public class RestTemplateConfig {

    @Bean
    public RestTemplate restTemplate() {

        // COnfiguração de timeout
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout((int) Duration.ofSeconds(10).toMillis());
        factory.setReadTimeout((int) Duration.ofSeconds(15).toMillis());

        RestTemplate restTemplate = new RestTemplate();

        // COnfiguração para UTF-8
        restTemplate.getMessageConverters()
                .stream()
                .filter(converter -> converter instanceof StringHttpMessageConverter)
                .forEach(converter -> ((StringHttpMessageConverter) converter)
                        .setDefaultCharset(StandardCharsets.UTF_8));

        return restTemplate;
    }
}
