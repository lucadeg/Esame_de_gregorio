package com.esame.controller;

import com.esame.dto.ApiResponse;
import com.esame.dto.LoginRequestDTO;
import com.esame.dto.PasswordChangeDTO;
import com.esame.dto.UserDTO;
import com.esame.security.JwtTokenProvider;
import com.esame.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Enhanced Authentication Controller
 * Controller Autenticazione Potenziato
 * 
 * REST API endpoints for enhanced authentication with JWT
 * Endpoint API REST per autenticazione potenziata con JWT
 */
@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "Enhanced Authentication", description = "Enhanced API for user authentication / API potenziata per autenticazione utenti")
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:3000", "http://127.0.0.1:5173"})
public class EnhancedAuthController {
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    
    @Autowired
    private AuthenticationManager authenticationManager;
    
    /**
     * User registration
     * Registrazione utente
     * 
     * @param userDTO User data / Dati utente
     * @return Created user DTO / DTO utente creato
     */
    @PostMapping("/register")
    @Operation(summary = "Register new user", description = "Create a new user account / Crea un nuovo account utente")
    public ResponseEntity<ApiResponse<UserDTO>> register(
            @Parameter(description = "User data / Dati utente")
            @Valid @RequestBody UserDTO userDTO) {
        
        try {
            UserDTO createdUser = userService.createUser(userDTO);
            // Remove password from response / Rimuovi password dalla risposta
            createdUser.setPassword(null);
            
            return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("User registered successfully / Utente registrato con successo", createdUser));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error("Error registering user / Errore nella registrazione utente: " + e.getMessage()));
        }
    }
    
    /**
     * User login with JWT
     * Accesso utente con JWT
     * 
     * @param loginRequest Login credentials / Credenziali di accesso
     * @return JWT token and user info / Token JWT e informazioni utente
     */
    @PostMapping("/login")
    @Operation(summary = "User login", description = "Authenticate user and return JWT token / Autentica utente e restituisce token JWT")
    public ResponseEntity<ApiResponse<Map<String, Object>>> login(
            @Parameter(description = "Login credentials / Credenziali di accesso")
            @Valid @RequestBody LoginRequestDTO loginRequest) {
        
        try {
            System.out.println("Attempting login for email: " + loginRequest.getEmail());
            
            // Authenticate user / Autentica utente
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    loginRequest.getEmail(),
                    loginRequest.getPassword()
                )
            );
            
            System.out.println("Authentication successful for: " + loginRequest.getEmail());
            
            SecurityContextHolder.getContext().setAuthentication(authentication);
            
            // Generate JWT token / Genera token JWT
            String jwt = jwtTokenProvider.generateToken(authentication);
            String refreshToken = jwtTokenProvider.generateRefreshToken(loginRequest.getEmail());
            
            // Get user details / Recupera dettagli utente
            UserDTO user = userService.getUserByEmail(loginRequest.getEmail());
            user.setPassword(null); // Remove password / Rimuovi password
            
            // Create response / Crea risposta
            Map<String, Object> response = new HashMap<>();
            response.put("user", user);
            response.put("accessToken", jwt);
            response.put("refreshToken", refreshToken);
            response.put("tokenType", "Bearer");
            response.put("expiresIn", jwtTokenProvider.getJwtExpirationInMs());
            response.put("timestamp", LocalDateTime.now());
            
            return ResponseEntity.ok(ApiResponse.success("Login successful / Accesso riuscito", response));
        } catch (Exception e) {
            System.out.println("Login failed for: " + loginRequest.getEmail() + " - Error: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(ApiResponse.error("Invalid credentials / Credenziali non valide: " + e.getMessage()));
        }
    }
    
    /**
     * Refresh JWT token
     * Aggiorna token JWT
     * 
     * @param refreshToken Refresh token / Token di refresh
     * @return New JWT token / Nuovo token JWT
     */
    @PostMapping("/refresh")
    @Operation(summary = "Refresh token", description = "Refresh JWT token using refresh token / Aggiorna token JWT usando refresh token")
    public ResponseEntity<ApiResponse<Map<String, Object>>> refreshToken(
            @Parameter(description = "Refresh token / Token di refresh")
            @RequestBody Map<String, String> request) {
        
        try {
            String refreshToken = request.get("refreshToken");
            
            if (refreshToken == null || !jwtTokenProvider.validateToken(refreshToken)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error("Invalid refresh token / Refresh token non valido"));
            }
            
            String username = jwtTokenProvider.getUsernameFromJWT(refreshToken);
            String newJwt = jwtTokenProvider.generateTokenFromUsername(username);
            String newRefreshToken = jwtTokenProvider.generateRefreshToken(username);
            
            Map<String, Object> response = new HashMap<>();
            response.put("accessToken", newJwt);
            response.put("refreshToken", newRefreshToken);
            response.put("tokenType", "Bearer");
            response.put("expiresIn", jwtTokenProvider.getJwtExpirationInMs());
            response.put("timestamp", LocalDateTime.now());
            
            return ResponseEntity.ok(ApiResponse.success("Token refreshed successfully / Token aggiornato con successo", response));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(ApiResponse.error("Error refreshing token / Errore nell'aggiornamento del token: " + e.getMessage()));
        }
    }
    
    /**
     * Get current user profile
     * Recupera profilo utente corrente
     * 
     * @return Current user profile / Profilo utente corrente
     */
    @GetMapping("/profile")
    @Operation(summary = "Get current user profile", description = "Retrieve current user profile / Recupera profilo utente corrente")
    public ResponseEntity<ApiResponse<UserDTO>> getCurrentUserProfile() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String email = authentication.getName();
            
            UserDTO user = userService.getUserByEmail(email);
            user.setPassword(null); // Remove password / Rimuovi password
            
            return ResponseEntity.ok(ApiResponse.success("Profile retrieved successfully / Profilo recuperato con successo", user));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(ApiResponse.error("Error retrieving profile / Errore nel recupero del profilo: " + e.getMessage()));
        }
    }
    
    /**
     * Update current user profile
     * Aggiorna profilo utente corrente
     * 
     * @param userDTO Updated user data / Dati utente aggiornati
     * @return Updated user profile / Profilo utente aggiornato
     */
    @PutMapping("/profile")
    @Operation(summary = "Update current user profile", description = "Update current user profile / Aggiorna profilo utente corrente")
    public ResponseEntity<ApiResponse<UserDTO>> updateCurrentUserProfile(
            @Parameter(description = "Updated user data / Dati utente aggiornati")
            @Valid @RequestBody UserDTO userDTO) {
        
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String email = authentication.getName();
            
            UserDTO currentUser = userService.getUserByEmail(email);
            UserDTO updatedUser = userService.updateUser(currentUser.getId(), userDTO);
            updatedUser.setPassword(null); // Remove password / Rimuovi password
            
            return ResponseEntity.ok(ApiResponse.success("Profile updated successfully / Profilo aggiornato con successo", updatedUser));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error("Error updating profile / Errore nell'aggiornamento del profilo: " + e.getMessage()));
        }
    }
    
    /**
     * Change user password
     * Cambia password utente
     * 
     * @param passwordData Password change data / Dati cambio password
     * @return Success message / Messaggio di successo
     */
    @PutMapping("/password")
    @Operation(summary = "Change password", description = "Change current user password / Cambia password utente corrente")
    public ResponseEntity<ApiResponse<String>> changePassword(
            @Parameter(description = "Password change data / Dati cambio password")
            @RequestBody Map<String, String> passwordData) {
        
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String email = authentication.getName();
            
            String currentPassword = passwordData.get("currentPassword");
            String newPassword = passwordData.get("newPassword");
            
            if (currentPassword == null || newPassword == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error("Current password and new password are required / Password corrente e nuova password sono obbligatorie"));
            }
            
            UserDTO currentUser = userService.getUserByEmail(email);
            PasswordChangeDTO passwordChangeDTO = new PasswordChangeDTO();
            passwordChangeDTO.setCurrentPassword(currentPassword);
            passwordChangeDTO.setNewPassword(newPassword);
            userService.changePassword(currentUser.getId(), passwordChangeDTO);
            
            return ResponseEntity.ok(ApiResponse.success("Password changed successfully / Password cambiata con successo", "Password updated successfully / Password aggiornata con successo"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error("Error changing password / Errore nel cambio password: " + e.getMessage()));
        }
    }
    
    /**
     * Logout user
     * Disconnetti utente
     * 
     * @return Success message / Messaggio di successo
     */
    @PostMapping("/logout")
    @Operation(summary = "Logout user", description = "Logout current user / Disconnetti utente corrente")
    public ResponseEntity<ApiResponse<String>> logout() {
        try {
            SecurityContextHolder.clearContext();
            return ResponseEntity.ok(ApiResponse.success("Logout successful / Disconnessione riuscita", "User logged out successfully / Utente disconnesso con successo"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("Error during logout / Errore durante la disconnessione: " + e.getMessage()));
        }
    }
}
