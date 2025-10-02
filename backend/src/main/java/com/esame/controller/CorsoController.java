package com.esame.controller;

import com.esame.model.Corso;
import com.esame.repository.CorsoRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Course Controller
 * Controller Corso
 * 
 * REST API endpoints for course management
 * Endpoint API REST per la gestione dei corsi
 */
@RestController
@RequestMapping("/courses")
@Tag(name = "Course Management", description = "API for managing courses / API per la gestione dei corsi")
@CrossOrigin(origins = "http://localhost:5173") // React Vite default port / Porta di default React Vite
public class CorsoController {
    
    @Autowired
    private CorsoRepository corsoRepository;
    
    /**
     * Get all available courses
     * Recupera tutti i corsi disponibili
     * 
     * @param titolo Optional course title filter / Filtro opzionale per titolo corso
     * @param luogo Optional location filter / Filtro opzionale per luogo
     * @param disponibili Optional filter for available courses only / Filtro opzionale per corsi disponibili
     * @return List of courses / Lista di corsi
     */
    @GetMapping
    @Operation(summary = "Get all courses", description = "Retrieve all available courses with optional filters / Recupera tutti i corsi disponibili con filtri opzionali")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Courses retrieved successfully / Corsi recuperati con successo"),
        @ApiResponse(responseCode = "500", description = "Internal server error / Errore interno del server")
    })
    public ResponseEntity<List<Corso>> getAllCourses(
            @Parameter(description = "Course title filter / Filtro per titolo corso") 
            @RequestParam(required = false) String titolo,
            
            @Parameter(description = "Location filter / Filtro per luogo") 
            @RequestParam(required = false) String luogo,
            
            @Parameter(description = "Show only available courses / Mostra solo corsi disponibili") 
            @RequestParam(required = false, defaultValue = "false") boolean disponibili) {
        
        try {
            List<Corso> corsi;
            
            if (titolo != null && !titolo.isEmpty()) {
                corsi = corsoRepository.findByTitoloContainingIgnoreCase(titolo);
            } else if (luogo != null && !luogo.isEmpty()) {
                corsi = corsoRepository.findByLuogoContainingIgnoreCase(luogo);
            } else if (disponibili) {
                corsi = corsoRepository.findAvailableCourses();
            } else {
                corsi = corsoRepository.findAll();
            }
            
            return ResponseEntity.ok(corsi);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
    
    /**
     * Get course by ID
     * Recupera corso per ID
     * 
     * @param id Course ID / ID del corso
     * @return Course details / Dettagli del corso
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get course by ID", description = "Retrieve a specific course by its ID / Recupera un corso specifico per il suo ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Course found / Corso trovato"),
        @ApiResponse(responseCode = "404", description = "Course not found / Corso non trovato"),
        @ApiResponse(responseCode = "500", description = "Internal server error / Errore interno del server")
    })
    public ResponseEntity<Corso> getCourseById(
            @Parameter(description = "Course ID / ID del corso") 
            @PathVariable Long id) {
        
        try {
            Optional<Corso> corso = corsoRepository.findById(id);
            return corso.map(ResponseEntity::ok)
                       .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
    
    /**
     * Get courses starting after a specific date
     * Recupera corsi che iniziano dopo una data specifica
     * 
     * @param dataInizio Start date filter / Filtro data di inizio
     * @return List of courses / Lista di corsi
     */
    @GetMapping("/upcoming")
    @Operation(summary = "Get upcoming courses", description = "Retrieve courses starting after a specific date / Recupera corsi che iniziano dopo una data specifica")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Upcoming courses retrieved / Corsi futuri recuperati"),
        @ApiResponse(responseCode = "500", description = "Internal server error / Errore interno del server")
    })
    public ResponseEntity<List<Corso>> getUpcomingCourses(
            @Parameter(description = "Start date filter (ISO format) / Filtro data di inizio (formato ISO)") 
            @RequestParam(required = false) String dataInizio) {
        
        try {
            LocalDateTime filterDate = dataInizio != null ? 
                LocalDateTime.parse(dataInizio) : 
                LocalDateTime.now();
            
            List<Corso> corsi = corsoRepository.findAvailableCoursesStartingAfter(filterDate);
            return ResponseEntity.ok(corsi);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
