package com.esame;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main Application Class
 * Classe Principale dell'Applicazione
 * 
 * Spring Boot application entry point for Course Management System
 * Punto di ingresso dell'applicazione Spring Boot per il Sistema di Gestione Corsi
 */
@SpringBootApplication
public class CourseManagementApplication {

    public static void main(String[] args) {
        SpringApplication.run(CourseManagementApplication.class, args);
    }
}
