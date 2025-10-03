package com.esame.mapper;

import com.esame.model.User;
import com.esame.dto.UserDTO;
import com.esame.dto.SubscriptionDTO;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

/**
 * User Mapper
 * Mapper per Utente
 * 
 * Maps between User entity and DTO
 * Mappa tra entità Utente e DTO
 */
@Component
public class UserMapper {
    
    /**
     * Convert User entity to DTO
     * Converte entità Utente in DTO
     * 
     * @param user User entity / Entità Utente
     * @return User DTO / DTO Utente
     */
    public UserDTO toDTO(User user) {
        if (user == null) {
            return null;
        }
        
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setNome(user.getNome());
        dto.setCognome(user.getCognome());
        dto.setEmail(user.getEmail());
        dto.setPhoneNumber(user.getPhoneNumber());
        dto.setDateOfBirth(user.getDateOfBirth());
        dto.setProfileImage(user.getProfileImage());
        dto.setIsActive(user.getIsActive());
        dto.setEmailVerified(user.getEmailVerified());
        dto.setCreatedAt(user.getCreatedAt());
        dto.setUpdatedAt(user.getUpdatedAt());
        dto.setLastLogin(user.getLastLogin());
        
        // Set role and subscription as strings / Imposta ruolo e abbonamento come stringhe
        if (user.getRole() != null) {
            dto.setRole(user.getRole().name());
        }
        if (user.getSubscriptionType() != null) {
            dto.setSubscriptionType(user.getSubscriptionType().name());
        }
        
        dto.setSubscriptionExpiresAt(user.getSubscriptionExpiresAt());
        
        // Set additional fields / Imposta campi aggiuntivi
        dto.setFullName(user.getFullName());
        dto.setSubscriptionActive(user.isSubscriptionActive());
        
        // Calculate days remaining / Calcola giorni rimanenti
        if (user.getSubscriptionExpiresAt() != null) {
            long daysRemaining = ChronoUnit.DAYS.between(LocalDateTime.now(), user.getSubscriptionExpiresAt());
            dto.setDaysRemaining(Math.max(0, daysRemaining));
        }
        
        // Set subscription features / Imposta caratteristiche abbonamento
        if (user.getSubscriptionType() != null) {
            dto.setMaxCourses(user.getSubscriptionType().getMaxCourses());
            dto.setHasAdvancedFeatures(user.getSubscriptionType().hasAdvancedFeatures());
        }
        
        return dto;
    }
    
    /**
     * Convert User DTO to entity
     * Converte DTO Utente in entità
     * 
     * @param dto User DTO / DTO Utente
     * @return User entity / Entità Utente
     */
    public User toEntity(UserDTO dto) {
        if (dto == null) {
            return null;
        }
        
        User user = new User();
        user.setId(dto.getId());
        user.setNome(dto.getNome());
        user.setCognome(dto.getCognome());
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword());
        user.setPhoneNumber(dto.getPhoneNumber());
        user.setDateOfBirth(dto.getDateOfBirth());
        user.setProfileImage(dto.getProfileImage());
        user.setIsActive(dto.getIsActive());
        user.setEmailVerified(dto.getEmailVerified());
        user.setCreatedAt(dto.getCreatedAt());
        user.setUpdatedAt(dto.getUpdatedAt());
        user.setLastLogin(dto.getLastLogin());
        
        // Set role and subscription from strings / Imposta ruolo e abbonamento da stringhe
        if (dto.getRole() != null) {
            try {
                user.setRole(User.UserRole.valueOf(dto.getRole()));
            } catch (IllegalArgumentException e) {
                user.setRole(User.UserRole.STUDENT);
            }
        }
        
        if (dto.getSubscriptionType() != null) {
            try {
                user.setSubscriptionType(User.SubscriptionType.valueOf(dto.getSubscriptionType()));
            } catch (IllegalArgumentException e) {
                user.setSubscriptionType(User.SubscriptionType.FREE);
            }
        }
        
        user.setSubscriptionExpiresAt(dto.getSubscriptionExpiresAt());
        
        return user;
    }
    
    /**
     * Convert list of User entities to DTOs
     * Converte lista di entità Utente in DTO
     * 
     * @param users List of User entities / Lista di entità Utente
     * @return List of User DTOs / Lista di DTO Utente
     */
    public List<UserDTO> toDTOList(List<User> users) {
        if (users == null) {
            return null;
        }
        
        return users.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Update User entity with DTO data
     * Aggiorna entità Utente con dati DTO
     * 
     * @param user Existing User entity / Entità Utente esistente
     * @param dto User DTO with new data / DTO Utente con nuovi dati
     * @return Updated User entity / Entità Utente aggiornata
     */
    public User updateEntity(User user, UserDTO dto) {
        if (user == null || dto == null) {
            return user;
        }
        
        // Update only non-null fields / Aggiorna solo campi non null
        if (dto.getNome() != null) {
            user.setNome(dto.getNome());
        }
        if (dto.getCognome() != null) {
            user.setCognome(dto.getCognome());
        }
        if (dto.getPhoneNumber() != null) {
            user.setPhoneNumber(dto.getPhoneNumber());
        }
        if (dto.getDateOfBirth() != null) {
            user.setDateOfBirth(dto.getDateOfBirth());
        }
        if (dto.getProfileImage() != null) {
            user.setProfileImage(dto.getProfileImage());
        }
        if (dto.getIsActive() != null) {
            user.setIsActive(dto.getIsActive());
        }
        if (dto.getEmailVerified() != null) {
            user.setEmailVerified(dto.getEmailVerified());
        }
        
        // Update role if provided / Aggiorna ruolo se fornito
        if (dto.getRole() != null) {
            try {
                user.setRole(User.UserRole.valueOf(dto.getRole()));
            } catch (IllegalArgumentException e) {
                // Keep existing role if invalid / Mantieni ruolo esistente se non valido
            }
        }
        
        // Update subscription if provided / Aggiorna abbonamento se fornito
        if (dto.getSubscriptionType() != null) {
            try {
                user.setSubscriptionType(User.SubscriptionType.valueOf(dto.getSubscriptionType()));
            } catch (IllegalArgumentException e) {
                // Keep existing subscription if invalid / Mantieni abbonamento esistente se non valido
            }
        }
        
        if (dto.getSubscriptionExpiresAt() != null) {
            user.setSubscriptionExpiresAt(dto.getSubscriptionExpiresAt());
        }
        
        // Always update timestamp / Aggiorna sempre timestamp
        user.setUpdatedAt(LocalDateTime.now());
        
        return user;
    }
    
    /**
     * Convert User entity to Subscription DTO
     * Converte entità Utente in DTO Abbonamento
     * 
     * @param user User entity / Entità Utente
     * @return Subscription DTO / DTO Abbonamento
     */
    public SubscriptionDTO toSubscriptionDTO(User user) {
        if (user == null) {
            return null;
        }
        
        SubscriptionDTO dto = new SubscriptionDTO();
        dto.setUserId(user.getId());
        dto.setSubscriptionType(user.getSubscriptionType().name());
        dto.setSubscriptionExpiresAt(user.getSubscriptionExpiresAt());
        dto.setIsActive(user.isSubscriptionActive());
        dto.setMaxCourses(user.getSubscriptionType().getMaxCourses());
        dto.setHasAdvancedFeatures(user.getSubscriptionType().hasAdvancedFeatures());
        dto.setDisplayName(user.getSubscriptionType().getDisplayName());
        dto.setPrice(user.getSubscriptionType().getPrice());
        
        // Calculate days remaining / Calcola giorni rimanenti
        if (user.getSubscriptionExpiresAt() != null) {
            long daysRemaining = ChronoUnit.DAYS.between(LocalDateTime.now(), user.getSubscriptionExpiresAt());
            dto.setDaysRemaining(Math.max(0, daysRemaining));
        }
        
        return dto;
    }
}
