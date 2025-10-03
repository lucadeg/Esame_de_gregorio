package com.esame.dto;

import jakarta.validation.constraints.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Course Data Transfer Object
 * DTO per Corso
 * 
 * Represents course data for API communication
 * Rappresenta i dati del corso per la comunicazione API
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CourseDTO {
    
    private Long corsoId;
    
    @NotBlank(message = "Course title is required / Il titolo del corso è obbligatorio")
    @Size(max = 50, message = "Course title must not exceed 50 characters / Il titolo del corso non deve superare i 50 caratteri")
    private String titolo;
    
    @NotNull(message = "Start date and time is required / La data e ora di inizio è obbligatoria")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime dataOraInizio;
    
    @NotBlank(message = "Location is required / Il luogo è obbligatorio")
    @Size(max = 100, message = "Location must not exceed 100 characters / Il luogo non deve superare i 100 caratteri")
    private String luogo;
    
    @NotNull(message = "Availability is required / La disponibilità è obbligatoria")
    @Min(value = 0, message = "Availability cannot be negative / La disponibilità non può essere negativa")
    private Integer disponibilita;
    
    // Enhanced course information / Informazioni avanzate del corso
    private String programma;
    
    @Size(max = 200, message = "Teachers field must not exceed 200 characters / Il campo docenti non deve superare i 200 caratteri")
    private String docenti;
    
    private String informazioniGenerali;
    
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime dataTest;
    
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime dataCompletamento;
    
    @Min(value = 1, message = "Duration must be at least 1 hour / La durata deve essere di almeno 1 ora")
    @Max(value = 1000, message = "Duration cannot exceed 1000 hours / La durata non può superare le 1000 ore")
    private Integer durataOre;
    
    @Size(max = 20, message = "Level must not exceed 20 characters / Il livello non deve superare i 20 caratteri")
    private String livello;
    
    @Size(max = 50, message = "Category must not exceed 50 characters / La categoria non deve superare i 50 caratteri")
    private String categoria;
    
    @DecimalMin(value = "0.0", message = "Price cannot be negative / Il prezzo non può essere negativo")
    private Double prezzo;
    
    private Boolean certificazione = false;
    
    // Additional fields for API responses / Campi aggiuntivi per risposte API
    private Integer iscrizioniCount;
    private Boolean isAvailable;
    private String status;
    private List<EnrollmentDTO> iscrizioni;
    
    // Constructors / Costruttori
    public CourseDTO() {}
    
    public CourseDTO(String titolo, LocalDateTime dataOraInizio, String luogo, Integer disponibilita) {
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
    
    public Integer getIscrizioniCount() {
        return iscrizioniCount;
    }
    
    public void setIscrizioniCount(Integer iscrizioniCount) {
        this.iscrizioniCount = iscrizioniCount;
    }
    
    public Boolean getIsAvailable() {
        return isAvailable;
    }
    
    public void setIsAvailable(Boolean isAvailable) {
        this.isAvailable = isAvailable;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public List<EnrollmentDTO> getIscrizioni() {
        return iscrizioni;
    }
    
    public void setIscrizioni(List<EnrollmentDTO> iscrizioni) {
        this.iscrizioni = iscrizioni;
    }
    
    // Helper methods / Metodi helper
    public boolean isUpcoming() {
        return dataOraInizio != null && dataOraInizio.isAfter(LocalDateTime.now());
    }
    
    public boolean isFull() {
        return disponibilita != null && disponibilita <= 0;
    }
    
    public boolean hasAvailability() {
        return disponibilita != null && disponibilita > 0;
    }
}
