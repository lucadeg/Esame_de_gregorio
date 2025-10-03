package com.esame.controller;

import com.esame.model.User;
import com.esame.repository.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Authentication Controller
 * Controller Autenticazione
 * 
 * REST API endpoints for user authentication and registration
 * Endpoint API REST per autenticazione e registrazione utenti
 */
@RestController
@RequestMapping("/auth")
@Tag(name = "Authentication", description = "API for user authentication and registration / API per autenticazione e registrazione utenti")
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:3000", "http://127.0.0.1:5173"})
public class AuthController {
    
    @Autowired
    private UserRepository userRepository;
    
    /**
     * User registration
     * Registrazione utente
     * 
     * @param user User object to register / Oggetto Utente da registrare
     * @return Created user / Utente creato
     */
    @PostMapping("/register")
    @Operation(summary = "Register new user", description = "Create a new user account / Crea un nuovo account utente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "User registered successfully / Utente registrato con successo"),
        @ApiResponse(responseCode = "400", description = "Invalid user data / Dati utente non validi"),
        @ApiResponse(responseCode = "409", description = "Email already exists / Email già esistente"),
        @ApiResponse(responseCode = "500", description = "Internal server error / Errore interno del server")
    })
    public ResponseEntity<?> register(
            @Parameter(description = "User object to register / Oggetto Utente da registrare")
            @Valid @RequestBody User user) {
        
        try {
            // Check if email already exists / Controlla se email esiste già
            if (userRepository.findByEmail(user.getEmail()).isPresent()) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "Email già registrata");
                error.put("message", "Un utente con questa email esiste già");
                return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
            }
            
            // Set default values / Imposta valori di default
            user.setCreatedAt(LocalDateTime.now());
            user.setUpdatedAt(LocalDateTime.now());
            user.setIsActive(true);
            user.setEmailVerified(false);
            user.setRole(User.UserRole.STUDENT);
            user.setSubscriptionType(User.SubscriptionType.FREE);
            
            // Save user / Salva utente
            User savedUser = userRepository.save(user);
            
            // Remove password from response / Rimuovi password dalla risposta
            savedUser.setPassword(null);
            
            return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
            
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Errore durante la registrazione");
            error.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
    
    /**
     * User login
     * Accesso utente
     * 
     * @param loginRequest Login credentials / Credenziali di accesso
     * @return User info and token / Informazioni utente e token
     */
    @PostMapping("/login")
    @Operation(summary = "User login", description = "Authenticate user and return user info / Autentica utente e restituisce informazioni utente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Login successful / Accesso riuscito"),
        @ApiResponse(responseCode = "401", description = "Invalid credentials / Credenziali non valide"),
        @ApiResponse(responseCode = "403", description = "Account inactive / Account inattivo"),
        @ApiResponse(responseCode = "500", description = "Internal server error / Errore interno del server")
    })
    public ResponseEntity<?> login(
            @Parameter(description = "Login credentials / Credenziali di accesso")
            @RequestBody Map<String, String> loginRequest) {
        
        try {
            String email = loginRequest.get("email");
            String password = loginRequest.get("password");
            
            if (email == null || password == null) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "Email e password sono obbligatori");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
            }
            
            // Find user by email / Trova utente per email
            Optional<User> userOpt = userRepository.findActiveByEmail(email);
            
            if (userOpt.isEmpty()) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "Credenziali non valide");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
            }
            
            User user = userOpt.get();
            
            // Check password (simple comparison for demo) / Controlla password (confronto semplice per demo)
            if (!user.getPassword().equals(password)) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "Credenziali non valide");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
            }
            
            // Update last login / Aggiorna ultimo accesso
            user.setLastLogin(LocalDateTime.now());
            userRepository.save(user);
            
            // Remove password from response / Rimuovi password dalla risposta
            user.setPassword(null);
            
            // Create response / Crea risposta
            Map<String, Object> response = new HashMap<>();
            response.put("user", user);
            response.put("message", "Accesso riuscito");
            response.put("timestamp", LocalDateTime.now());
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Errore durante l'accesso");
            error.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
    
    /**
     * Get user profile
     * Recupera profilo utente
     * 
     * @param userId User ID / ID utente
     * @return User profile / Profilo utente
     */
    @GetMapping("/profile/{userId}")
    @Operation(summary = "Get user profile", description = "Retrieve user profile by ID / Recupera profilo utente per ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Profile retrieved successfully / Profilo recuperato con successo"),
        @ApiResponse(responseCode = "404", description = "User not found / Utente non trovato"),
        @ApiResponse(responseCode = "500", description = "Internal server error / Errore interno del server")
    })
    public ResponseEntity<?> getProfile(
            @Parameter(description = "User ID / ID utente")
            @PathVariable Long userId) {
        
        try {
            Optional<User> userOpt = userRepository.findById(userId);
            
            if (userOpt.isEmpty()) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "Utente non trovato");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
            }
            
            User user = userOpt.get();
            user.setPassword(null); // Remove password / Rimuovi password
            
            return ResponseEntity.ok(user);
            
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Errore nel recupero del profilo");
            error.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
    
    /**
     * Update user profile
     * Aggiorna profilo utente
     * 
     * @param userId User ID / ID utente
     * @param userData Updated user data / Dati utente aggiornati
     * @return Updated user / Utente aggiornato
     */
    @PutMapping("/profile/{userId}")
    @Operation(summary = "Update user profile", description = "Update user profile information / Aggiorna informazioni profilo utente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Profile updated successfully / Profilo aggiornato con successo"),
        @ApiResponse(responseCode = "404", description = "User not found / Utente non trovato"),
        @ApiResponse(responseCode = "400", description = "Invalid data / Dati non validi"),
        @ApiResponse(responseCode = "500", description = "Internal server error / Errore interno del server")
    })
    public ResponseEntity<?> updateProfile(
            @Parameter(description = "User ID / ID utente")
            @PathVariable Long userId,
            @Parameter(description = "Updated user data / Dati utente aggiornati")
            @Valid @RequestBody User userData) {
        
        try {
            Optional<User> userOpt = userRepository.findById(userId);
            
            if (userOpt.isEmpty()) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "Utente non trovato");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
            }
            
            User user = userOpt.get();
            
            // Update allowed fields / Aggiorna campi consentiti
            user.setNome(userData.getNome());
            user.setCognome(userData.getCognome());
            user.setPhoneNumber(userData.getPhoneNumber());
            user.setDateOfBirth(userData.getDateOfBirth());
            user.setProfileImage(userData.getProfileImage());
            user.setUpdatedAt(LocalDateTime.now());
            
            // Save updated user / Salva utente aggiornato
            User updatedUser = userRepository.save(user);
            updatedUser.setPassword(null); // Remove password / Rimuovi password
            
            return ResponseEntity.ok(updatedUser);
            
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Errore nell'aggiornamento del profilo");
            error.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
    
    /**
     * Change user password
     * Cambia password utente
     * 
     * @param userId User ID / ID utente
     * @param passwordData Password change data / Dati cambio password
     * @return Success message / Messaggio di successo
     */
    @PutMapping("/password/{userId}")
    @Operation(summary = "Change password", description = "Change user password / Cambia password utente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Password changed successfully / Password cambiata con successo"),
        @ApiResponse(responseCode = "404", description = "User not found / Utente non trovato"),
        @ApiResponse(responseCode = "400", description = "Invalid current password / Password corrente non valida"),
        @ApiResponse(responseCode = "500", description = "Internal server error / Errore interno del server")
    })
    public ResponseEntity<?> changePassword(
            @Parameter(description = "User ID / ID utente")
            @PathVariable Long userId,
            @Parameter(description = "Password change data / Dati cambio password")
            @RequestBody Map<String, String> passwordData) {
        
        try {
            Optional<User> userOpt = userRepository.findById(userId);
            
            if (userOpt.isEmpty()) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "Utente non trovato");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
            }
            
            User user = userOpt.get();
            String currentPassword = passwordData.get("currentPassword");
            String newPassword = passwordData.get("newPassword");
            
            // Check current password / Controlla password corrente
            if (!user.getPassword().equals(currentPassword)) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "Password corrente non valida");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
            }
            
            // Update password / Aggiorna password
            user.setPassword(newPassword);
            user.setUpdatedAt(LocalDateTime.now());
            userRepository.save(user);
            
            Map<String, String> response = new HashMap<>();
            response.put("message", "Password cambiata con successo");
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Errore nel cambio password");
            error.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
}
