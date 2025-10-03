package com.esame.service;

import com.esame.dto.EnrollmentDTO;
import com.esame.exception.BusinessLogicException;
import com.esame.exception.ResourceNotFoundException;
import com.esame.mapper.EnrollmentMapper;
import com.esame.model.Corso;
import com.esame.model.Iscrizione;
import com.esame.repository.CorsoRepository;
import com.esame.repository.IscrizioneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Enrollment Service
 * Servizio Iscrizione
 * 
 * Business logic for enrollment management
 * Logica di business per la gestione delle iscrizioni
 */
@Service
@Transactional
public class EnrollmentService {
    
    @Autowired
    private IscrizioneRepository iscrizioneRepository;
    
    @Autowired
    private CorsoRepository corsoRepository;
    
    @Autowired
    private EnrollmentMapper enrollmentMapper;
    
    /**
     * Get all enrollments
     * Recupera tutte le iscrizioni
     * 
     * @param corsoId Optional course ID filter / Filtro opzionale per ID corso
     * @return List of enrollment DTOs / Lista DTO iscrizioni
     */
    public List<EnrollmentDTO> getAllEnrollments(Long corsoId) {
        List<Iscrizione> iscrizioni;
        
        if (corsoId != null) {
            iscrizioni = iscrizioneRepository.findByCorsoId(corsoId);
        } else {
            iscrizioni = iscrizioneRepository.findAll();
        }
        
        return enrollmentMapper.toDTOList(iscrizioni);
    }
    
    /**
     * Get enrollment by ID
     * Recupera iscrizione per ID
     * 
     * @param id Enrollment ID / ID dell'iscrizione
     * @return Enrollment DTO / DTO Iscrizione
     * @throws ResourceNotFoundException if enrollment not found / se iscrizione non trovata
     */
    public EnrollmentDTO getEnrollmentById(Long id) {
        Iscrizione iscrizione = iscrizioneRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Enrollment", "id", id));
        
        return enrollmentMapper.toDTO(iscrizione);
    }
    
    /**
     * Create new enrollment
     * Crea nuova iscrizione
     * 
     * @param enrollmentDTO Enrollment data / Dati dell'iscrizione
     * @return Created enrollment DTO / DTO iscrizione creata
     * @throws ResourceNotFoundException if course not found / se corso non trovato
     * @throws BusinessLogicException if enrollment not possible / se iscrizione non possibile
     */
    public EnrollmentDTO createEnrollment(EnrollmentDTO enrollmentDTO) {
        // Validate enrollment data / Valida dati iscrizione
        validateEnrollmentData(enrollmentDTO);
        
        // Get course / Recupera corso
        Corso corso = corsoRepository.findById(enrollmentDTO.getCorsoId())
            .orElseThrow(() -> new ResourceNotFoundException("Course", "id", enrollmentDTO.getCorsoId()));
        
        // Check course availability / Controlla disponibilità corso
        if (corso.getDisponibilita() <= 0) {
            throw new BusinessLogicException(
                "COURSE_FULL", 
                "Course is full / Il corso è completo"
            );
        }
        
        // Check if participant is already enrolled / Controlla se partecipante è già iscritto
        if (iscrizioneRepository.existsByCorsoIdAndPartecipanteEmail(
            enrollmentDTO.getCorsoId(), enrollmentDTO.getPartecipanteEmail())) {
            throw new BusinessLogicException(
                "ALREADY_ENROLLED", 
                "Participant is already enrolled in this course / Il partecipante è già iscritto a questo corso"
            );
        }
        
        // Check if course has started / Controlla se corso è iniziato
        if (corso.getDataOraInizio() != null && corso.getDataOraInizio().isBefore(LocalDateTime.now())) {
            throw new BusinessLogicException(
                "COURSE_STARTED", 
                "Cannot enroll in a course that has already started / Impossibile iscriversi a un corso già iniziato"
            );
        }
        
        // Create enrollment / Crea iscrizione
        Iscrizione iscrizione = enrollmentMapper.toEntity(enrollmentDTO);
        iscrizione.setCorso(corso);
        iscrizione.setDataOraIscrizione(LocalDateTime.now());
        
        // Save enrollment / Salva iscrizione
        Iscrizione savedIscrizione = iscrizioneRepository.save(iscrizione);
        
        // Update course availability / Aggiorna disponibilità corso
        corso.setDisponibilita(corso.getDisponibilita() - 1);
        corsoRepository.save(corso);
        
        return enrollmentMapper.toDTO(savedIscrizione);
    }
    
    /**
     * Update enrollment
     * Aggiorna iscrizione
     * 
     * @param id Enrollment ID / ID dell'iscrizione
     * @param enrollmentDTO Updated enrollment data / Dati iscrizione aggiornati
     * @return Updated enrollment DTO / DTO iscrizione aggiornata
     * @throws ResourceNotFoundException if enrollment not found / se iscrizione non trovata
     * @throws BusinessLogicException if update not allowed / se aggiornamento non consentito
     */
    public EnrollmentDTO updateEnrollment(Long id, EnrollmentDTO enrollmentDTO) {
        // Find existing enrollment / Trova iscrizione esistente
        Iscrizione existingIscrizione = iscrizioneRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Enrollment", "id", id));
        
        // Check if course has started / Controlla se corso è iniziato
        if (existingIscrizione.getCorso() != null && 
            existingIscrizione.getCorso().getDataOraInizio() != null && 
            existingIscrizione.getCorso().getDataOraInizio().isBefore(LocalDateTime.now())) {
            throw new BusinessLogicException(
                "COURSE_STARTED", 
                "Cannot update enrollment for a course that has already started / Impossibile aggiornare iscrizione per un corso già iniziato"
            );
        }
        
        // Update enrollment / Aggiorna iscrizione
        Iscrizione updatedIscrizione = enrollmentMapper.updateEntity(existingIscrizione, enrollmentDTO);
        
        // Save updated enrollment / Salva iscrizione aggiornata
        Iscrizione savedIscrizione = iscrizioneRepository.save(updatedIscrizione);
        
        return enrollmentMapper.toDTO(savedIscrizione);
    }
    
    /**
     * Delete enrollment
     * Elimina iscrizione
     * 
     * @param id Enrollment ID / ID dell'iscrizione
     * @throws ResourceNotFoundException if enrollment not found / se iscrizione non trovata
     * @throws BusinessLogicException if deletion not allowed / se eliminazione non consentita
     */
    public void deleteEnrollment(Long id) {
        // Find existing enrollment / Trova iscrizione esistente
        Iscrizione iscrizione = iscrizioneRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Enrollment", "id", id));
        
        // Check if course has started / Controlla se corso è iniziato
        if (iscrizione.getCorso() != null && 
            iscrizione.getCorso().getDataOraInizio() != null && 
            iscrizione.getCorso().getDataOraInizio().isBefore(LocalDateTime.now())) {
            throw new BusinessLogicException(
                "COURSE_STARTED", 
                "Cannot delete enrollment for a course that has already started / Impossibile eliminare iscrizione per un corso già iniziato"
            );
        }
        
        // Update course availability / Aggiorna disponibilità corso
        if (iscrizione.getCorso() != null) {
            Corso corso = iscrizione.getCorso();
            corso.setDisponibilita(corso.getDisponibilita() + 1);
            corsoRepository.save(corso);
        }
        
        // Delete enrollment / Elimina iscrizione
        iscrizioneRepository.deleteById(id);
    }
    
    /**
     * Get enrollments by course
     * Recupera iscrizioni per corso
     * 
     * @param corsoId Course ID / ID del corso
     * @return List of enrollment DTOs / Lista DTO iscrizioni
     * @throws ResourceNotFoundException if course not found / se corso non trovato
     */
    public List<EnrollmentDTO> getEnrollmentsByCourse(Long corsoId) {
        // Check if course exists / Controlla se corso esiste
        if (!corsoRepository.existsById(corsoId)) {
            throw new ResourceNotFoundException("Course", "id", corsoId);
        }
        
        List<Iscrizione> iscrizioni = iscrizioneRepository.findByCorsoId(corsoId);
        
        return enrollmentMapper.toDTOList(iscrizioni);
    }
    
    /**
     * Get enrollments by participant email
     * Recupera iscrizioni per email partecipante
     * 
     * @param email Participant email / Email partecipante
     * @return List of enrollment DTOs / Lista DTO iscrizioni
     */
    public List<EnrollmentDTO> getEnrollmentsByParticipantEmail(String email) {
        List<Iscrizione> iscrizioni = iscrizioneRepository.findByPartecipanteEmail(email);
        
        return enrollmentMapper.toDTOList(iscrizioni);
    }
    
    /**
     * Validate enrollment data
     * Valida dati iscrizione
     * 
     * @param enrollmentDTO Enrollment DTO / DTO Iscrizione
     * @throws BusinessLogicException if validation fails / se validazione fallisce
     */
    private void validateEnrollmentData(EnrollmentDTO enrollmentDTO) {
        // Check if course ID is provided / Controlla se ID corso è fornito
        if (enrollmentDTO.getCorsoId() == null) {
            throw new BusinessLogicException(
                "MISSING_COURSE_ID", 
                "Course ID is required / ID corso è obbligatorio"
            );
        }
        
        // Check if participant data is provided / Controlla se dati partecipante sono forniti
        if (enrollmentDTO.getPartecipanteNome() == null || enrollmentDTO.getPartecipanteNome().trim().isEmpty()) {
            throw new BusinessLogicException(
                "MISSING_PARTICIPANT_NAME", 
                "Participant name is required / Nome partecipante è obbligatorio"
            );
        }
        
        if (enrollmentDTO.getPartecipanteCognome() == null || enrollmentDTO.getPartecipanteCognome().trim().isEmpty()) {
            throw new BusinessLogicException(
                "MISSING_PARTICIPANT_SURNAME", 
                "Participant surname is required / Cognome partecipante è obbligatorio"
            );
        }
        
        if (enrollmentDTO.getPartecipanteEmail() == null || enrollmentDTO.getPartecipanteEmail().trim().isEmpty()) {
            throw new BusinessLogicException(
                "MISSING_PARTICIPANT_EMAIL", 
                "Participant email is required / Email partecipante è obbligatoria"
            );
        }
        
        // Validate email format / Valida formato email
        if (!enrollmentDTO.getPartecipanteEmail().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            throw new BusinessLogicException(
                "INVALID_EMAIL_FORMAT", 
                "Invalid email format / Formato email non valido"
            );
        }
    }
}
