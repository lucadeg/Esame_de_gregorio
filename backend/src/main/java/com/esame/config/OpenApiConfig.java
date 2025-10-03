package com.esame.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * OpenAPI Configuration
 * Configurazione OpenAPI
 * 
 * Configures Swagger UI and OpenAPI documentation
 * Configura Swagger UI e documentazione OpenAPI
 */
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Course Management API")
                        .description("RESTful API for Course Management System / API RESTful per Sistema di Gestione Corsi")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Course Management Team")
                                .email("support@coursemanagement.com")
                                .url("https://coursemanagement.com"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:8080")
                                .description("Development Server / Server di Sviluppo"),
                        new Server()
                                .url("https://api.coursemanagement.com")
                                .description("Production Server / Server di Produzione")
                ));
    }
}