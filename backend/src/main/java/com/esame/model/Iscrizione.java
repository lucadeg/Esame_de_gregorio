package com.esame.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.time.LocalDateTime;

/**
 * Enrollment Entity Model
 * Modello Entità Iscrizione
 * 
 * Represents an enrollment entity with all required fields
 * Rappresenta un'entità iscrizione con tutti i campi richiesti
 */
@Entity
@Table(name = "iscrizioni")
public class Iscrizione {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "iscrizione_id")
    private Long iscrizioneId;
    
    @NotNull(message = "Course ID is required / L'ID del corso è obbligatorio")
    @Column(name = "corso_id", nullable = false, insertable = false, updatable = false)
    private Long corsoId;
    
    @NotBlank(message = "Participant first name is required / Il nome del partecipante è obbligatorio")
    @Size(max = 30, message = "First name must not exceed 30 characters / Il nome non deve superare i 30 caratteri")
    @Column(name = "partecipante_nome", length = 30, nullable = false)
    private String partecipanteNome;
    
    @NotBlank(message = "Participant last name is required / Il cognome del partecipante è obbligatorio")
    @Size(max = 30, message = "Last name must not exceed 30 characters / Il cognome non deve superare i 30 caratteri")
    @Column(name = "partecipante_cognome", length = 30, nullable = false)
    private String partecipanteCognome;
    
    @NotBlank(message = "Participant email is required / L'email del partecipante è obbligatoria")
    @Email(message = "Invalid email format / Formato email non valido")
    @Size(max = 50, message = "Email must not exceed 50 characters / L'email non deve superare i 50 caratteri")
    @Column(name = "partecipante_email", length = 50, nullable = false)
    private String partecipanteEmail;
    
    @Column(name = "data_ora_iscrizione", nullable = false, updatable = false)
    private LocalDateTime dataOraIscrizione;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "corso_id", referencedColumnName = "corso_id")
    @JsonIgnore
    private Corso corso;
    
    // Default constructor / Costruttore di default
    public Iscrizione() {
        this.dataOraIscrizione = LocalDateTime.now();
    }
    
    // Constructor with parameters / Costruttore con parametri
    public Iscrizione(Long corsoId, String partecipanteNome, String partecipanteCognome, String partecipanteEmail) {
        this.corsoId = corsoId;
        this.partecipanteNome = partecipanteNome;
        this.partecipanteCognome = partecipanteCognome;
        this.partecipanteEmail = partecipanteEmail;
        this.dataOraIscrizione = LocalDateTime.now();
    }
    
    // Constructor with Corso object / Costruttore con oggetto Corso
    public Iscrizione(Corso corso, String partecipanteNome, String partecipanteCognome, String partecipanteEmail) {
        this.corso = corso;
        this.corsoId = corso.getCorsoId();
        this.partecipanteNome = partecipanteNome;
        this.partecipanteCognome = partecipanteCognome;
        this.partecipanteEmail = partecipanteEmail;
        this.dataOraIscrizione = LocalDateTime.now();
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
    
    public Corso getCorso() {
        return corso;
    }
    
    public void setCorso(Corso corso) {
        this.corso = corso;
    }
}
