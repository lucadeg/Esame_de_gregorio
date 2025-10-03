package com.esame.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Password Change Data Transfer Object
 * DTO per Cambio Password
 * 
 * Represents password change request data for API communication
 * Rappresenta i dati della richiesta di cambio password per la comunicazione API
 */
public class PasswordChangeDTO {
    
    @NotBlank(message = "Current password is required / La password corrente è obbligatoria")
    private String currentPassword;
    
    @NotBlank(message = "New password is required / La nuova password è obbligatoria")
    @Size(min = 6, message = "New password must be at least 6 characters / La nuova password deve essere di almeno 6 caratteri")
    private String newPassword;
    
    @NotBlank(message = "Password confirmation is required / La conferma password è obbligatoria")
    private String confirmPassword;
    
    // Constructors / Costruttori
    public PasswordChangeDTO() {}
    
    public PasswordChangeDTO(String currentPassword, String newPassword, String confirmPassword) {
        this.currentPassword = currentPassword;
        this.newPassword = newPassword;
        this.confirmPassword = confirmPassword;
    }
    
    // Getters and Setters / Getter e Setter
    public String getCurrentPassword() {
        return currentPassword;
    }
    
    public void setCurrentPassword(String currentPassword) {
        this.currentPassword = currentPassword;
    }
    
    public String getNewPassword() {
        return newPassword;
    }
    
    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
    
    public String getConfirmPassword() {
        return confirmPassword;
    }
    
    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
    
    // Helper methods / Metodi helper
    public boolean isPasswordMatch() {
        return newPassword != null && newPassword.equals(confirmPassword);
    }
    
    public boolean isSameAsCurrent() {
        return newPassword != null && newPassword.equals(currentPassword);
    }
}
