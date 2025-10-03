package com.esame.controller;

import com.esame.model.Corso;
import com.esame.repository.CorsoRepository;
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
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;

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
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:3000", "http://127.0.0.1:5173"}) // React Vite default port / Porta di default React Vite
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
    public ResponseEntity<?> getAllCourses(
            @Parameter(description = "Course title filter / Filtro per titolo corso") 
            @RequestParam(required = false) String titolo,
            
            @Parameter(description = "Location filter / Filtro per luogo") 
            @RequestParam(required = false) String luogo,
            
            @Parameter(description = "Teacher filter / Filtro per docente") 
            @RequestParam(required = false) String docente,
            
            @Parameter(description = "Category filter / Filtro per categoria") 
            @RequestParam(required = false) String categoria,
            
            @Parameter(description = "Level filter / Filtro per livello") 
            @RequestParam(required = false) String livello,
            
            @Parameter(description = "Start date filter (YYYY-MM-DD) / Filtro data inizio (YYYY-MM-DD)") 
            @RequestParam(required = false) String dataInizio,
            
            @Parameter(description = "End date filter (YYYY-MM-DD) / Filtro data fine (YYYY-MM-DD)") 
            @RequestParam(required = false) String dataFine,
            
            @Parameter(description = "Minimum duration in hours / Durata minima in ore") 
            @RequestParam(required = false) Integer durataMin,
            
            @Parameter(description = "Maximum duration in hours / Durata massima in ore") 
            @RequestParam(required = false) Integer durataMax,
            
            @Parameter(description = "Minimum price in euros / Prezzo minimo in euro") 
            @RequestParam(required = false) Double prezzoMin,
            
            @Parameter(description = "Maximum price in euros / Prezzo massimo in euro") 
            @RequestParam(required = false) Double prezzoMax,
            
            @Parameter(description = "Show only available courses / Mostra solo corsi disponibili") 
            @RequestParam(required = false, defaultValue = "false") boolean disponibili) {
        
        try {
            List<Corso> corsi = corsoRepository.findAll();
            int initialCount = corsi.size();
            
            // Apply filters with validation / Applica filtri con validazione
            if (titolo != null && !titolo.trim().isEmpty()) {
                corsi = corsi.stream()
                    .filter(c -> c.getTitolo() != null && c.getTitolo().toLowerCase().contains(titolo.toLowerCase().trim()))
                    .collect(Collectors.toList());
            }
            
            if (luogo != null && !luogo.trim().isEmpty()) {
                corsi = corsi.stream()
                    .filter(c -> c.getLuogo() != null && c.getLuogo().toLowerCase().contains(luogo.toLowerCase().trim()))
                    .collect(Collectors.toList());
            }
            
            if (docente != null && !docente.trim().isEmpty()) {
                corsi = corsi.stream()
                    .filter(c -> c.getDocenti() != null && c.getDocenti().toLowerCase().contains(docente.toLowerCase().trim()))
                    .collect(Collectors.toList());
            }
            
            if (categoria != null && !categoria.trim().isEmpty()) {
                corsi = corsi.stream()
                    .filter(c -> c.getCategoria() != null && c.getCategoria().toLowerCase().contains(categoria.toLowerCase().trim()))
                    .collect(Collectors.toList());
            }
            
            if (livello != null && !livello.trim().isEmpty()) {
                corsi = corsi.stream()
                    .filter(c -> c.getLivello() != null && c.getLivello().toLowerCase().contains(livello.toLowerCase().trim()))
                    .collect(Collectors.toList());
            }
            
            if (dataInizio != null && !dataInizio.trim().isEmpty()) {
                try {
                    LocalDate startDate = LocalDate.parse(dataInizio.trim());
                    corsi = corsi.stream()
                        .filter(c -> c.getDataOraInizio() != null && 
                               (c.getDataOraInizio().toLocalDate().isAfter(startDate) || c.getDataOraInizio().toLocalDate().isEqual(startDate)))
                        .collect(Collectors.toList());
                } catch (DateTimeParseException e) {
                    // Return error for invalid date format
                    return ResponseEntity.badRequest().body("Formato data inizio non valido. Usa YYYY-MM-DD");
                }
            }
            
            if (dataFine != null && !dataFine.trim().isEmpty()) {
                try {
                    LocalDate endDate = LocalDate.parse(dataFine.trim());
                    corsi = corsi.stream()
                        .filter(c -> c.getDataOraInizio() != null && 
                               (c.getDataOraInizio().toLocalDate().isBefore(endDate) || c.getDataOraInizio().toLocalDate().isEqual(endDate)))
                        .collect(Collectors.toList());
                } catch (DateTimeParseException e) {
                    // Return error for invalid date format
                    return ResponseEntity.badRequest().body("Formato data fine non valido. Usa YYYY-MM-DD");
                }
            }
            
            if (durataMin != null && durataMin >= 0) {
                corsi = corsi.stream()
                    .filter(c -> c.getDurataOre() != null && c.getDurataOre() >= durataMin)
                    .collect(Collectors.toList());
            }
            
            if (durataMax != null && durataMax >= 0) {
                corsi = corsi.stream()
                    .filter(c -> c.getDurataOre() != null && c.getDurataOre() <= durataMax)
                    .collect(Collectors.toList());
            }
            
            // Price filters with validation / Filtri prezzo con validazione
            if (prezzoMin != null) {
                final Double finalPrezzoMin;
                if (prezzoMin < 0) {
                    // Skip filter if negative / Salta filtro se negativo
                    finalPrezzoMin = null;
                } else if (prezzoMin > 10000) {
                    finalPrezzoMin = 10000.0; // Cap at 10000 / Limita a 10000
                } else {
                    finalPrezzoMin = prezzoMin;
                }
                if (finalPrezzoMin != null) {
                    corsi = corsi.stream()
                        .filter(c -> c.getPrezzo() != null && c.getPrezzo() >= finalPrezzoMin)
                        .collect(Collectors.toList());
                }
            }
            
            if (prezzoMax != null) {
                final Double finalPrezzoMax;
                if (prezzoMax < 0) {
                    // Skip filter if negative / Salta filtro se negativo
                    finalPrezzoMax = null;
                } else if (prezzoMax > 10000) {
                    finalPrezzoMax = 10000.0; // Cap at 10000 / Limita a 10000
                } else {
                    finalPrezzoMax = prezzoMax;
                }
                if (finalPrezzoMax != null) {
                    corsi = corsi.stream()
                        .filter(c -> c.getPrezzo() != null && c.getPrezzo() <= finalPrezzoMax)
                        .collect(Collectors.toList());
                }
            }
            
            if (disponibili) {
                corsi = corsi.stream()
                    .filter(c -> c.getDisponibilita() != null && c.getDisponibilita() > 0)
                    .collect(Collectors.toList());
            }
            
            // Add metadata about search results / Aggiungi metadati sui risultati
            Map<String, Object> response = new HashMap<>();
            response.put("courses", corsi);
            response.put("totalFound", corsi.size());
            response.put("totalAvailable", initialCount);
            response.put("filtersApplied", getAppliedFilters(titolo, luogo, docente, categoria, livello, dataInizio, dataFine, durataMin, durataMax, prezzoMin, prezzoMax, disponibili));
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Errore interno del server durante la ricerca dei corsi");
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
    
    /**
     * Create new course
     * Crea nuovo corso
     * 
     * @param corso Course data / Dati del corso
     * @return Created course / Corso creato
     */
    @PostMapping
    @Operation(summary = "Create new course", description = "Create a new course / Crea un nuovo corso")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Course created successfully / Corso creato con successo"),
        @ApiResponse(responseCode = "400", description = "Invalid course data / Dati corso non validi"),
        @ApiResponse(responseCode = "500", description = "Internal server error / Errore interno del server")
    })
    public ResponseEntity<Corso> createCourse(
            @Parameter(description = "Course data / Dati del corso") 
            @Valid @RequestBody Corso corso) {
        
        try {
            Corso savedCorso = corsoRepository.save(corso);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedCorso);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
    
    /**
     * Helper method to get applied filters information
     * Metodo helper per ottenere informazioni sui filtri applicati
     */
    private Map<String, Object> getAppliedFilters(String titolo, String luogo, String docente, String categoria, 
                                                   String livello, String dataInizio, String dataFine, 
                                                   Integer durataMin, Integer durataMax, Double prezzoMin, 
                                                   Double prezzoMax, boolean disponibili) {
        Map<String, Object> appliedFilters = new HashMap<>();
        
        if (titolo != null && !titolo.trim().isEmpty()) {
            appliedFilters.put("titolo", titolo.trim());
        }
        if (luogo != null && !luogo.trim().isEmpty()) {
            appliedFilters.put("luogo", luogo.trim());
        }
        if (docente != null && !docente.trim().isEmpty()) {
            appliedFilters.put("docente", docente.trim());
        }
        if (categoria != null && !categoria.trim().isEmpty()) {
            appliedFilters.put("categoria", categoria.trim());
        }
        if (livello != null && !livello.trim().isEmpty()) {
            appliedFilters.put("livello", livello.trim());
        }
        if (dataInizio != null && !dataInizio.trim().isEmpty()) {
            appliedFilters.put("dataInizio", dataInizio.trim());
        }
        if (dataFine != null && !dataFine.trim().isEmpty()) {
            appliedFilters.put("dataFine", dataFine.trim());
        }
        if (durataMin != null && durataMin >= 0) {
            appliedFilters.put("durataMin", durataMin);
        }
        if (durataMax != null && durataMax >= 0) {
            appliedFilters.put("durataMax", durataMax);
        }
        if (prezzoMin != null && prezzoMin >= 0) {
            appliedFilters.put("prezzoMin", prezzoMin);
        }
        if (prezzoMax != null && prezzoMax >= 0) {
            appliedFilters.put("prezzoMax", prezzoMax);
        }
        if (disponibili) {
            appliedFilters.put("disponibili", true);
        }
        
        return appliedFilters;
    }
}