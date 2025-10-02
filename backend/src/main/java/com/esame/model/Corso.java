package com.esame.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Course Entity Model
 * Modello Entità Corso
 * 
 * Represents a course entity with all required fields
 * Rappresenta un'entità corso con tutti i campi richiesti
 */
@Entity
@Table(name = "corsi")
public class Corso {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "corso_id")
    private Long corsoId;
    
    @NotBlank(message = "Course title is required / Il titolo del corso è obbligatorio")
    @Size(max = 50, message = "Course title must not exceed 50 characters / Il titolo del corso non deve superare i 50 caratteri")
    @Column(name = "titolo", length = 50, nullable = false)
    private String titolo;
    
    @NotNull(message = "Start date and time is required / La data e ora di inizio è obbligatoria")
    @Column(name = "data_ora_inizio", nullable = false)
    private LocalDateTime dataOraInizio;
    
    @NotBlank(message = "Location is required / Il luogo è obbligatorio")
    @Size(max = 100, message = "Location must not exceed 100 characters / Il luogo non deve superare i 100 caratteri")
    @Column(name = "luogo", length = 100, nullable = false)
    private String luogo;
    
    @NotNull(message = "Availability is required / La disponibilità è obbligatoria")
    @Min(value = 0, message = "Availability cannot be negative / La disponibilità non può essere negativa")
    @Column(name = "disponibilita", nullable = false)
    private Integer disponibilita;
    
    @OneToMany(mappedBy = "corso", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Iscrizione> iscrizioni;
    
    // Default constructor / Costruttore di default
    public Corso() {}
    
    // Constructor with parameters / Costruttore con parametri
    public Corso(String titolo, LocalDateTime dataOraInizio, String luogo, Integer disponibilita) {
        this.titolo = titolo;
        this.dataOraInizio = dataOraInizio;
        this.luogo = luogo;
        this.disponibilita = disponibilita;
    }
    
    // Getters and Setters / Getter e Setter
    public Long getCorsoId() {
        return corsoId;
    }
    
    public void setCorsoId(Long corsoId) {
        this.corsoId = corsoId;
    }
    
    public String getTitolo() {
        return titolo;
    }
    
    public void setTitolo(String titolo) {
        this.titolo = titolo;
    }
    
    public LocalDateTime getDataOraInizio() {
        return dataOraInizio;
    }
    
    public void setDataOraInizio(LocalDateTime dataOraInizio) {
        this.dataOraInizio = dataOraInizio;
    }
    
    public String getLuogo() {
        return luogo;
    }
    
    public void setLuogo(String luogo) {
        this.luogo = luogo;
    }
    
    public Integer getDisponibilita() {
        return disponibilita;
    }
    
    public void setDisponibilita(Integer disponibilita) {
        this.disponibilita = disponibilita;
    }
    
    public List<Iscrizione> getIscrizioni() {
        return iscrizioni;
    }
    
    public void setIscrizioni(List<Iscrizione> iscrizioni) {
        this.iscrizioni = iscrizioni;
    }
}
