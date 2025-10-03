package com.esame.controller;

import com.esame.model.User;
import com.esame.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

/**
 * Subscription Controller Integration Tests
 * Test di Integrazione Controller Abbonamenti
 * 
 * Comprehensive integration tests for subscription management endpoints
 * Test di integrazione completi per endpoint gestione abbonamenti
 */
@WebMvcTest(SubscriptionController.class)
class SubscriptionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserRepository userRepository;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setNome("Mario");
        testUser.setCognome("Rossi");
        testUser.setEmail("mario.rossi@email.com");
        testUser.setRole(User.UserRole.STUDENT);
        testUser.setSubscriptionType(User.SubscriptionType.BASIC);
        testUser.setSubscriptionExpiresAt(LocalDateTime.now().plusMonths(1));
        testUser.setIsActive(true);

    }

    @Test
    @DisplayName("Test Get Subscription Types - Success")
    void testGetSubscriptionTypesSuccess() throws Exception {
        mockMvc.perform(get("/subscriptions/types"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].type").value("FREE"))
                .andExpect(jsonPath("$[0].displayName").value("Gratuito"))
                .andExpect(jsonPath("$[0].price").value(0.0))
                .andExpect(jsonPath("$[0].maxCourses").value(3))
                .andExpect(jsonPath("$[0].hasAdvancedFeatures").value(false))
                .andExpect(jsonPath("$[1].type").value("BASIC"))
                .andExpect(jsonPath("$[1].displayName").value("Base"))
                .andExpect(jsonPath("$[1].price").value(9.99))
                .andExpect(jsonPath("$[1].maxCourses").value(10))
                .andExpect(jsonPath("$[1].hasAdvancedFeatures").value(true))
                .andExpect(jsonPath("$[2].type").value("PREMIUM"))
                .andExpect(jsonPath("$[2].displayName").value("Premium"))
                .andExpect(jsonPath("$[2].price").value(19.99))
                .andExpect(jsonPath("$[2].maxCourses").value(25))
                .andExpect(jsonPath("$[2].hasAdvancedFeatures").value(true))
                .andExpect(jsonPath("$[3].type").value("ENTERPRISE"))
                .andExpect(jsonPath("$[3].displayName").value("Enterprise"))
                .andExpect(jsonPath("$[3].price").value(49.99))
                .andExpect(jsonPath("$[3].maxCourses").value(100))
                .andExpect(jsonPath("$[3].hasAdvancedFeatures").value(true));
    }

    @Test
    @DisplayName("Test Get User Subscription - Success")
    void testGetUserSubscriptionSuccess() throws Exception {
        // Mock repository behavior
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));

        mockMvc.perform(get("/subscriptions/user/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(1))
                .andExpect(jsonPath("$.subscriptionType").value("BASIC"))
                .andExpect(jsonPath("$.isActive").value(true))
                .andExpect(jsonPath("$.maxCourses").value(10))
                .andExpect(jsonPath("$.hasAdvancedFeatures").value(true))
                .andExpect(jsonPath("$.daysRemaining").exists());

        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Test Get User Subscription - User Not Found")
    void testGetUserSubscriptionNotFound() throws Exception {
        // Mock repository behavior - user not found
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/subscriptions/user/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Utente non trovato"));

        verify(userRepository, times(1)).findById(999L);
    }

    @Test
    @DisplayName("Test Get User Subscription - Free Plan")
    void testGetUserSubscriptionFreePlan() throws Exception {
        // Create free user
        User freeUser = new User();
        freeUser.setId(2L);
        freeUser.setSubscriptionType(User.SubscriptionType.FREE);
        freeUser.setSubscriptionExpiresAt(null);

        when(userRepository.findById(2L)).thenReturn(Optional.of(freeUser));

        mockMvc.perform(get("/subscriptions/user/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(2))
                .andExpect(jsonPath("$.subscriptionType").value("FREE"))
                .andExpect(jsonPath("$.isActive").value(true))
                .andExpect(jsonPath("$.maxCourses").value(3))
                .andExpect(jsonPath("$.hasAdvancedFeatures").value(false))
                .andExpect(jsonPath("$.daysRemaining").isEmpty());

        verify(userRepository, times(1)).findById(2L);
    }

    @Test
    @DisplayName("Test Update User Subscription - Success")
    void testUpdateUserSubscriptionSuccess() throws Exception {
        // Mock repository behavior
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        String subscriptionJson = "{\"subscriptionType\":\"PREMIUM\",\"expirationDate\":\"2025-12-31T23:59:59\"}";

        mockMvc.perform(put("/subscriptions/user/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(subscriptionJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Abbonamento aggiornato con successo"))
                .andExpect(jsonPath("$.subscriptionType").value("BASIC")) // Original type
                .andExpect(jsonPath("$.isActive").value(true));

        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("Test Update User Subscription - User Not Found")
    void testUpdateUserSubscriptionNotFound() throws Exception {
        // Mock repository behavior - user not found
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        String subscriptionJson = "{\"subscriptionType\":\"PREMIUM\"}";

        mockMvc.perform(put("/subscriptions/user/999")
                .contentType(MediaType.APPLICATION_JSON)
                .content(subscriptionJson))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Utente non trovato"));

        verify(userRepository, times(1)).findById(999L);
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("Test Update User Subscription - Invalid Subscription Type")
    void testUpdateUserSubscriptionInvalidType() throws Exception {
        // Mock repository behavior
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));

        String subscriptionJson = "{\"subscriptionType\":\"INVALID_TYPE\"}";

        mockMvc.perform(put("/subscriptions/user/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(subscriptionJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Tipo abbonamento non valido"));

        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("Test Update User Subscription - Invalid Date Format")
    void testUpdateUserSubscriptionInvalidDate() throws Exception {
        // Mock repository behavior
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));

        String subscriptionJson = "{\"expirationDate\":\"invalid-date\"}";

        mockMvc.perform(put("/subscriptions/user/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(subscriptionJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Formato data scadenza non valido"));

        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("Test Get Subscription Statistics - Success")
    void testGetSubscriptionStatisticsSuccess() throws Exception {
        // Mock repository behavior
        when(userRepository.countBySubscriptionType(User.SubscriptionType.FREE)).thenReturn(1L);
        when(userRepository.countBySubscriptionType(User.SubscriptionType.BASIC)).thenReturn(1L);
        when(userRepository.countBySubscriptionType(User.SubscriptionType.PREMIUM)).thenReturn(1L);
        when(userRepository.countBySubscriptionType(User.SubscriptionType.ENTERPRISE)).thenReturn(0L);
        when(userRepository.findUsersWithActiveSubscriptions(any(LocalDateTime.class))).thenReturn(Arrays.asList(testUser));
        when(userRepository.findUsersWithExpiredSubscriptions(any(LocalDateTime.class))).thenReturn(Arrays.asList());
        when(userRepository.count()).thenReturn(4L);

        mockMvc.perform(get("/subscriptions/statistics"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.freeUsers").value(1))
                .andExpect(jsonPath("$.basicUsers").value(1))
                .andExpect(jsonPath("$.premiumUsers").value(1))
                .andExpect(jsonPath("$.enterpriseUsers").value(0))
                .andExpect(jsonPath("$.activeSubscriptions").value(1))
                .andExpect(jsonPath("$.expiredSubscriptions").value(0))
                .andExpect(jsonPath("$.totalUsers").value(4));

        verify(userRepository, times(1)).countBySubscriptionType(User.SubscriptionType.FREE);
        verify(userRepository, times(1)).countBySubscriptionType(User.SubscriptionType.BASIC);
        verify(userRepository, times(1)).countBySubscriptionType(User.SubscriptionType.PREMIUM);
        verify(userRepository, times(1)).countBySubscriptionType(User.SubscriptionType.ENTERPRISE);
        verify(userRepository, times(1)).findUsersWithActiveSubscriptions(any(LocalDateTime.class));
        verify(userRepository, times(1)).findUsersWithExpiredSubscriptions(any(LocalDateTime.class));
        verify(userRepository, times(1)).count();
    }

    @Test
    @DisplayName("Test Cancel User Subscription - Success")
    void testCancelUserSubscriptionSuccess() throws Exception {
        // Mock repository behavior
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        mockMvc.perform(delete("/subscriptions/user/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Abbonamento cancellato con successo"));

        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("Test Cancel User Subscription - User Not Found")
    void testCancelUserSubscriptionNotFound() throws Exception {
        // Mock repository behavior - user not found
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        mockMvc.perform(delete("/subscriptions/user/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Utente non trovato"));

        verify(userRepository, times(1)).findById(999L);
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("Test CORS Headers")
    void testCORSHeaders() throws Exception {
        mockMvc.perform(get("/subscriptions/types")
                .header("Origin", "http://localhost:5173"))
                .andExpect(status().isOk())
                .andExpect(header().string("Access-Control-Allow-Origin", "http://localhost:5173"));
    }

    @Test
    @DisplayName("Test Content Type Headers")
    void testContentTypeHeaders() throws Exception {
        mockMvc.perform(get("/subscriptions/types"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @DisplayName("Test Error Handling - Internal Server Error")
    void testErrorHandlingInternalServerError() throws Exception {
        // Mock repository to throw exception
        when(userRepository.findById(anyLong())).thenThrow(new RuntimeException("Database error"));

        mockMvc.perform(get("/subscriptions/user/1"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.error").value("Errore nel recupero dell'abbonamento"))
                .andExpect(jsonPath("$.message").value("Database error"));
    }
}
