package com.esame.controller;

import com.esame.dto.ApiResponse;
import com.esame.dto.CourseDTO;
import com.esame.dto.PagedResponse;
import com.esame.service.CourseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

/**
 * Enhanced Course Controller
 * Controller Corso Potenziato
 * 
 * REST API endpoints for course management with DTOs and enhanced features
 * Endpoint API REST per la gestione dei corsi con DTO e funzionalità potenziate
 */
@RestController
@RequestMapping("/api/v1/courses")
@Tag(name = "Enhanced Course Management", description = "Enhanced API for managing courses / API potenziata per la gestione dei corsi")
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:3000", "http://127.0.0.1:5173"})
public class EnhancedCourseController {
    
    @Autowired
    private CourseService courseService;
    
    /**
     * Get all courses with pagination and filtering
     * Recupera tutti i corsi con paginazione e filtri
     * 
     * @param page Page number / Numero pagina
     * @param size Page size / Dimensione pagina
     * @param sortBy Sort field / Campo ordinamento
     * @param sortDir Sort direction / Direzione ordinamento
     * @param titolo Course title filter / Filtro titolo corso
     * @param luogo Location filter / Filtro luogo
     * @param docente Teacher filter / Filtro docente
     * @param categoria Category filter / Filtro categoria
     * @param disponibili Available courses only / Solo corsi disponibili
     * @return Paged response with courses / Risposta paginata con corsi
     */
    @GetMapping
    @Operation(summary = "Get all courses with pagination", description = "Retrieve all courses with pagination and filtering / Recupera tutti i corsi con paginazione e filtri")
    public ResponseEntity<ApiResponse<PagedResponse<CourseDTO>>> getAllCourses(
            @Parameter(description = "Page number (0-based) / Numero pagina (basato su 0)")
            @RequestParam(defaultValue = "0") int page,
            
            @Parameter(description = "Page size / Dimensione pagina")
            @RequestParam(defaultValue = "10") int size,
            
            @Parameter(description = "Sort field / Campo ordinamento")
            @RequestParam(defaultValue = "titolo") String sortBy,
            
            @Parameter(description = "Sort direction (asc/desc) / Direzione ordinamento (asc/desc)")
            @RequestParam(defaultValue = "asc") String sortDir,
            
            @Parameter(description = "Course title filter / Filtro titolo corso")
            @RequestParam(required = false) String titolo,
            
            @Parameter(description = "Location filter / Filtro luogo")
            @RequestParam(required = false) String luogo,
            
            @Parameter(description = "Teacher filter / Filtro docente")
            @RequestParam(required = false) String docente,
            
            @Parameter(description = "Category filter / Filtro categoria")
            @RequestParam(required = false) String categoria,
            
            @Parameter(description = "Available courses only / Solo corsi disponibili")
            @RequestParam(required = false) Boolean disponibili) {
        
        try {
            PagedResponse<CourseDTO> response = courseService.getAllCourses(
                page, size, sortBy, sortDir, titolo, luogo, docente, categoria, disponibili);
            
            return ResponseEntity.ok(ApiResponse.success("Courses retrieved successfully / Corsi recuperati con successo", response));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("Error retrieving courses / Errore nel recupero dei corsi: " + e.getMessage()));
        }
    }
    
    /**
     * Get course by ID
     * Recupera corso per ID
     * 
     * @param id Course ID / ID del corso
     * @return Course DTO / DTO Corso
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get course by ID", description = "Retrieve a specific course by its ID / Recupera un corso specifico per il suo ID")
    public ResponseEntity<ApiResponse<CourseDTO>> getCourseById(
            @Parameter(description = "Course ID / ID del corso")
            @PathVariable Long id) {
        
        try {
            CourseDTO course = courseService.getCourseById(id);
            return ResponseEntity.ok(ApiResponse.success("Course retrieved successfully / Corso recuperato con successo", course));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.error("Course not found / Corso non trovato: " + e.getMessage()));
        }
    }
    
    /**
     * Create new course
     * Crea nuovo corso
     * 
     * @param courseDTO Course data / Dati del corso
     * @return Created course DTO / DTO corso creato
     */
    @PostMapping
    @Operation(summary = "Create new course", description = "Create a new course / Crea un nuovo corso")
    public ResponseEntity<ApiResponse<CourseDTO>> createCourse(
            @Parameter(description = "Course data / Dati del corso")
            @Valid @RequestBody CourseDTO courseDTO) {
        
        try {
            CourseDTO createdCourse = courseService.createCourse(courseDTO);
            return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Course created successfully / Corso creato con successo", createdCourse));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error("Error creating course / Errore nella creazione del corso: " + e.getMessage()));
        }
    }
    
    /**
     * Update existing course
     * Aggiorna corso esistente
     * 
     * @param id Course ID / ID del corso
     * @param courseDTO Updated course data / Dati corso aggiornati
     * @return Updated course DTO / DTO corso aggiornato
     */
    @PutMapping("/{id}")
    @Operation(summary = "Update course", description = "Update an existing course / Aggiorna un corso esistente")
    public ResponseEntity<ApiResponse<CourseDTO>> updateCourse(
            @Parameter(description = "Course ID / ID del corso")
            @PathVariable Long id,
            @Parameter(description = "Updated course data / Dati corso aggiornati")
            @Valid @RequestBody CourseDTO courseDTO) {
        
        try {
            CourseDTO updatedCourse = courseService.updateCourse(id, courseDTO);
            return ResponseEntity.ok(ApiResponse.success("Course updated successfully / Corso aggiornato con successo", updatedCourse));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error("Error updating course / Errore nell'aggiornamento del corso: " + e.getMessage()));
        }
    }
    
    /**
     * Delete course
     * Elimina corso
     * 
     * @param id Course ID / ID del corso
     * @return Success message / Messaggio di successo
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete course", description = "Delete a course / Elimina un corso")
    public ResponseEntity<ApiResponse<String>> deleteCourse(
            @Parameter(description = "Course ID / ID del corso")
            @PathVariable Long id) {
        
        try {
            courseService.deleteCourse(id);
            return ResponseEntity.ok(ApiResponse.success("Course deleted successfully / Corso eliminato con successo", "Course with ID " + id + " has been deleted / Corso con ID " + id + " è stato eliminato"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error("Error deleting course / Errore nell'eliminazione del corso: " + e.getMessage()));
        }
    }
    
    /**
     * Get upcoming courses
     * Recupera corsi futuri
     * 
     * @return List of upcoming courses / Lista corsi futuri
     */
    @GetMapping("/upcoming")
    @Operation(summary = "Get upcoming courses", description = "Retrieve upcoming courses / Recupera corsi futuri")
    public ResponseEntity<ApiResponse<List<CourseDTO>>> getUpcomingCourses() {
        try {
            List<CourseDTO> courses = courseService.getUpcomingCourses(null);
            return ResponseEntity.ok(ApiResponse.success("Upcoming courses retrieved successfully / Corsi futuri recuperati con successo", courses));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("Error retrieving upcoming courses / Errore nel recupero dei corsi futuri: " + e.getMessage()));
        }
    }
    
    /**
     * Get courses by category
     * Recupera corsi per categoria
     * 
     * @param categoria Category / Categoria
     * @return List of courses / Lista corsi
     */
    @GetMapping("/category/{categoria}")
    @Operation(summary = "Get courses by category", description = "Retrieve courses by category / Recupera corsi per categoria")
    public ResponseEntity<ApiResponse<List<CourseDTO>>> getCoursesByCategory(
            @Parameter(description = "Category / Categoria")
            @PathVariable String categoria) {
        
        try {
            List<CourseDTO> courses = courseService.getCoursesByCategory(categoria);
            return ResponseEntity.ok(ApiResponse.success("Courses retrieved successfully / Corsi recuperati con successo", courses));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("Error retrieving courses by category / Errore nel recupero dei corsi per categoria: " + e.getMessage()));
        }
    }
    
    /**
     * Get available courses
     * Recupera corsi disponibili
     * 
     * @return List of available courses / Lista corsi disponibili
     */
    @GetMapping("/available")
    @Operation(summary = "Get available courses", description = "Retrieve available courses / Recupera corsi disponibili")
    public ResponseEntity<ApiResponse<List<CourseDTO>>> getAvailableCourses() {
        try {
            List<CourseDTO> courses = courseService.getAvailableCourses();
            return ResponseEntity.ok(ApiResponse.success("Available courses retrieved successfully / Corsi disponibili recuperati con successo", courses));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("Error retrieving available courses / Errore nel recupero dei corsi disponibili: " + e.getMessage()));
        }
    }
}
