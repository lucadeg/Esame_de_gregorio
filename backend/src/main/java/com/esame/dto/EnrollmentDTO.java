package com.esame.dto;

import jakarta.validation.constraints.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.LocalDateTime;

/**
 * Enrollment Data Transfer Object
 * DTO per Iscrizione
 * 
 * Represents enrollment data for API communication
 * Rappresenta i dati dell'iscrizione per la comunicazione API
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EnrollmentDTO {
    
    private Long iscrizioneId;
    
    @NotNull(message = "Course ID is required / L'ID del corso è obbligatorio")
    private Long corsoId;
    
    @NotBlank(message = "Participant first name is required / Il nome del partecipante è obbligatorio")
    @Size(max = 30, message = "First name must not exceed 30 characters / Il nome non deve superare i 30 caratteri")
    private String partecipanteNome;
    
    @NotBlank(message = "Participant last name is required / Il cognome del partecipante è obbligatorio")
    @Size(max = 30, message = "Last name must not exceed 30 characters / Il cognome non deve superare i 30 caratteri")
    private String partecipanteCognome;
    
    @NotBlank(message = "Participant email is required / L'email del partecipante è obbligatoria")
    @Email(message = "Invalid email format / Formato email non valido")
    @Size(max = 50, message = "Email must not exceed 50 characters / L'email non deve superare i 50 caratteri")
    private String partecipanteEmail;
    
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime dataOraIscrizione;
    
    // Additional fields for API responses / Campi aggiuntivi per risposte API
    private String corsoTitolo;
    private String corsoLuogo;
    private LocalDateTime corsoDataOraInizio;
    private String status;
    private String fullName;
    
    // Constructors / Costruttori
    public EnrollmentDTO() {}
    
    public EnrollmentDTO(Long corsoId, String partecipanteNome, String partecipanteCognome, String partecipanteEmail) {
        this.corsoId = corsoId;
        this.partecipanteNome = partecipanteNome;
        this.partecipanteCognome = partecipanteCognome;
        this.partecipanteEmail = partecipanteEmail;
    }
    
    // Getters and Setters / Getter e Setter
    public Long getIscrizioneId() {
        return iscrizioneId;
    }
    
    public void setIscrizioneId(Long iscrizioneId) {
        this.iscrizioneId = iscrizioneId;
    }
    
    public Long getCorsoId() {
        return corsoId;
    }
    
    public void setCorsoId(Long corsoId) {
        this.corsoId = corsoId;
    }
    
    public String getPartecipanteNome() {
        return partecipanteNome;
    }
    
    public void setPartecipanteNome(String partecipanteNome) {
        this.partecipanteNome = partecipanteNome;
    }
    
    public String getPartecipanteCognome() {
        return partecipanteCognome;
    }
    
    public void setPartecipanteCognome(String partecipanteCognome) {
        this.partecipanteCognome = partecipanteCognome;
    }
    
    public String getPartecipanteEmail() {
        return partecipanteEmail;
    }
    
    public void setPartecipanteEmail(String partecipanteEmail) {
        this.partecipanteEmail = partecipanteEmail;
    }
    
    public LocalDateTime getDataOraIscrizione() {
        return dataOraIscrizione;
    }
    
    public void setDataOraIscrizione(LocalDateTime dataOraIscrizione) {
        this.dataOraIscrizione = dataOraIscrizione;
    }
    
    public String getCorsoTitolo() {
        return corsoTitolo;
    }
    
    public void setCorsoTitolo(String corsoTitolo) {
        this.corsoTitolo = corsoTitolo;
    }
    
    public String getCorsoLuogo() {
        return corsoLuogo;
    }
    
    public void setCorsoLuogo(String corsoLuogo) {
        this.corsoLuogo = corsoLuogo;
    }
    
    public LocalDateTime getCorsoDataOraInizio() {
        return corsoDataOraInizio;
    }
    
    public void setCorsoDataOraInizio(LocalDateTime corsoDataOraInizio) {
        this.corsoDataOraInizio = corsoDataOraInizio;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public String getFullName() {
        return fullName;
    }
    
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
    
    // Helper methods / Metodi helper
    public String getParticipantFullName() {
        if (partecipanteNome != null && partecipanteCognome != null) {
            return partecipanteNome + " " + partecipanteCognome;
        }
        return fullName;
    }
    
    public boolean isRecent() {
        return dataOraIscrizione != null && 
               dataOraIscrizione.isAfter(LocalDateTime.now().minusDays(7));
    }
}
