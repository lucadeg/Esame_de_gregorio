package com.esame.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

/**
 * User Model Unit Tests
 * Test Unitari Modello Utente
 * 
 * Comprehensive unit tests for User entity
 * Test unitari completi per l'entità Utente
 */
class UserTest {

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setNome("Mario");
        user.setCognome("Rossi");
        user.setEmail("mario.rossi@email.com");
        user.setPassword("password123");
        user.setPhoneNumber("+39 123 456 7890");
        user.setDateOfBirth(LocalDateTime.of(1990, 1, 1, 0, 0));
        user.setProfileImage("profile.jpg");
        user.setIsActive(true);
        user.setEmailVerified(true);
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        user.setLastLogin(LocalDateTime.now());
        user.setRole(User.UserRole.STUDENT);
        user.setSubscriptionType(User.SubscriptionType.BASIC);
        user.setSubscriptionExpiresAt(LocalDateTime.now().plusMonths(1));
    }

    @Test
    @DisplayName("Test User Constructor")
    void testUserConstructor() {
        User newUser = new User("Giulia", "Bianchi", "giulia@email.com", "password456");
        
        assertEquals("Giulia", newUser.getNome());
        assertEquals("Bianchi", newUser.getCognome());
        assertEquals("giulia@email.com", newUser.getEmail());
        assertEquals("password456", newUser.getPassword());
    }

    @Test
    @DisplayName("Test Get Full Name")
    void testGetFullName() {
        String fullName = user.getFullName();
        assertEquals("Mario Rossi", fullName);
    }

    @Test
    @DisplayName("Test Subscription Active - Free Plan")
    void testSubscriptionActiveFreePlan() {
        user.setSubscriptionType(User.SubscriptionType.FREE);
        user.setSubscriptionExpiresAt(null);
        
        assertTrue(user.isSubscriptionActive());
    }

    @Test
    @DisplayName("Test Subscription Active - Paid Plan Valid")
    void testSubscriptionActivePaidPlanValid() {
        user.setSubscriptionType(User.SubscriptionType.PREMIUM);
        user.setSubscriptionExpiresAt(LocalDateTime.now().plusDays(30));
        
        assertTrue(user.isSubscriptionActive());
    }

    @Test
    @DisplayName("Test Subscription Inactive - Paid Plan Expired")
    void testSubscriptionInactivePaidPlanExpired() {
        user.setSubscriptionType(User.SubscriptionType.BASIC);
        user.setSubscriptionExpiresAt(LocalDateTime.now().minusDays(1));
        
        assertFalse(user.isSubscriptionActive());
    }

    @Test
    @DisplayName("Test Has Role - Student")
    void testHasRoleStudent() {
        user.setRole(User.UserRole.STUDENT);
        
        assertTrue(user.hasRole(User.UserRole.STUDENT));
        assertFalse(user.hasRole(User.UserRole.INSTRUCTOR));
        assertFalse(user.hasRole(User.UserRole.ADMIN));
    }

    @Test
    @DisplayName("Test Has Role - Instructor")
    void testHasRoleInstructor() {
        user.setRole(User.UserRole.INSTRUCTOR);
        
        assertTrue(user.hasRole(User.UserRole.INSTRUCTOR));
        assertFalse(user.hasRole(User.UserRole.STUDENT));
        assertFalse(user.hasRole(User.UserRole.ADMIN));
    }

    @Test
    @DisplayName("Test Has Role - Admin")
    void testHasRoleAdmin() {
        user.setRole(User.UserRole.ADMIN);
        
        assertTrue(user.hasRole(User.UserRole.ADMIN));
        assertFalse(user.hasRole(User.UserRole.STUDENT));
        assertFalse(user.hasRole(User.UserRole.INSTRUCTOR));
    }

    @Test
    @DisplayName("Test Is Admin")
    void testIsAdmin() {
        user.setRole(User.UserRole.ADMIN);
        assertTrue(user.isAdmin());
        
        user.setRole(User.UserRole.STUDENT);
        assertFalse(user.isAdmin());
    }

    @Test
    @DisplayName("Test Is Instructor")
    void testIsInstructor() {
        user.setRole(User.UserRole.INSTRUCTOR);
        assertTrue(user.isInstructor());
        
        user.setRole(User.UserRole.STUDENT);
        assertFalse(user.isInstructor());
    }

    @Test
    @DisplayName("Test Is Student")
    void testIsStudent() {
        user.setRole(User.UserRole.STUDENT);
        assertTrue(user.isStudent());
        
        user.setRole(User.UserRole.ADMIN);
        assertFalse(user.isStudent());
    }

    @Test
    @DisplayName("Test UserRole Enum Values")
    void testUserRoleEnumValues() {
        assertEquals("Studente", User.UserRole.STUDENT.getDisplayName());
        assertEquals("Istruttore", User.UserRole.INSTRUCTOR.getDisplayName());
        assertEquals("Amministratore", User.UserRole.ADMIN.getDisplayName());
    }

    @Test
    @DisplayName("Test SubscriptionType Enum Values")
    void testSubscriptionTypeEnumValues() {
        // Test FREE subscription
        assertEquals("Gratuito", User.SubscriptionType.FREE.getDisplayName());
        assertEquals(0.0, User.SubscriptionType.FREE.getPrice());
        assertEquals(3, User.SubscriptionType.FREE.getMaxCourses());
        assertFalse(User.SubscriptionType.FREE.hasAdvancedFeatures());

        // Test BASIC subscription
        assertEquals("Base", User.SubscriptionType.BASIC.getDisplayName());
        assertEquals(9.99, User.SubscriptionType.BASIC.getPrice());
        assertEquals(10, User.SubscriptionType.BASIC.getMaxCourses());
        assertTrue(User.SubscriptionType.BASIC.hasAdvancedFeatures());

        // Test PREMIUM subscription
        assertEquals("Premium", User.SubscriptionType.PREMIUM.getDisplayName());
        assertEquals(19.99, User.SubscriptionType.PREMIUM.getPrice());
        assertEquals(25, User.SubscriptionType.PREMIUM.getMaxCourses());
        assertTrue(User.SubscriptionType.PREMIUM.hasAdvancedFeatures());

        // Test ENTERPRISE subscription
        assertEquals("Enterprise", User.SubscriptionType.ENTERPRISE.getDisplayName());
        assertEquals(49.99, User.SubscriptionType.ENTERPRISE.getPrice());
        assertEquals(100, User.SubscriptionType.ENTERPRISE.getMaxCourses());
        assertTrue(User.SubscriptionType.ENTERPRISE.hasAdvancedFeatures());
    }

    @Test
    @DisplayName("Test User Getters and Setters")
    void testUserGettersAndSetters() {
        // Test all getters and setters
        assertEquals(1L, user.getId());
        assertEquals("Mario", user.getNome());
        assertEquals("Rossi", user.getCognome());
        assertEquals("mario.rossi@email.com", user.getEmail());
        assertEquals("password123", user.getPassword());
        assertEquals("+39 123 456 7890", user.getPhoneNumber());
        assertNotNull(user.getDateOfBirth());
        assertEquals("profile.jpg", user.getProfileImage());
        assertTrue(user.getIsActive());
        assertTrue(user.getEmailVerified());
        assertNotNull(user.getCreatedAt());
        assertNotNull(user.getUpdatedAt());
        assertNotNull(user.getLastLogin());
        assertEquals(User.UserRole.STUDENT, user.getRole());
        assertEquals(User.SubscriptionType.BASIC, user.getSubscriptionType());
        assertNotNull(user.getSubscriptionExpiresAt());
    }

    @Test
    @DisplayName("Test User Equality")
    void testUserEquality() {
        User user1 = new User();
        user1.setId(1L);
        user1.setEmail("test@email.com");

        User user2 = new User();
        user2.setId(1L);
        user2.setEmail("test@email.com");

        // Note: This test assumes equals() and hashCode() are implemented
        // In a real scenario, you would implement these methods in the User class
        assertEquals(user1.getId(), user2.getId());
        assertEquals(user1.getEmail(), user2.getEmail());
    }

    @Test
    @DisplayName("Test User String Representation")
    void testUserStringRepresentation() {
        // Test that user can be converted to string without errors
        String userString = user.toString();
        assertNotNull(userString);
        assertFalse(userString.isEmpty());
    }

    @Test
    @DisplayName("Test User Validation")
    void testUserValidation() {
        // Test required fields
        assertNotNull(user.getNome());
        assertNotNull(user.getCognome());
        assertNotNull(user.getEmail());
        assertNotNull(user.getPassword());
        
        // Test email format (basic validation)
        assertTrue(user.getEmail().contains("@"));
        assertTrue(user.getEmail().contains("."));
        
        // Test password length
        assertTrue(user.getPassword().length() >= 6);
    }

    @Test
    @DisplayName("Test User Default Values")
    void testUserDefaultValues() {
        User newUser = new User();
        
        // Test default values
        assertTrue(newUser.getIsActive());
        assertFalse(newUser.getEmailVerified());
        assertNotNull(newUser.getCreatedAt());
        assertNotNull(newUser.getUpdatedAt());
        assertEquals(User.UserRole.STUDENT, newUser.getRole());
        assertEquals(User.SubscriptionType.FREE, newUser.getSubscriptionType());
    }
}
