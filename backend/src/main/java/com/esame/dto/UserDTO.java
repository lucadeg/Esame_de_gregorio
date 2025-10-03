package com.esame.dto;

import jakarta.validation.constraints.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.esame.config.LocalDateTimeDeserializer;
import java.time.LocalDateTime;

/**
 * User Data Transfer Object
 * DTO per Utente
 * 
 * Represents user data for API communication
 * Rappresenta i dati dell'utente per la comunicazione API
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDTO {
    
    private Long id;
    
    @NotBlank(message = "Name is required / Il nome è obbligatorio")
    @Size(min = 2, max = 50, message = "Name must be between 2 and 50 characters / Il nome deve essere tra 2 e 50 caratteri")
    private String nome;
    
    @NotBlank(message = "Surname is required / Il cognome è obbligatorio")
    @Size(min = 2, max = 50, message = "Surname must be between 2 and 50 characters / Il cognome deve essere tra 2 e 50 caratteri")
    private String cognome;
    
    @NotBlank(message = "Email is required / L'email è obbligatoria")
    @Email(message = "Email must be valid / L'email deve essere valida")
    private String email;
    
    // Password field only for registration/update / Campo password solo per registrazione/aggiornamento
    @Size(min = 6, message = "Password must be at least 6 characters / La password deve essere di almeno 6 caratteri")
    private String password;
    
    @Pattern(regexp = "^[+]?[0-9\\s\\-()]{10,15}$", message = "Invalid phone number format / Formato numero di telefono non valido")
    private String phoneNumber;
    
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime dateOfBirth;
    
    private String profileImage;
    
    private Boolean isActive;
    
    private Boolean emailVerified;
    
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;
    
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime updatedAt;
    
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime lastLogin;
    
    private String role;
    
    private String subscriptionType;
    
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime subscriptionExpiresAt;
    
    // Additional fields for API responses / Campi aggiuntivi per risposte API
    private String fullName;
    private Boolean subscriptionActive;
    private Long daysRemaining;
    private Integer maxCourses;
    private Boolean hasAdvancedFeatures;
    
    // Constructors / Costruttori
    public UserDTO() {}
    
    public UserDTO(String nome, String cognome, String email, String password) {
        this.nome = nome;
        this.cognome = cognome;
        this.email = email;
        this.password = password;
    }
    
    // Getters and Setters / Getter e Setter
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getNome() {
        return nome;
    }
    
    public void setNome(String nome) {
        this.nome = nome;
    }
    
    public String getCognome() {
        return cognome;
    }
    
    public void setCognome(String cognome) {
        this.cognome = cognome;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public String getPhoneNumber() {
        return phoneNumber;
    }
    
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    
    public LocalDateTime getDateOfBirth() {
        return dateOfBirth;
    }
    
    public void setDateOfBirth(LocalDateTime dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }
    
    public String getProfileImage() {
        return profileImage;
    }
    
    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }
    
    public Boolean getIsActive() {
        return isActive;
    }
    
    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }
    
    public Boolean getEmailVerified() {
        return emailVerified;
    }
    
    public void setEmailVerified(Boolean emailVerified) {
        this.emailVerified = emailVerified;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    public LocalDateTime getLastLogin() {
        return lastLogin;
    }
    
    public void setLastLogin(LocalDateTime lastLogin) {
        this.lastLogin = lastLogin;
    }
    
    public String getRole() {
        return role;
    }
    
    public void setRole(String role) {
        this.role = role;
    }
    
    public String getSubscriptionType() {
        return subscriptionType;
    }
    
    public void setSubscriptionType(String subscriptionType) {
        this.subscriptionType = subscriptionType;
    }
    
    public LocalDateTime getSubscriptionExpiresAt() {
        return subscriptionExpiresAt;
    }
    
    public void setSubscriptionExpiresAt(LocalDateTime subscriptionExpiresAt) {
        this.subscriptionExpiresAt = subscriptionExpiresAt;
    }
    
    public String getFullName() {
        return fullName;
    }
    
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
    
    public Boolean getSubscriptionActive() {
        return subscriptionActive;
    }
    
    public void setSubscriptionActive(Boolean subscriptionActive) {
        this.subscriptionActive = subscriptionActive;
    }
    
    public Long getDaysRemaining() {
        return daysRemaining;
    }
    
    public void setDaysRemaining(Long daysRemaining) {
        this.daysRemaining = daysRemaining;
    }
    
    public Integer getMaxCourses() {
        return maxCourses;
    }
    
    public void setMaxCourses(Integer maxCourses) {
        this.maxCourses = maxCourses;
    }
    
    public Boolean getHasAdvancedFeatures() {
        return hasAdvancedFeatures;
    }
    
    public void setHasAdvancedFeatures(Boolean hasAdvancedFeatures) {
        this.hasAdvancedFeatures = hasAdvancedFeatures;
    }
    
    // Helper methods / Metodi helper
    public String getUserFullName() {
        if (nome != null && cognome != null) {
            return nome + " " + cognome;
        }
        return fullName;
    }
    
    public boolean isAdmin() {
        return "ADMIN".equals(role);
    }
    
    public boolean isInstructor() {
        return "INSTRUCTOR".equals(role);
    }
    
    public boolean isStudent() {
        return "STUDENT".equals(role);
    }
    
    public boolean isRecentlyActive() {
        return lastLogin != null && 
               lastLogin.isAfter(LocalDateTime.now().minusDays(30));
    }
}
