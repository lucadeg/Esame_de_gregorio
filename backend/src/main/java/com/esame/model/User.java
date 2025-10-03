package com.esame.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

/**
 * User Entity
 * Entità Utente
 * 
 * Represents a user in the system with authentication and subscription info
 * Rappresenta un utente nel sistema con informazioni di autenticazione e abbonamento
 */
@Entity
@Table(name = "users")
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "Nome è obbligatorio")
    @Size(min = 2, max = 50, message = "Nome deve essere tra 2 e 50 caratteri")
    @Column(nullable = false)
    private String nome;
    
    @NotBlank(message = "Cognome è obbligatorio")
    @Size(min = 2, max = 50, message = "Cognome deve essere tra 2 e 50 caratteri")
    @Column(nullable = false)
    private String cognome;
    
    @NotBlank(message = "Email è obbligatoria")
    @Email(message = "Email deve essere valida")
    @Column(unique = true, nullable = false)
    private String email;
    
    @NotBlank(message = "Password è obbligatoria")
    @Size(min = 6, message = "Password deve essere di almeno 6 caratteri")
    @Column(nullable = false)
    private String password;
    
    @Column(name = "phone_number")
    private String phoneNumber;
    
    @Column(name = "date_of_birth")
    private LocalDateTime dateOfBirth;
    
    @Column(name = "profile_image")
    private String profileImage;
    
    @Column(name = "is_active")
    private Boolean isActive = true;
    
    @Column(name = "email_verified")
    private Boolean emailVerified = false;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();
    
    @Column(name = "last_login")
    private LocalDateTime lastLogin;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private UserRole role = UserRole.STUDENT;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "subscription_type")
    private SubscriptionType subscriptionType = SubscriptionType.FREE;
    
    @Column(name = "subscription_expires_at")
    private LocalDateTime subscriptionExpiresAt;
    
    // Constructors / Costruttori
    public User() {}
    
    public User(String nome, String cognome, String email, String password) {
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
    
    public UserRole getRole() {
        return role;
    }
    
    public void setRole(UserRole role) {
        this.role = role;
    }
    
    public SubscriptionType getSubscriptionType() {
        return subscriptionType;
    }
    
    public void setSubscriptionType(SubscriptionType subscriptionType) {
        this.subscriptionType = subscriptionType;
    }
    
    public LocalDateTime getSubscriptionExpiresAt() {
        return subscriptionExpiresAt;
    }
    
    public void setSubscriptionExpiresAt(LocalDateTime subscriptionExpiresAt) {
        this.subscriptionExpiresAt = subscriptionExpiresAt;
    }
    
    // Helper methods / Metodi helper
    public String getFullName() {
        return nome + " " + cognome;
    }
    
    public boolean isSubscriptionActive() {
        if (subscriptionType == SubscriptionType.FREE) {
            return true;
        }
        return subscriptionExpiresAt != null && subscriptionExpiresAt.isAfter(LocalDateTime.now());
    }
    
    public boolean hasRole(UserRole requiredRole) {
        return this.role == requiredRole;
    }
    
    public boolean isAdmin() {
        return hasRole(UserRole.ADMIN);
    }
    
    public boolean isInstructor() {
        return hasRole(UserRole.INSTRUCTOR);
    }
    
    public boolean isStudent() {
        return hasRole(UserRole.STUDENT);
    }
    
    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
    
    // Enums / Enumerazioni
    public enum UserRole {
        STUDENT("Studente"),
        INSTRUCTOR("Istruttore"),
        ADMIN("Amministratore");
        
        private final String displayName;
        
        UserRole(String displayName) {
            this.displayName = displayName;
        }
        
        public String getDisplayName() {
            return displayName;
        }
    }
    
    public enum SubscriptionType {
        FREE("Gratuito", 0, 3, false),
        BASIC("Base", 9.99, 10, true),
        PREMIUM("Premium", 19.99, 25, true),
        ENTERPRISE("Enterprise", 49.99, 100, true);
        
        private final String displayName;
        private final double price;
        private final int maxCourses;
        private final boolean hasAdvancedFeatures;
        
        SubscriptionType(String displayName, double price, int maxCourses, boolean hasAdvancedFeatures) {
            this.displayName = displayName;
            this.price = price;
            this.maxCourses = maxCourses;
            this.hasAdvancedFeatures = hasAdvancedFeatures;
        }
        
        public String getDisplayName() {
            return displayName;
        }
        
        public double getPrice() {
            return price;
        }
        
        public int getMaxCourses() {
            return maxCourses;
        }
        
        public boolean hasAdvancedFeatures() {
            return hasAdvancedFeatures;
        }
    }
}
