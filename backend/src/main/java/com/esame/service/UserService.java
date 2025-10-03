package com.esame.service;

import com.esame.dto.UserDTO;
import com.esame.dto.SubscriptionDTO;
import com.esame.dto.PasswordChangeDTO;
import com.esame.exception.BusinessLogicException;
import com.esame.exception.ResourceNotFoundException;
import com.esame.mapper.UserMapper;
import com.esame.model.User;
import com.esame.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * User Service
 * Servizio Utente
 * 
 * Business logic for user management
 * Logica di business per la gestione degli utenti
 */
@Service
@Transactional
public class UserService implements UserDetailsService {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private UserMapper userMapper;
    
    @Autowired
    private PasswordService passwordService;
    
    /**
     * Load user by username for Spring Security
     * Carica utente per username per Spring Security
     * 
     * @param username Username / Nome utente
     * @return UserDetails / Dettagli utente
     * @throws UsernameNotFoundException if user not found / se utente non trovato
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username)
            .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + username));
        
        return org.springframework.security.core.userdetails.User.builder()
            .username(user.getEmail())
            .password(user.getPassword())
            .authorities(Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + user.getRole().name())))
            .accountExpired(false)
            .accountLocked(!user.getIsActive())
            .credentialsExpired(false)
            .disabled(!user.getIsActive())
            .build();
    }
    
    /**
     * Get all users
     * Recupera tutti gli utenti
     * 
     * @return List of user DTOs / Lista DTO utenti
     */
    public List<UserDTO> getAllUsers() {
        List<User> users = userRepository.findAll();
        return userMapper.toDTOList(users);
    }
    
    /**
     * Get user by ID
     * Recupera utente per ID
     * 
     * @param id User ID / ID utente
     * @return User DTO / DTO Utente
     * @throws ResourceNotFoundException if user not found / se utente non trovato
     */
    public UserDTO getUserById(Long id) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
        
        return userMapper.toDTO(user);
    }
    
    /**
     * Get user by email
     * Recupera utente per email
     * 
     * @param email User email / Email utente
     * @return User DTO / DTO Utente
     * @throws ResourceNotFoundException if user not found / se utente non trovato
     */
    public UserDTO getUserByEmail(String email) {
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));
        
        return userMapper.toDTO(user);
    }
    
    /**
     * Create new user
     * Crea nuovo utente
     * 
     * @param userDTO User data / Dati utente
     * @return Created user DTO / DTO utente creato
     * @throws BusinessLogicException if email already exists / se email già esistente
     */
    public UserDTO createUser(UserDTO userDTO) {
        // Validate user data / Valida dati utente
        validateUserData(userDTO);
        
        // Check if email already exists / Controlla se email esiste già
        if (userRepository.findByEmail(userDTO.getEmail()).isPresent()) {
            throw new BusinessLogicException(
                "EMAIL_ALREADY_EXISTS", 
                "User with this email already exists / Utente con questa email già esistente"
            );
        }
        
        // Convert DTO to entity / Converte DTO in entità
        User user = userMapper.toEntity(userDTO);
        
        // Encode password / Codifica password
        if (user.getPassword() != null) {
            user.setPassword(passwordService.encodePassword(user.getPassword()));
        }
        
        // Set default values / Imposta valori di default
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        user.setIsActive(true);
        user.setEmailVerified(false);
        
        if (user.getRole() == null) {
            user.setRole(User.UserRole.STUDENT);
        }
        
        if (user.getSubscriptionType() == null) {
            user.setSubscriptionType(User.SubscriptionType.FREE);
        }
        
        // Save user / Salva utente
        User savedUser = userRepository.save(user);
        
        return userMapper.toDTO(savedUser);
    }
    
    /**
     * Update user
     * Aggiorna utente
     * 
     * @param id User ID / ID utente
     * @param userDTO Updated user data / Dati utente aggiornati
     * @return Updated user DTO / DTO utente aggiornato
     * @throws ResourceNotFoundException if user not found / se utente non trovato
     * @throws BusinessLogicException if email already exists / se email già esistente
     */
    public UserDTO updateUser(Long id, UserDTO userDTO) {
        // Find existing user / Trova utente esistente
        User existingUser = userRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
        
        // Validate user data / Valida dati utente
        validateUserData(userDTO);
        
        // Check if email is being changed and already exists / Controlla se email viene cambiata e già esistente
        if (!existingUser.getEmail().equals(userDTO.getEmail()) && 
            userRepository.findByEmail(userDTO.getEmail()).isPresent()) {
            throw new BusinessLogicException(
                "EMAIL_ALREADY_EXISTS", 
                "User with this email already exists / Utente con questa email già esistente"
            );
        }
        
        // Update user / Aggiorna utente
        User updatedUser = userMapper.updateEntity(existingUser, userDTO);
        
        // Save updated user / Salva utente aggiornato
        User savedUser = userRepository.save(updatedUser);
        
        return userMapper.toDTO(savedUser);
    }
    
    /**
     * Delete user
     * Elimina utente
     * 
     * @param id User ID / ID utente
     * @throws ResourceNotFoundException if user not found / se utente non trovato
     */
    public void deleteUser(Long id) {
        // Check if user exists / Controlla se utente esiste
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("User", "id", id);
        }
        
        // Delete user / Elimina utente
        userRepository.deleteById(id);
    }
    
    /**
     * Authenticate user
     * Autentica utente
     * 
     * @param email User email / Email utente
     * @param password User password / Password utente
     * @return User DTO if authentication successful / DTO Utente se autenticazione riuscita
     * @throws BusinessLogicException if authentication fails / se autenticazione fallisce
     */
    public UserDTO authenticateUser(String email, String password) {
        // Find user by email / Trova utente per email
        Optional<User> userOpt = userRepository.findActiveByEmail(email);
        
        if (userOpt.isEmpty()) {
            throw new BusinessLogicException(
                "INVALID_CREDENTIALS", 
                "Invalid email or password / Email o password non validi"
            );
        }
        
        User user = userOpt.get();
        
        // Check password using password encoder / Controlla password usando codificatore password
        if (!passwordService.matches(password, user.getPassword())) {
            throw new BusinessLogicException(
                "INVALID_CREDENTIALS", 
                "Invalid email or password / Email o password non validi"
            );
        }
        
        // Check if user is active / Controlla se utente è attivo
        if (!user.getIsActive()) {
            throw new BusinessLogicException(
                "ACCOUNT_INACTIVE", 
                "Account is inactive / Account inattivo"
            );
        }
        
        // Update last login / Aggiorna ultimo accesso
        user.setLastLogin(LocalDateTime.now());
        userRepository.save(user);
        
        return userMapper.toDTO(user);
    }
    
    /**
     * Change user password
     * Cambia password utente
     * 
     * @param id User ID / ID utente
     * @param passwordChangeDTO Password change data / Dati cambio password
     * @throws ResourceNotFoundException if user not found / se utente non trovato
     * @throws BusinessLogicException if current password is wrong / se password corrente è sbagliata
     */
    public void changePassword(Long id, PasswordChangeDTO passwordChangeDTO) {
        // Find existing user / Trova utente esistente
        User user = userRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
        
        // Validate password change data / Valida dati cambio password
        validatePasswordChange(passwordChangeDTO);
        
        // Check current password / Controlla password corrente
        if (!passwordService.matches(passwordChangeDTO.getCurrentPassword(), user.getPassword())) {
            throw new BusinessLogicException(
                "INVALID_CURRENT_PASSWORD", 
                "Current password is incorrect / Password corrente non corretta"
            );
        }
        
        // Update password with encoding / Aggiorna password con codifica
        user.setPassword(passwordService.encodePassword(passwordChangeDTO.getNewPassword()));
        user.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user);
    }
    
    /**
     * Get user subscription
     * Recupera abbonamento utente
     * 
     * @param id User ID / ID utente
     * @return Subscription DTO / DTO Abbonamento
     * @throws ResourceNotFoundException if user not found / se utente non trovato
     */
    public SubscriptionDTO getUserSubscription(Long id) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
        
        return userMapper.toSubscriptionDTO(user);
    }
    
    /**
     * Update user subscription
     * Aggiorna abbonamento utente
     * 
     * @param id User ID / ID utente
     * @param subscriptionDTO Subscription data / Dati abbonamento
     * @return Updated subscription DTO / DTO abbonamento aggiornato
     * @throws ResourceNotFoundException if user not found / se utente non trovato
     */
    public SubscriptionDTO updateUserSubscription(Long id, SubscriptionDTO subscriptionDTO) {
        // Find existing user / Trova utente esistente
        User user = userRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
        
        // Update subscription type / Aggiorna tipo abbonamento
        if (subscriptionDTO.getSubscriptionType() != null) {
            try {
                user.setSubscriptionType(User.SubscriptionType.valueOf(subscriptionDTO.getSubscriptionType()));
            } catch (IllegalArgumentException e) {
                throw new BusinessLogicException(
                    "INVALID_SUBSCRIPTION_TYPE", 
                    "Invalid subscription type / Tipo abbonamento non valido"
                );
            }
        }
        
        // Update expiration date / Aggiorna data scadenza
        if (subscriptionDTO.getSubscriptionExpiresAt() != null) {
            user.setSubscriptionExpiresAt(subscriptionDTO.getSubscriptionExpiresAt());
        }
        
        // Set expiration for paid subscriptions / Imposta scadenza per abbonamenti a pagamento
        if (user.getSubscriptionType() != User.SubscriptionType.FREE && user.getSubscriptionExpiresAt() == null) {
            user.setSubscriptionExpiresAt(LocalDateTime.now().plusMonths(1));
        }
        
        user.setUpdatedAt(LocalDateTime.now());
        User updatedUser = userRepository.save(user);
        
        return userMapper.toSubscriptionDTO(updatedUser);
    }
    
    /**
     * Validate user data
     * Valida dati utente
     * 
     * @param userDTO User DTO / DTO Utente
     * @throws BusinessLogicException if validation fails / se validazione fallisce
     */
    private void validateUserData(UserDTO userDTO) {
        // Check required fields / Controlla campi obbligatori
        if (userDTO.getNome() == null || userDTO.getNome().trim().isEmpty()) {
            throw new BusinessLogicException(
                "MISSING_NAME", 
                "Name is required / Nome è obbligatorio"
            );
        }
        
        if (userDTO.getCognome() == null || userDTO.getCognome().trim().isEmpty()) {
            throw new BusinessLogicException(
                "MISSING_SURNAME", 
                "Surname is required / Cognome è obbligatorio"
            );
        }
        
        if (userDTO.getEmail() == null || userDTO.getEmail().trim().isEmpty()) {
            throw new BusinessLogicException(
                "MISSING_EMAIL", 
                "Email is required / Email è obbligatoria"
            );
        }
        
        // Validate email format / Valida formato email
        if (!userDTO.getEmail().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            throw new BusinessLogicException(
                "INVALID_EMAIL_FORMAT", 
                "Invalid email format / Formato email non valido"
            );
        }
        
        // Validate password if provided / Valida password se fornita
        if (userDTO.getPassword() != null && userDTO.getPassword().length() < 6) {
            throw new BusinessLogicException(
                "INVALID_PASSWORD_LENGTH", 
                "Password must be at least 6 characters / Password deve essere di almeno 6 caratteri"
            );
        }
    }
    
    /**
     * Validate password change data
     * Valida dati cambio password
     * 
     * @param passwordChangeDTO Password change DTO / DTO cambio password
     * @throws BusinessLogicException if validation fails / se validazione fallisce
     */
    private void validatePasswordChange(PasswordChangeDTO passwordChangeDTO) {
        // Check required fields / Controlla campi obbligatori
        if (passwordChangeDTO.getCurrentPassword() == null || passwordChangeDTO.getCurrentPassword().trim().isEmpty()) {
            throw new BusinessLogicException(
                "MISSING_CURRENT_PASSWORD", 
                "Current password is required / Password corrente è obbligatoria"
            );
        }
        
        if (passwordChangeDTO.getNewPassword() == null || passwordChangeDTO.getNewPassword().trim().isEmpty()) {
            throw new BusinessLogicException(
                "MISSING_NEW_PASSWORD", 
                "New password is required / Nuova password è obbligatoria"
            );
        }
        
        if (passwordChangeDTO.getConfirmPassword() == null || passwordChangeDTO.getConfirmPassword().trim().isEmpty()) {
            throw new BusinessLogicException(
                "MISSING_CONFIRM_PASSWORD", 
                "Password confirmation is required / Conferma password è obbligatoria"
            );
        }
        
        // Check password length / Controlla lunghezza password
        if (passwordChangeDTO.getNewPassword().length() < 6) {
            throw new BusinessLogicException(
                "INVALID_PASSWORD_LENGTH", 
                "New password must be at least 6 characters / Nuova password deve essere di almeno 6 caratteri"
            );
        }
        
        // Check password match / Controlla corrispondenza password
        if (!passwordChangeDTO.getNewPassword().equals(passwordChangeDTO.getConfirmPassword())) {
            throw new BusinessLogicException(
                "PASSWORD_MISMATCH", 
                "New password and confirmation do not match / Nuova password e conferma non corrispondono"
            );
        }
        
        // Check if new password is different from current / Controlla se nuova password è diversa da corrente
        if (passwordChangeDTO.getNewPassword().equals(passwordChangeDTO.getCurrentPassword())) {
            throw new BusinessLogicException(
                "SAME_PASSWORD", 
                "New password must be different from current password / Nuova password deve essere diversa da quella corrente"
            );
        }
    }
}
