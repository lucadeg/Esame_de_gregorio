package com.esame.repository;

import com.esame.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * User Repository Interface
 * Interfaccia Repository Utente
 * 
 * Data access layer for User entity
 * Livello di accesso ai dati per l'entità Utente
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    /**
     * Find user by email
     * Trova utente per email
     * 
     * @param email User email / Email utente
     * @return Optional user / Utente opzionale
     */
    Optional<User> findByEmail(String email);
    
    /**
     * Find active users by email
     * Trova utenti attivi per email
     * 
     * @param email User email / Email utente
     * @return Optional active user / Utente attivo opzionale
     */
    @Query("SELECT u FROM User u WHERE u.email = :email AND u.isActive = true")
    Optional<User> findActiveByEmail(@Param("email") String email);
    
    /**
     * Find users by role
     * Trova utenti per ruolo
     * 
     * @param role User role / Ruolo utente
     * @return List of users / Lista utenti
     */
    List<User> findByRole(User.UserRole role);
    
    /**
     * Find users by subscription type
     * Trova utenti per tipo abbonamento
     * 
     * @param subscriptionType Subscription type / Tipo abbonamento
     * @return List of users / Lista utenti
     */
    List<User> findBySubscriptionType(User.SubscriptionType subscriptionType);
    
    /**
     * Find users with active subscriptions
     * Trova utenti con abbonamenti attivi
     * 
     * @return List of users with active subscriptions / Lista utenti con abbonamenti attivi
     */
    @Query("SELECT u FROM User u WHERE u.subscriptionType != 'FREE' AND u.subscriptionExpiresAt > :now")
    List<User> findUsersWithActiveSubscriptions(@Param("now") LocalDateTime now);
    
    /**
     * Find users with expired subscriptions
     * Trova utenti con abbonamenti scaduti
     * 
     * @return List of users with expired subscriptions / Lista utenti con abbonamenti scaduti
     */
    @Query("SELECT u FROM User u WHERE u.subscriptionType != 'FREE' AND u.subscriptionExpiresAt <= :now")
    List<User> findUsersWithExpiredSubscriptions(@Param("now") LocalDateTime now);
    
    /**
     * Find users created after date
     * Trova utenti creati dopo una data
     * 
     * @param date Creation date / Data di creazione
     * @return List of users / Lista utenti
     */
    List<User> findByCreatedAtAfter(LocalDateTime date);
    
    /**
     * Find users by name containing
     * Trova utenti per nome contenente
     * 
     * @param name Name to search / Nome da cercare
     * @return List of users / Lista utenti
     */
    @Query("SELECT u FROM User u WHERE LOWER(u.nome) LIKE LOWER(CONCAT('%', :name, '%')) OR LOWER(u.cognome) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<User> findByNameContaining(@Param("name") String name);
    
    /**
     * Count users by role
     * Conta utenti per ruolo
     * 
     * @param role User role / Ruolo utente
     * @return Number of users / Numero di utenti
     */
    long countByRole(User.UserRole role);
    
    /**
     * Count users by subscription type
     * Conta utenti per tipo abbonamento
     * 
     * @param subscriptionType Subscription type / Tipo abbonamento
     * @return Number of users / Numero di utenti
     */
    long countBySubscriptionType(User.SubscriptionType subscriptionType);
    
    /**
     * Find users with email verification pending
     * Trova utenti con verifica email in sospeso
     * 
     * @return List of users with unverified emails / Lista utenti con email non verificate
     */
    @Query("SELECT u FROM User u WHERE u.emailVerified = false")
    List<User> findUsersWithUnverifiedEmails();
    
    /**
     * Find users by last login date
     * Trova utenti per data ultimo accesso
     * 
     * @param date Last login date / Data ultimo accesso
     * @return List of users / Lista utenti
     */
    List<User> findByLastLoginAfter(LocalDateTime date);
    
    /**
     * Find users who never logged in
     * Trova utenti che non hanno mai effettuato l'accesso
     * 
     * @return List of users who never logged in / Lista utenti che non hanno mai effettuato l'accesso
     */
    @Query("SELECT u FROM User u WHERE u.lastLogin IS NULL")
    List<User> findUsersWhoNeverLoggedIn();
}
