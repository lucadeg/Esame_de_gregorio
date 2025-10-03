package com.esame.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * JSON Configuration
 * Configurazione JSON
 * 
 * Configures JSON serialization/deserialization
 * Configura serializzazione/deserializzazione JSON
 */
@Configuration
public class JsonConfig {
    
    /**
     * Object Mapper Bean
     * Bean Object Mapper
     * 
     * Configures JSON mapper with proper date handling
     * Configura mapper JSON con gestione date corretta
     */
    @Bean
    @Primary
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        
        // Register Java Time Module / Registra modulo Java Time
        mapper.registerModule(new JavaTimeModule());
        
        // Configure date/time handling / Configura gestione date/ora
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        mapper.disable(DeserializationFeature.FAIL_ON_INVALID_SUBTYPE);
        
        // Configure LocalDateTime handling / Configura gestione LocalDateTime
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        
        return mapper;
    }
}
