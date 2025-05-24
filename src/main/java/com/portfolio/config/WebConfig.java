package com.portfolio.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry reg) {
        reg.addMapping("/**")
                .allowedHeaders("http://localhost:5173/", "http://localhost:4173/") // Adicionar o localhost e o site da Vercel posteriormente
                .allowedMethods("GET", "POST", "PUT", "DELETE")
                .allowedOrigins("*");
    }
}
