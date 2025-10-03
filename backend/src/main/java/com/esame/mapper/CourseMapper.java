package com.esame.mapper;

import com.esame.model.Corso;
import com.esame.dto.CourseDTO;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Course Mapper
 * Mapper per Corso
 * 
 * Maps between Course entity and DTO
 * Mappa tra entità Corso e DTO
 */
@Component
public class CourseMapper {
    
    /**
     * Convert Course entity to DTO
     * Converte entità Corso in DTO
     * 
     * @param corso Course entity / Entità Corso
     * @return Course DTO / DTO Corso
     */
    public CourseDTO toDTO(Corso corso) {
        if (corso == null) {
            return null;
        }
        
        CourseDTO dto = new CourseDTO();
        dto.setCorsoId(corso.getCorsoId());
        dto.setTitolo(corso.getTitolo());
        dto.setDataOraInizio(corso.getDataOraInizio());
        dto.setLuogo(corso.getLuogo());
        dto.setDisponibilita(corso.getDisponibilita());
        dto.setProgramma(corso.getProgramma());
        dto.setDocenti(corso.getDocenti());
        dto.setInformazioniGenerali(corso.getInformazioniGenerali());
        dto.setDataTest(corso.getDataTest());
        dto.setDataCompletamento(corso.getDataCompletamento());
        dto.setDurataOre(corso.getDurataOre());
        dto.setLivello(corso.getLivello());
        dto.setCategoria(corso.getCategoria());
        dto.setPrezzo(corso.getPrezzo());
        dto.setCertificazione(corso.getCertificazione());
        
        // Set additional fields / Imposta campi aggiuntivi
        dto.setIsAvailable(corso.getDisponibilita() != null && corso.getDisponibilita() > 0);
        dto.setStatus(determineStatus(corso));
        
        // Set enrollments count if available / Imposta conteggio iscrizioni se disponibile
        if (corso.getIscrizioni() != null) {
            dto.setIscrizioniCount(corso.getIscrizioni().size());
        }
        
        return dto;
    }
    
    /**
     * Convert Course DTO to entity
     * Converte DTO Corso in entità
     * 
     * @param dto Course DTO / DTO Corso
     * @return Course entity / Entità Corso
     */
    public Corso toEntity(CourseDTO dto) {
        if (dto == null) {
            return null;
        }
        
        Corso corso = new Corso();
        corso.setCorsoId(dto.getCorsoId());
        corso.setTitolo(dto.getTitolo());
        corso.setDataOraInizio(dto.getDataOraInizio());
        corso.setLuogo(dto.getLuogo());
        corso.setDisponibilita(dto.getDisponibilita());
        corso.setProgramma(dto.getProgramma());
        corso.setDocenti(dto.getDocenti());
        corso.setInformazioniGenerali(dto.getInformazioniGenerali());
        corso.setDataTest(dto.getDataTest());
        corso.setDataCompletamento(dto.getDataCompletamento());
        corso.setDurataOre(dto.getDurataOre());
        corso.setLivello(dto.getLivello());
        corso.setCategoria(dto.getCategoria());
        corso.setPrezzo(dto.getPrezzo());
        corso.setCertificazione(dto.getCertificazione());
        
        return corso;
    }
    
    /**
     * Convert list of Course entities to DTOs
     * Converte lista di entità Corso in DTO
     * 
     * @param corsi List of Course entities / Lista di entità Corso
     * @return List of Course DTOs / Lista di DTO Corso
     */
    public List<CourseDTO> toDTOList(List<Corso> corsi) {
        if (corsi == null) {
            return null;
        }
        
        return corsi.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Update Course entity with DTO data
     * Aggiorna entità Corso con dati DTO
     * 
     * @param corso Existing Course entity / Entità Corso esistente
     * @param dto Course DTO with new data / DTO Corso con nuovi dati
     * @return Updated Course entity / Entità Corso aggiornata
     */
    public Corso updateEntity(Corso corso, CourseDTO dto) {
        if (corso == null || dto == null) {
            return corso;
        }
        
        // Update only non-null fields / Aggiorna solo campi non null
        if (dto.getTitolo() != null) {
            corso.setTitolo(dto.getTitolo());
        }
        if (dto.getDataOraInizio() != null) {
            corso.setDataOraInizio(dto.getDataOraInizio());
        }
        if (dto.getLuogo() != null) {
            corso.setLuogo(dto.getLuogo());
        }
        if (dto.getDisponibilita() != null) {
            corso.setDisponibilita(dto.getDisponibilita());
        }
        if (dto.getProgramma() != null) {
            corso.setProgramma(dto.getProgramma());
        }
        if (dto.getDocenti() != null) {
            corso.setDocenti(dto.getDocenti());
        }
        if (dto.getInformazioniGenerali() != null) {
            corso.setInformazioniGenerali(dto.getInformazioniGenerali());
        }
        if (dto.getDataTest() != null) {
            corso.setDataTest(dto.getDataTest());
        }
        if (dto.getDataCompletamento() != null) {
            corso.setDataCompletamento(dto.getDataCompletamento());
        }
        if (dto.getDurataOre() != null) {
            corso.setDurataOre(dto.getDurataOre());
        }
        if (dto.getLivello() != null) {
            corso.setLivello(dto.getLivello());
        }
        if (dto.getCategoria() != null) {
            corso.setCategoria(dto.getCategoria());
        }
        if (dto.getPrezzo() != null) {
            corso.setPrezzo(dto.getPrezzo());
        }
        if (dto.getCertificazione() != null) {
            corso.setCertificazione(dto.getCertificazione());
        }
        
        return corso;
    }
    
    /**
     * Determine course status based on availability and dates
     * Determina stato del corso basato su disponibilità e date
     * 
     * @param corso Course entity / Entità Corso
     * @return Course status / Stato del corso
     */
    private String determineStatus(Corso corso) {
        if (corso.getDisponibilita() == null || corso.getDisponibilita() <= 0) {
            return "FULL";
        }
        
        if (corso.getDataOraInizio() == null) {
            return "UNKNOWN";
        }
        
        java.time.LocalDateTime now = java.time.LocalDateTime.now();
        
        if (corso.getDataOraInizio().isBefore(now)) {
            return "STARTED";
        } else if (corso.getDataOraInizio().isAfter(now.plusDays(7))) {
            return "UPCOMING";
        } else {
            return "STARTING_SOON";
        }
    }
}
