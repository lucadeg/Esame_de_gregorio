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
 * Configuration for Swagger UI documentation
 * Configurazione per documentazione Swagger UI
 */
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Course Management API")
                        .description("RESTful API for managing course enrollments and registrations / API RESTful per la gestione di iscrizioni e registrazioni ai corsi")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Course Management Team")
                                .email("support@coursemanagement.com")
                                .url("https://github.com/lucadeg/Esame_de_gregorio"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:8080")
                                .description("Development server / Server di sviluppo"),
                        new Server()
                                .url("https://api.coursemanagement.com")
                                .description("Production server / Server di produzione")
                ));
    }
}
