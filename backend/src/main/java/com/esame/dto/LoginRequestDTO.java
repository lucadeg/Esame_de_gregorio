package com.esame.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Login Request Data Transfer Object
 * DTO per Richiesta di Login
 * 
 * Represents login request data for API communication
 * Rappresenta i dati della richiesta di login per la comunicazione API
 */
public class LoginRequestDTO {
    
    @NotBlank(message = "Email is required / L'email è obbligatoria")
    @Email(message = "Email must be valid / L'email deve essere valida")
    private String email;
    
    @NotBlank(message = "Password is required / La password è obbligatoria")
    @Size(min = 6, message = "Password must be at least 6 characters / La password deve essere di almeno 6 caratteri")
    private String password;
    
    // Remember me option / Opzione ricorda
    private Boolean rememberMe = false;
    
    // Constructors / Costruttori
    public LoginRequestDTO() {}
    
    public LoginRequestDTO(String email, String password) {
        this.email = email;
        this.password = password;
    }
    
    public LoginRequestDTO(String email, String password, Boolean rememberMe) {
        this.email = email;
        this.password = password;
        this.rememberMe = rememberMe;
    }
    
    // Getters and Setters / Getter e Setter
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
    
    public Boolean getRememberMe() {
        return rememberMe;
    }
    
    public void setRememberMe(Boolean rememberMe) {
        this.rememberMe = rememberMe;
    }
}
