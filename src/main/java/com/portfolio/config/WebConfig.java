package com.portfolio.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry reg) {
        reg.addMapping("/**")
                .allowedHeaders("*") // Adicionar o localhost e o site da Vercel posteriormente
                .allowedMethods("GET", "POST", "PUT", "DELETE")
                .allowedOrigins("https://portfolio-lyart-rho-77.vercel.app/")
                .allowCredentials(false)
                .exposedHeaders("Content-Type", "Cache-Control")
                .maxAge(3600);
    }
}
