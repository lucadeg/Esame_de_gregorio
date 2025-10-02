package com.esame.controller;

import com.esame.model.Corso;
import com.esame.model.Iscrizione;
import com.esame.repository.CorsoRepository;
import com.esame.repository.IscrizioneRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;

/**
 * Enrollment Controller
 * Controller Iscrizione
 * 
 * REST API endpoints for enrollment management
 * Endpoint API REST per la gestione delle iscrizioni
 */
@RestController
@RequestMapping("/enrollments")
@Tag(name = "Enrollment Management", description = "API for managing course enrollments / API per la gestione delle iscrizioni ai corsi")
@CrossOrigin(origins = "http://localhost:5173") // React Vite default port / Porta di default React Vite
public class IscrizioneController {
    
    @Autowired
    private IscrizioneRepository iscrizioneRepository;
    
    @Autowired
    private CorsoRepository corsoRepository;
    
    /**
     * Get all enrollments
     * Recupera tutte le iscrizioni
     * 
     * @param corsoId Optional course ID filter / Filtro opzionale per ID corso
     * @return List of enrollments / Lista di iscrizioni
     */
    @GetMapping
    @Operation(summary = "Get all enrollments", description = "Retrieve all enrollments with optional course filter / Recupera tutte le iscrizioni con filtro opzionale per corso")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Enrollments retrieved successfully / Iscrizioni recuperate con successo"),
        @ApiResponse(responseCode = "500", description = "Internal server error / Errore interno del server")
    })
    public ResponseEntity<List<Iscrizione>> getAllEnrollments(
            @Parameter(description = "Course ID filter / Filtro per ID corso") 
            @RequestParam(required = false) Long corsoId) {
        
        try {
            List<Iscrizione> iscrizioni;
            
            if (corsoId != null) {
                iscrizioni = iscrizioneRepository.findByCorsoId(corsoId);
            } else {
                iscrizioni = iscrizioneRepository.findAll();
            }
            
            return ResponseEntity.ok(iscrizioni);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
    
    /**
     * Get enrollment by ID
     * Recupera iscrizione per ID
     * 
     * @param id Enrollment ID / ID dell'iscrizione
     * @return Enrollment details / Dettagli dell'iscrizione
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get enrollment by ID", description = "Retrieve a specific enrollment by its ID / Recupera un'iscrizione specifica per il suo ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Enrollment found / Iscrizione trovata"),
        @ApiResponse(responseCode = "404", description = "Enrollment not found / Iscrizione non trovata"),
        @ApiResponse(responseCode = "500", description = "Internal server error / Errore interno del server")
    })
    public ResponseEntity<Iscrizione> getEnrollmentById(
            @Parameter(description = "Enrollment ID / ID dell'iscrizione") 
            @PathVariable Long id) {
        
        try {
            Optional<Iscrizione> iscrizione = iscrizioneRepository.findById(id);
            return iscrizione.map(ResponseEntity::ok)
                            .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
    
    /**
     * Create new enrollment
     * Crea nuova iscrizione
     * 
     * @param iscrizione Enrollment data / Dati dell'iscrizione
     * @return Created enrollment / Iscrizione creata
     */
    @PostMapping
    @Operation(summary = "Create new enrollment", description = "Create a new course enrollment / Crea una nuova iscrizione al corso")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Enrollment created successfully / Iscrizione creata con successo"),
        @ApiResponse(responseCode = "400", description = "Invalid enrollment data / Dati iscrizione non validi"),
        @ApiResponse(responseCode = "404", description = "Course not found / Corso non trovato"),
        @ApiResponse(responseCode = "409", description = "Participant already enrolled / Partecipante già iscritto"),
        @ApiResponse(responseCode = "500", description = "Internal server error / Errore interno del server")
    })
    public ResponseEntity<?> createEnrollment(
            @Parameter(description = "Enrollment data / Dati dell'iscrizione") 
            @Valid @RequestBody Iscrizione iscrizione) {
        
        try {
            // Check if course exists / Controlla se il corso esiste
            Optional<Corso> corsoOpt = corsoRepository.findById(iscrizione.getCorsoId());
            if (corsoOpt.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            
            Corso corso = corsoOpt.get();
            
            // Check if course has availability / Controlla se il corso ha disponibilità
            if (corso.getDisponibilita() <= 0) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Course is full / Il corso è completo");
            }
            
            // Check if participant is already enrolled / Controlla se il partecipante è già iscritto
            if (iscrizioneRepository.existsByCorsoIdAndPartecipanteEmail(
                iscrizione.getCorsoId(), iscrizione.getPartecipanteEmail())) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Participant already enrolled in this course / Partecipante già iscritto a questo corso");
            }
            
            // Create enrollment / Crea iscrizione
            Iscrizione savedIscrizione = iscrizioneRepository.save(iscrizione);
            
            return ResponseEntity.status(HttpStatus.CREATED).body(savedIscrizione);
            
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
    
    /**
     * Get enrollments for a specific course
     * Recupera iscrizioni per un corso specifico
     * 
     * @param corsoId Course ID / ID del corso
     * @return List of enrollments for the course / Lista di iscrizioni per il corso
     */
    @GetMapping("/course/{corsoId}")
    @Operation(summary = "Get enrollments by course", description = "Retrieve all enrollments for a specific course / Recupera tutte le iscrizioni per un corso specifico")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Enrollments retrieved / Iscrizioni recuperate"),
        @ApiResponse(responseCode = "404", description = "Course not found / Corso non trovato"),
        @ApiResponse(responseCode = "500", description = "Internal server error / Errore interno del server")
    })
    public ResponseEntity<?> getEnrollmentsByCourse(
            @Parameter(description = "Course ID / ID del corso") 
            @PathVariable Long corsoId) {
        
        try {
            // Check if course exists / Controlla se il corso esiste
            if (!corsoRepository.existsById(corsoId)) {
                return ResponseEntity.notFound().build();
            }
            
            List<Iscrizione> iscrizioni = iscrizioneRepository.findByCorsoId(corsoId);
            return ResponseEntity.ok(iscrizioni);
            
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
