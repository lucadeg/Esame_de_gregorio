package com.esame.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
    
    // Enhanced course information / Informazioni avanzate del corso
    @Column(name = "programma", columnDefinition = "TEXT")
    private String programma;
    
    @Column(name = "docenti", length = 200)
    private String docenti;
    
    @Column(name = "informazioni_generali", columnDefinition = "TEXT")
    private String informazioniGenerali;
    
    @Column(name = "data_test")
    private LocalDateTime dataTest;
    
    @Column(name = "data_completamento")
    private LocalDateTime dataCompletamento;
    
    @Column(name = "durata_ore")
    private Integer durataOre;
    
    @Column(name = "livello", length = 20)
    private String livello;
    
    @Column(name = "categoria", length = 50)
    private String categoria;
    
    @Column(name = "prezzo")
    private Double prezzo;
    
    @Column(name = "certificazione")
    private Boolean certificazione = false;
    
    @OneToMany(mappedBy = "corso", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
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
    
    public String getProgramma() {
        return programma;
    }
    
    public void setProgramma(String programma) {
        this.programma = programma;
    }
    
    public String getDocenti() {
        return docenti;
    }
    
    public void setDocenti(String docenti) {
        this.docenti = docenti;
    }
    
    public String getInformazioniGenerali() {
        return informazioniGenerali;
    }
    
    public void setInformazioniGenerali(String informazioniGenerali) {
        this.informazioniGenerali = informazioniGenerali;
    }
    
    public LocalDateTime getDataTest() {
        return dataTest;
    }
    
    public void setDataTest(LocalDateTime dataTest) {
        this.dataTest = dataTest;
    }
    
    public LocalDateTime getDataCompletamento() {
        return dataCompletamento;
    }
    
    public void setDataCompletamento(LocalDateTime dataCompletamento) {
        this.dataCompletamento = dataCompletamento;
    }
    
    public Integer getDurataOre() {
        return durataOre;
    }
    
    public void setDurataOre(Integer durataOre) {
        this.durataOre = durataOre;
    }
    
    public String getLivello() {
        return livello;
    }
    
    public void setLivello(String livello) {
        this.livello = livello;
    }
    
    public String getCategoria() {
        return categoria;
    }
    
    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }
    
    public Double getPrezzo() {
        return prezzo;
    }
    
    public void setPrezzo(Double prezzo) {
        this.prezzo = prezzo;
    }
    
    public Boolean getCertificazione() {
        return certificazione;
    }
    
    public void setCertificazione(Boolean certificazione) {
        this.certificazione = certificazione;
    }
    
    public List<Iscrizione> getIscrizioni() {
        return iscrizioni;
    }
    
    public void setIscrizioni(List<Iscrizione> iscrizioni) {
        this.iscrizioni = iscrizioni;
    }
}
