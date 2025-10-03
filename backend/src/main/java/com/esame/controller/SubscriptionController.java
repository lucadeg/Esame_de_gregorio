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

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Subscription Controller
 * Controller Abbonamenti
 * 
 * REST API endpoints for subscription management
 * Endpoint API REST per gestione abbonamenti
 */
@RestController
@RequestMapping("/subscriptions")
@Tag(name = "Subscription Management", description = "API for managing user subscriptions / API per gestione abbonamenti utenti")
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:3000", "http://127.0.0.1:5173"})
public class SubscriptionController {
    
    @Autowired
    private UserRepository userRepository;
    
    /**
     * Get all subscription types
     * Recupera tutti i tipi di abbonamento
     * 
     * @return List of subscription types / Lista tipi abbonamento
     */
    @GetMapping("/types")
    @Operation(summary = "Get subscription types", description = "Retrieve all available subscription types / Recupera tutti i tipi di abbonamento disponibili")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Subscription types retrieved successfully / Tipi abbonamento recuperati con successo"),
        @ApiResponse(responseCode = "500", description = "Internal server error / Errore interno del server")
    })
    public ResponseEntity<?> getSubscriptionTypes() {
        try {
            User.SubscriptionType[] types = User.SubscriptionType.values();
            
            @SuppressWarnings("unchecked")
            Map<String, Object>[] subscriptionTypes = new Map[types.length];
            for (int i = 0; i < types.length; i++) {
                Map<String, Object> typeInfo = new HashMap<>();
                typeInfo.put("type", types[i].name());
                typeInfo.put("displayName", types[i].getDisplayName());
                typeInfo.put("price", types[i].getPrice());
                typeInfo.put("maxCourses", types[i].getMaxCourses());
                typeInfo.put("hasAdvancedFeatures", types[i].hasAdvancedFeatures());
                subscriptionTypes[i] = typeInfo;
            }
            
            return ResponseEntity.ok(subscriptionTypes);
            
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Errore nel recupero dei tipi di abbonamento");
            error.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
    
    /**
     * Get user subscription info
     * Recupera informazioni abbonamento utente
     * 
     * @param userId User ID / ID utente
     * @return User subscription info / Informazioni abbonamento utente
     */
    @GetMapping("/user/{userId}")
    @Operation(summary = "Get user subscription", description = "Retrieve user subscription information / Recupera informazioni abbonamento utente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Subscription info retrieved successfully / Informazioni abbonamento recuperate con successo"),
        @ApiResponse(responseCode = "404", description = "User not found / Utente non trovato"),
        @ApiResponse(responseCode = "500", description = "Internal server error / Errore interno del server")
    })
    public ResponseEntity<?> getUserSubscription(
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
            
            Map<String, Object> subscriptionInfo = new HashMap<>();
            subscriptionInfo.put("userId", user.getId());
            subscriptionInfo.put("subscriptionType", user.getSubscriptionType());
            subscriptionInfo.put("subscriptionExpiresAt", user.getSubscriptionExpiresAt());
            subscriptionInfo.put("isActive", user.isSubscriptionActive());
            subscriptionInfo.put("maxCourses", user.getSubscriptionType().getMaxCourses());
            subscriptionInfo.put("hasAdvancedFeatures", user.getSubscriptionType().hasAdvancedFeatures());
            
            // Calculate days remaining / Calcola giorni rimanenti
            if (user.getSubscriptionExpiresAt() != null) {
                long daysRemaining = java.time.temporal.ChronoUnit.DAYS.between(
                    LocalDateTime.now(), 
                    user.getSubscriptionExpiresAt()
                );
                subscriptionInfo.put("daysRemaining", Math.max(0, daysRemaining));
            } else {
                subscriptionInfo.put("daysRemaining", null);
            }
            
            return ResponseEntity.ok(subscriptionInfo);
            
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Errore nel recupero dell'abbonamento");
            error.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
    
    /**
     * Update user subscription
     * Aggiorna abbonamento utente
     * 
     * @param userId User ID / ID utente
     * @param subscriptionData Subscription data / Dati abbonamento
     * @return Updated subscription info / Informazioni abbonamento aggiornate
     */
    @PutMapping("/user/{userId}")
    @Operation(summary = "Update user subscription", description = "Update user subscription type and expiration / Aggiorna tipo abbonamento e scadenza utente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Subscription updated successfully / Abbonamento aggiornato con successo"),
        @ApiResponse(responseCode = "404", description = "User not found / Utente non trovato"),
        @ApiResponse(responseCode = "400", description = "Invalid subscription data / Dati abbonamento non validi"),
        @ApiResponse(responseCode = "500", description = "Internal server error / Errore interno del server")
    })
    public ResponseEntity<?> updateUserSubscription(
            @Parameter(description = "User ID / ID utente")
            @PathVariable Long userId,
            @Parameter(description = "Subscription data / Dati abbonamento")
            @RequestBody Map<String, Object> subscriptionData) {
        
        try {
            Optional<User> userOpt = userRepository.findById(userId);
            
            if (userOpt.isEmpty()) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "Utente non trovato");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
            }
            
            User user = userOpt.get();
            
            // Update subscription type / Aggiorna tipo abbonamento
            String subscriptionTypeStr = (String) subscriptionData.get("subscriptionType");
            if (subscriptionTypeStr != null) {
                try {
                    User.SubscriptionType newType = User.SubscriptionType.valueOf(subscriptionTypeStr.toUpperCase());
                    user.setSubscriptionType(newType);
                } catch (IllegalArgumentException e) {
                    Map<String, String> error = new HashMap<>();
                    error.put("error", "Tipo abbonamento non valido");
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
                }
            }
            
            // Update expiration date / Aggiorna data scadenza
            String expirationDateStr = (String) subscriptionData.get("expirationDate");
            if (expirationDateStr != null) {
                try {
                    LocalDateTime expirationDate = LocalDateTime.parse(expirationDateStr);
                    user.setSubscriptionExpiresAt(expirationDate);
                } catch (Exception e) {
                    Map<String, String> error = new HashMap<>();
                    error.put("error", "Formato data scadenza non valido");
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
                }
            }
            
            // Set expiration for paid subscriptions / Imposta scadenza per abbonamenti a pagamento
            if (user.getSubscriptionType() != User.SubscriptionType.FREE && user.getSubscriptionExpiresAt() == null) {
                user.setSubscriptionExpiresAt(LocalDateTime.now().plusMonths(1));
            }
            
            user.setUpdatedAt(LocalDateTime.now());
            User updatedUser = userRepository.save(user);
            
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Abbonamento aggiornato con successo");
            response.put("subscriptionType", updatedUser.getSubscriptionType());
            response.put("subscriptionExpiresAt", updatedUser.getSubscriptionExpiresAt());
            response.put("isActive", updatedUser.isSubscriptionActive());
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Errore nell'aggiornamento dell'abbonamento");
            error.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
    
    /**
     * Get subscription statistics
     * Recupera statistiche abbonamenti
     * 
     * @return Subscription statistics / Statistiche abbonamenti
     */
    @GetMapping("/statistics")
    @Operation(summary = "Get subscription statistics", description = "Retrieve subscription statistics / Recupera statistiche abbonamenti")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Statistics retrieved successfully / Statistiche recuperate con successo"),
        @ApiResponse(responseCode = "500", description = "Internal server error / Errore interno del server")
    })
    public ResponseEntity<?> getSubscriptionStatistics() {
        try {
            Map<String, Object> statistics = new HashMap<>();
            
            // Count users by subscription type / Conta utenti per tipo abbonamento
            for (User.SubscriptionType type : User.SubscriptionType.values()) {
                long count = userRepository.countBySubscriptionType(type);
                statistics.put(type.name().toLowerCase() + "Users", count);
            }
            
            // Count active subscriptions / Conta abbonamenti attivi
            List<User> activeSubscriptions = userRepository.findUsersWithActiveSubscriptions(LocalDateTime.now());
            statistics.put("activeSubscriptions", activeSubscriptions.size());
            
            // Count expired subscriptions / Conta abbonamenti scaduti
            List<User> expiredSubscriptions = userRepository.findUsersWithExpiredSubscriptions(LocalDateTime.now());
            statistics.put("expiredSubscriptions", expiredSubscriptions.size());
            
            // Total users / Totale utenti
            long totalUsers = userRepository.count();
            statistics.put("totalUsers", totalUsers);
            
            return ResponseEntity.ok(statistics);
            
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Errore nel recupero delle statistiche");
            error.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
    
    /**
     * Cancel user subscription
     * Cancella abbonamento utente
     * 
     * @param userId User ID / ID utente
     * @return Success message / Messaggio di successo
     */
    @DeleteMapping("/user/{userId}")
    @Operation(summary = "Cancel subscription", description = "Cancel user subscription and revert to free / Cancella abbonamento utente e ripristina gratuito")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Subscription cancelled successfully / Abbonamento cancellato con successo"),
        @ApiResponse(responseCode = "404", description = "User not found / Utente non trovato"),
        @ApiResponse(responseCode = "500", description = "Internal server error / Errore interno del server")
    })
    public ResponseEntity<?> cancelSubscription(
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
            
            // Revert to free subscription / Ripristina abbonamento gratuito
            user.setSubscriptionType(User.SubscriptionType.FREE);
            user.setSubscriptionExpiresAt(null);
            user.setUpdatedAt(LocalDateTime.now());
            
            userRepository.save(user);
            
            Map<String, String> response = new HashMap<>();
            response.put("message", "Abbonamento cancellato con successo");
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Errore nella cancellazione dell'abbonamento");
            error.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
}
