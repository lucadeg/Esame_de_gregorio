package com.esame.mapper;

import com.esame.model.Iscrizione;
import com.esame.dto.EnrollmentDTO;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Enrollment Mapper
 * Mapper per Iscrizione
 * 
 * Maps between Enrollment entity and DTO
 * Mappa tra entità Iscrizione e DTO
 */
@Component
public class EnrollmentMapper {
    
    /**
     * Convert Enrollment entity to DTO
     * Converte entità Iscrizione in DTO
     * 
     * @param iscrizione Enrollment entity / Entità Iscrizione
     * @return Enrollment DTO / DTO Iscrizione
     */
    public EnrollmentDTO toDTO(Iscrizione iscrizione) {
        if (iscrizione == null) {
            return null;
        }
        
        EnrollmentDTO dto = new EnrollmentDTO();
        dto.setIscrizioneId(iscrizione.getIscrizioneId());
        dto.setCorsoId(iscrizione.getCorsoId());
        dto.setPartecipanteNome(iscrizione.getPartecipanteNome());
        dto.setPartecipanteCognome(iscrizione.getPartecipanteCognome());
        dto.setPartecipanteEmail(iscrizione.getPartecipanteEmail());
        dto.setDataOraIscrizione(iscrizione.getDataOraIscrizione());
        
        // Set additional fields from course if available / Imposta campi aggiuntivi dal corso se disponibile
        if (iscrizione.getCorso() != null) {
            dto.setCorsoTitolo(iscrizione.getCorso().getTitolo());
            dto.setCorsoLuogo(iscrizione.getCorso().getLuogo());
            dto.setCorsoDataOraInizio(iscrizione.getCorso().getDataOraInizio());
        }
        
        // Set full name / Imposta nome completo
        dto.setFullName(iscrizione.getPartecipanteNome() + " " + iscrizione.getPartecipanteCognome());
        
        // Set status / Imposta stato
        dto.setStatus(determineStatus(iscrizione));
        
        return dto;
    }
    
    /**
     * Convert Enrollment DTO to entity
     * Converte DTO Iscrizione in entità
     * 
     * @param dto Enrollment DTO / DTO Iscrizione
     * @return Enrollment entity / Entità Iscrizione
     */
    public Iscrizione toEntity(EnrollmentDTO dto) {
        if (dto == null) {
            return null;
        }
        
        Iscrizione iscrizione = new Iscrizione();
        iscrizione.setIscrizioneId(dto.getIscrizioneId());
        iscrizione.setCorsoId(dto.getCorsoId());
        iscrizione.setPartecipanteNome(dto.getPartecipanteNome());
        iscrizione.setPartecipanteCognome(dto.getPartecipanteCognome());
        iscrizione.setPartecipanteEmail(dto.getPartecipanteEmail());
        iscrizione.setDataOraIscrizione(dto.getDataOraIscrizione());
        
        return iscrizione;
    }
    
    /**
     * Convert list of Enrollment entities to DTOs
     * Converte lista di entità Iscrizione in DTO
     * 
     * @param iscrizioni List of Enrollment entities / Lista di entità Iscrizione
     * @return List of Enrollment DTOs / Lista di DTO Iscrizione
     */
    public List<EnrollmentDTO> toDTOList(List<Iscrizione> iscrizioni) {
        if (iscrizioni == null) {
            return null;
        }
        
        return iscrizioni.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Update Enrollment entity with DTO data
     * Aggiorna entità Iscrizione con dati DTO
     * 
     * @param iscrizione Existing Enrollment entity / Entità Iscrizione esistente
     * @param dto Enrollment DTO with new data / DTO Iscrizione con nuovi dati
     * @return Updated Enrollment entity / Entità Iscrizione aggiornata
     */
    public Iscrizione updateEntity(Iscrizione iscrizione, EnrollmentDTO dto) {
        if (iscrizione == null || dto == null) {
            return iscrizione;
        }
        
        // Update only non-null fields / Aggiorna solo campi non null
        if (dto.getPartecipanteNome() != null) {
            iscrizione.setPartecipanteNome(dto.getPartecipanteNome());
        }
        if (dto.getPartecipanteCognome() != null) {
            iscrizione.setPartecipanteCognome(dto.getPartecipanteCognome());
        }
        if (dto.getPartecipanteEmail() != null) {
            iscrizione.setPartecipanteEmail(dto.getPartecipanteEmail());
        }
        
        return iscrizione;
    }
    
    /**
     * Determine enrollment status based on course dates
     * Determina stato dell'iscrizione basato sulle date del corso
     * 
     * @param iscrizione Enrollment entity / Entità Iscrizione
     * @return Enrollment status / Stato dell'iscrizione
     */
    private String determineStatus(Iscrizione iscrizione) {
        if (iscrizione.getCorso() == null || iscrizione.getCorso().getDataOraInizio() == null) {
            return "UNKNOWN";
        }
        
        java.time.LocalDateTime now = java.time.LocalDateTime.now();
        java.time.LocalDateTime courseStart = iscrizione.getCorso().getDataOraInizio();
        
        if (courseStart.isBefore(now)) {
            return "STARTED";
        } else if (courseStart.isAfter(now.plusDays(7))) {
            return "UPCOMING";
        } else {
            return "STARTING_SOON";
        }
    }
}
