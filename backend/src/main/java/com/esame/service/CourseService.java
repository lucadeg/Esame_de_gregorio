package com.esame.service;

import com.esame.dto.CourseDTO;
import com.esame.dto.PagedResponse;
import com.esame.exception.BusinessLogicException;
import com.esame.exception.ResourceNotFoundException;
import com.esame.mapper.CourseMapper;
import com.esame.model.Corso;
import com.esame.repository.CorsoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Course Service
 * Servizio Corso
 * 
 * Business logic for course management
 * Logica di business per la gestione dei corsi
 */
@Service
@Transactional
public class CourseService {
    
    @Autowired
    private CorsoRepository corsoRepository;
    
    @Autowired
    private CourseMapper courseMapper;
    
    /**
     * Get all courses with optional filtering and pagination
     * Recupera tutti i corsi con filtri opzionali e paginazione
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
    public PagedResponse<CourseDTO> getAllCourses(
            int page, int size, String sortBy, String sortDir,
            String titolo, String luogo, String docente, String categoria, 
            Boolean disponibili) {
        
        // Create sort object / Crea oggetto ordinamento
        Sort sort = sortDir.equalsIgnoreCase("desc") ? 
            Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        
        // Create pageable object / Crea oggetto paginabile
        Pageable pageable = PageRequest.of(page, size, sort);
        
        // Get courses from repository / Recupera corsi dal repository
        Page<Corso> coursePage = corsoRepository.findAll(pageable);
        
        // Convert to DTOs / Converte in DTO
        List<CourseDTO> courseDTOs = courseMapper.toDTOList(coursePage.getContent());
        
        // Create paging info / Crea informazioni paginazione
        PagedResponse.PagingInfo pagingInfo = new PagedResponse.PagingInfo(
            page, size, coursePage.getTotalElements()
        );
        
        return PagedResponse.success(courseDTOs, pagingInfo);
    }
    
    /**
     * Get course by ID
     * Recupera corso per ID
     * 
     * @param id Course ID / ID del corso
     * @return Course DTO / DTO Corso
     * @throws ResourceNotFoundException if course not found / se corso non trovato
     */
    public CourseDTO getCourseById(Long id) {
        Corso corso = corsoRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Course", "id", id));
        
        return courseMapper.toDTO(corso);
    }
    
    /**
     * Create new course
     * Crea nuovo corso
     * 
     * @param courseDTO Course data / Dati del corso
     * @return Created course DTO / DTO corso creato
     * @throws BusinessLogicException if validation fails / se validazione fallisce
     */
    public CourseDTO createCourse(CourseDTO courseDTO) {
        // Validate course data / Valida dati corso
        validateCourseData(courseDTO);
        
        // Check for duplicate title and date / Controlla titolo e data duplicati
        if (corsoRepository.existsByTitoloAndDataOraInizio(
            courseDTO.getTitolo(), courseDTO.getDataOraInizio())) {
            throw new BusinessLogicException(
                "DUPLICATE_COURSE", 
                "Course with same title and date already exists / Corso con stesso titolo e data già esistente"
            );
        }
        
        // Convert DTO to entity / Converte DTO in entità
        Corso corso = courseMapper.toEntity(courseDTO);
        
        // Save course / Salva corso
        Corso savedCorso = corsoRepository.save(corso);
        
        return courseMapper.toDTO(savedCorso);
    }
    
    /**
     * Update existing course
     * Aggiorna corso esistente
     * 
     * @param id Course ID / ID del corso
     * @param courseDTO Updated course data / Dati corso aggiornati
     * @return Updated course DTO / DTO corso aggiornato
     * @throws ResourceNotFoundException if course not found / se corso non trovato
     * @throws BusinessLogicException if validation fails / se validazione fallisce
     */
    public CourseDTO updateCourse(Long id, CourseDTO courseDTO) {
        // Find existing course / Trova corso esistente
        Corso existingCorso = corsoRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Course", "id", id));
        
        // Validate course data / Valida dati corso
        validateCourseData(courseDTO);
        
        // Check for duplicate title and date (excluding current course) / Controlla titolo e data duplicati (escludendo corso corrente)
        if (corsoRepository.existsByTitoloAndDataOraInizioAndCorsoIdNot(
            courseDTO.getTitolo(), courseDTO.getDataOraInizio(), id)) {
            throw new BusinessLogicException(
                "DUPLICATE_COURSE", 
                "Course with same title and date already exists / Corso con stesso titolo e data già esistente"
            );
        }
        
        // Update course / Aggiorna corso
        Corso updatedCorso = courseMapper.updateEntity(existingCorso, courseDTO);
        
        // Save updated course / Salva corso aggiornato
        Corso savedCorso = corsoRepository.save(updatedCorso);
        
        return courseMapper.toDTO(savedCorso);
    }
    
    /**
     * Delete course by ID
     * Elimina corso per ID
     * 
     * @param id Course ID / ID del corso
     * @throws ResourceNotFoundException if course not found / se corso non trovato
     * @throws BusinessLogicException if course has enrollments / se corso ha iscrizioni
     */
    public void deleteCourse(Long id) {
        // Find existing course / Trova corso esistente
        Corso corso = corsoRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Course", "id", id));
        
        // Check if course has enrollments / Controlla se corso ha iscrizioni
        if (corso.getIscrizioni() != null && !corso.getIscrizioni().isEmpty()) {
            throw new BusinessLogicException(
                "COURSE_HAS_ENROLLMENTS", 
                "Cannot delete course with existing enrollments / Impossibile eliminare corso con iscrizioni esistenti"
            );
        }
        
        // Delete course / Elimina corso
        corsoRepository.deleteById(id);
    }
    
    /**
     * Get upcoming courses
     * Recupera corsi futuri
     * 
     * @param startDate Start date filter / Filtro data inizio
     * @return List of upcoming courses / Lista corsi futuri
     */
    public List<CourseDTO> getUpcomingCourses(LocalDateTime startDate) {
        LocalDateTime filterDate = startDate != null ? startDate : LocalDateTime.now();
        
        List<Corso> corsi = corsoRepository.findAvailableCoursesStartingAfter(filterDate);
        
        return courseMapper.toDTOList(corsi);
    }
    
    /**
     * Get courses by category
     * Recupera corsi per categoria
     * 
     * @param categoria Category / Categoria
     * @return List of courses / Lista corsi
     */
    public List<CourseDTO> getCoursesByCategory(String categoria) {
        List<Corso> corsi = corsoRepository.findByCategoria(categoria);
        
        return courseMapper.toDTOList(corsi);
    }
    
    /**
     * Get available courses
     * Recupera corsi disponibili
     * 
     * @return List of available courses / Lista corsi disponibili
     */
    public List<CourseDTO> getAvailableCourses() {
        List<Corso> corsi = corsoRepository.findByDisponibilitaGreaterThan(0);
        
        return courseMapper.toDTOList(corsi);
    }
    
    /**
     * Validate course data
     * Valida dati corso
     * 
     * @param courseDTO Course DTO / DTO Corso
     * @throws BusinessLogicException if validation fails / se validazione fallisce
     */
    private void validateCourseData(CourseDTO courseDTO) {
        // Check if start date is in the future / Controlla se data inizio è nel futuro
        if (courseDTO.getDataOraInizio() != null && 
            courseDTO.getDataOraInizio().isBefore(LocalDateTime.now())) {
            throw new BusinessLogicException(
                "INVALID_START_DATE", 
                "Course start date must be in the future / La data di inizio del corso deve essere nel futuro"
            );
        }
        
        // Check if availability is positive / Controlla se disponibilità è positiva
        if (courseDTO.getDisponibilita() != null && courseDTO.getDisponibilita() < 0) {
            throw new BusinessLogicException(
                "INVALID_AVAILABILITY", 
                "Course availability cannot be negative / La disponibilità del corso non può essere negativa"
            );
        }
        
        // Check if price is positive / Controlla se prezzo è positivo
        if (courseDTO.getPrezzo() != null && courseDTO.getPrezzo() < 0) {
            throw new BusinessLogicException(
                "INVALID_PRICE", 
                "Course price cannot be negative / Il prezzo del corso non può essere negativo"
            );
        }
        
        // Check if duration is positive / Controlla se durata è positiva
        if (courseDTO.getDurataOre() != null && courseDTO.getDurataOre() <= 0) {
            throw new BusinessLogicException(
                "INVALID_DURATION", 
                "Course duration must be positive / La durata del corso deve essere positiva"
            );
        }
    }
}
