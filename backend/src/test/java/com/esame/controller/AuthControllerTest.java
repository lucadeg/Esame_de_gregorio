package com.esame.controller;

import com.esame.model.User;
import com.esame.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

/**
 * Authentication Controller Integration Tests
 * Test di Integrazione Controller Autenticazione
 * 
 * Comprehensive integration tests for authentication endpoints
 * Test di integrazione completi per endpoint autenticazione
 */
@WebMvcTest(AuthController.class)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private User testUser;
    private User newUser;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setNome("Mario");
        testUser.setCognome("Rossi");
        testUser.setEmail("mario.rossi@email.com");
        testUser.setPassword("password123");
        testUser.setIsActive(true);
        testUser.setEmailVerified(true);
        testUser.setRole(User.UserRole.STUDENT);
        testUser.setSubscriptionType(User.SubscriptionType.FREE);

        newUser = new User();
        newUser.setNome("Giulia");
        newUser.setCognome("Bianchi");
        newUser.setEmail("giulia.bianchi@email.com");
        newUser.setPassword("password456");
    }

    @Test
    @DisplayName("Test User Registration - Success")
    void testUserRegistrationSuccess() throws Exception {
        // Mock repository behavior
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        // Create registration request
        String registrationJson = objectMapper.writeValueAsString(newUser);

        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(registrationJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nome").value("Mario"))
                .andExpect(jsonPath("$.cognome").value("Rossi"))
                .andExpect(jsonPath("$.email").value("mario.rossi@email.com"))
                .andExpect(jsonPath("$.password").doesNotExist()) // Password should not be returned
                .andExpect(jsonPath("$.role").value("STUDENT"))
                .andExpect(jsonPath("$.subscriptionType").value("FREE"));

        verify(userRepository, times(1)).findByEmail(anyString());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("Test User Registration - Email Already Exists")
    void testUserRegistrationEmailExists() throws Exception {
        // Mock repository behavior - email already exists
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(testUser));

        String registrationJson = objectMapper.writeValueAsString(newUser);

        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(registrationJson))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.error").value("Email già registrata"))
                .andExpect(jsonPath("$.message").value("Un utente con questa email esiste già"));

        verify(userRepository, times(1)).findByEmail(anyString());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("Test User Registration - Invalid Data")
    void testUserRegistrationInvalidData() throws Exception {
        // Create invalid user data
        User invalidUser = new User();
        invalidUser.setNome(""); // Empty name
        invalidUser.setEmail("invalid-email"); // Invalid email format
        invalidUser.setPassword("123"); // Password too short

        String registrationJson = objectMapper.writeValueAsString(invalidUser);

        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(registrationJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Test User Login - Success")
    void testUserLoginSuccess() throws Exception {
        // Mock repository behavior
        when(userRepository.findActiveByEmail(anyString())).thenReturn(Optional.of(testUser));
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        // Create login request
        String loginJson = "{\"email\":\"mario.rossi@email.com\",\"password\":\"password123\"}";

        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(loginJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.user.nome").value("Mario"))
                .andExpect(jsonPath("$.user.email").value("mario.rossi@email.com"))
                .andExpect(jsonPath("$.user.password").doesNotExist()) // Password should not be returned
                .andExpect(jsonPath("$.message").value("Accesso riuscito"))
                .andExpect(jsonPath("$.timestamp").exists());

        verify(userRepository, times(1)).findActiveByEmail(anyString());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("Test User Login - Invalid Credentials")
    void testUserLoginInvalidCredentials() throws Exception {
        // Mock repository behavior - user not found
        when(userRepository.findActiveByEmail(anyString())).thenReturn(Optional.empty());

        String loginJson = "{\"email\":\"nonexistent@email.com\",\"password\":\"wrongpassword\"}";

        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(loginJson))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error").value("Credenziali non valide"));

        verify(userRepository, times(1)).findActiveByEmail(anyString());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("Test User Login - Wrong Password")
    void testUserLoginWrongPassword() throws Exception {
        // Mock repository behavior
        when(userRepository.findActiveByEmail(anyString())).thenReturn(Optional.of(testUser));

        String loginJson = "{\"email\":\"mario.rossi@email.com\",\"password\":\"wrongpassword\"}";

        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(loginJson))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error").value("Credenziali non valide"));

        verify(userRepository, times(1)).findActiveByEmail(anyString());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("Test User Login - Missing Credentials")
    void testUserLoginMissingCredentials() throws Exception {
        String loginJson = "{\"email\":\"mario.rossi@email.com\"}"; // Missing password

        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(loginJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Email e password sono obbligatori"));
    }

    @Test
    @DisplayName("Test Get User Profile - Success")
    void testGetUserProfileSuccess() throws Exception {
        // Mock repository behavior
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));

        mockMvc.perform(get("/auth/profile/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nome").value("Mario"))
                .andExpect(jsonPath("$.cognome").value("Rossi"))
                .andExpect(jsonPath("$.email").value("mario.rossi@email.com"))
                .andExpect(jsonPath("$.password").doesNotExist()); // Password should not be returned

        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Test Get User Profile - User Not Found")
    void testGetUserProfileNotFound() throws Exception {
        // Mock repository behavior - user not found
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/auth/profile/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Utente non trovato"));

        verify(userRepository, times(1)).findById(999L);
    }

    @Test
    @DisplayName("Test Update User Profile - Success")
    void testUpdateUserProfileSuccess() throws Exception {
        // Mock repository behavior
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        // Create update request
        User updateData = new User();
        updateData.setNome("Mario Updated");
        updateData.setCognome("Rossi Updated");
        updateData.setPhoneNumber("+39 987 654 3210");

        String updateJson = objectMapper.writeValueAsString(updateData);

        mockMvc.perform(put("/auth/profile/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(updateJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Mario"))
                .andExpect(jsonPath("$.cognome").value("Rossi"))
                .andExpect(jsonPath("$.password").doesNotExist()); // Password should not be returned

        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("Test Update User Profile - User Not Found")
    void testUpdateUserProfileNotFound() throws Exception {
        // Mock repository behavior - user not found
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        User updateData = new User();
        updateData.setNome("Updated Name");

        String updateJson = objectMapper.writeValueAsString(updateData);

        mockMvc.perform(put("/auth/profile/999")
                .contentType(MediaType.APPLICATION_JSON)
                .content(updateJson))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Utente non trovato"));

        verify(userRepository, times(1)).findById(999L);
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("Test Change Password - Success")
    void testChangePasswordSuccess() throws Exception {
        // Mock repository behavior
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        String passwordJson = "{\"currentPassword\":\"password123\",\"newPassword\":\"newpassword456\"}";

        mockMvc.perform(put("/auth/password/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(passwordJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Password cambiata con successo"));

        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("Test Change Password - Wrong Current Password")
    void testChangePasswordWrongCurrent() throws Exception {
        // Mock repository behavior
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));

        String passwordJson = "{\"currentPassword\":\"wrongpassword\",\"newPassword\":\"newpassword456\"}";

        mockMvc.perform(put("/auth/password/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(passwordJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Password corrente non valida"));

        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("Test Change Password - User Not Found")
    void testChangePasswordUserNotFound() throws Exception {
        // Mock repository behavior - user not found
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        String passwordJson = "{\"currentPassword\":\"password123\",\"newPassword\":\"newpassword456\"}";

        mockMvc.perform(put("/auth/password/999")
                .contentType(MediaType.APPLICATION_JSON)
                .content(passwordJson))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Utente non trovato"));

        verify(userRepository, times(1)).findById(999L);
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("Test CORS Headers")
    void testCORSHeaders() throws Exception {
        String registrationJson = objectMapper.writeValueAsString(newUser);

        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(registrationJson)
                .header("Origin", "http://localhost:5173"))
                .andExpect(status().isCreated())
                .andExpect(header().string("Access-Control-Allow-Origin", "http://localhost:5173"));
    }
}
