package com.portfolio;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PortfolioApplication {

    public static void main(String[] args) {
        // Configuração do arquivo .env
        Dotenv dotenv = Dotenv.load();
        System.setProperty("spring.data.mongodb.uri", dotenv.get("MONGODB_URI"));
        System.setProperty("api.rawg.key", dotenv.get("RAWG_KEY"));

        SpringApplication.run(PortfolioApplication.class, args);
    }

}
