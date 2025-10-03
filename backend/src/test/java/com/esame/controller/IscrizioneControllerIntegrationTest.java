package com.esame.controller;

import com.esame.model.Corso;
import com.esame.model.Iscrizione;
import com.esame.repository.CorsoRepository;
import com.esame.repository.IscrizioneRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration Tests for IscrizioneController
 * Test di Integrazione per IscrizioneController
 */
@SpringBootTest
@AutoConfigureWebMvc
@ActiveProfiles("test")
@Transactional
public class IscrizioneControllerIntegrationTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @Autowired
    private IscrizioneRepository iscrizioneRepository;
    
    @Autowired
    private CorsoRepository corsoRepository;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    @Test
    void testGetAllEnrollments() throws Exception {
        // Create test data
        Corso corso = new Corso("Test Course", java.time.LocalDateTime.now(), "Test Location", 10);
        corso = corsoRepository.save(corso);
        
        Iscrizione iscrizione = new Iscrizione(corso.getCorsoId(), "Mario", "Rossi", "mario.rossi@example.com");
        iscrizioneRepository.save(iscrizione);
        
        // Test GET /enrollments endpoint
        mockMvc.perform(get("/enrollments")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].partecipanteNome").value("Mario"))
                .andExpect(jsonPath("$[0].partecipanteCognome").value("Rossi"))
                .andExpect(jsonPath("$[0].partecipanteEmail").value("mario.rossi@example.com"));
    }
    
    @Test
    void testGetAllEnrollmentsWithCourseFilter() throws Exception {
        // Create test data
        Corso corso1 = new Corso("Course 1", java.time.LocalDateTime.now(), "Location", 10);
        Corso corso2 = new Corso("Course 2", java.time.LocalDateTime.now(), "Location", 10);
        corso1 = corsoRepository.save(corso1);
        corso2 = corsoRepository.save(corso2);
        
        Iscrizione iscrizione1 = new Iscrizione(corso1.getCorsoId(), "Mario", "Rossi", "mario@example.com");
        Iscrizione iscrizione2 = new Iscrizione(corso2.getCorsoId(), "Luigi", "Verdi", "luigi@example.com");
        iscrizioneRepository.save(iscrizione1);
        iscrizioneRepository.save(iscrizione2);
        
        // Test with course filter
        mockMvc.perform(get("/enrollments")
                .param("corsoId", corso1.getCorsoId().toString())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].partecipanteNome").value("Mario"));
    }
    
    @Test
    void testCreateEnrollment() throws Exception {
        // Create test course
        Corso corso = new Corso("Test Course", java.time.LocalDateTime.now(), "Test Location", 10);
        corso = corsoRepository.save(corso);
        
        // Create enrollment data
        Iscrizione enrollmentData = new Iscrizione(corso.getCorsoId(), "Mario", "Rossi", "mario.rossi@example.com");
        
        // Test POST /enrollments endpoint
        mockMvc.perform(post("/enrollments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(enrollmentData)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.partecipanteNome").value("Mario"))
                .andExpect(jsonPath("$.partecipanteCognome").value("Rossi"))
                .andExpect(jsonPath("$.partecipanteEmail").value("mario.rossi@example.com"))
                .andExpect(jsonPath("$.corsoId").value(corso.getCorsoId()));
    }
    
    @Test
    void testCreateEnrollmentWithInvalidData() throws Exception {
        // Create enrollment with invalid data
        Iscrizione enrollmentData = new Iscrizione();
        enrollmentData.setCorsoId(999L); // Non-existent course
        enrollmentData.setPartecipanteNome("Mario");
        enrollmentData.setPartecipanteCognome("Rossi");
        enrollmentData.setPartecipanteEmail("invalid-email"); // Invalid email format
        
        // Test POST /enrollments with invalid data
        mockMvc.perform(post("/enrollments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(enrollmentData)))
                .andExpect(status().isBadRequest());
    }
    
    @Test
    void testGetEnrollmentById() throws Exception {
        // Create test data
        Corso corso = new Corso("Test Course", java.time.LocalDateTime.now(), "Test Location", 10);
        corso = corsoRepository.save(corso);
        
        Iscrizione iscrizione = new Iscrizione(corso.getCorsoId(), "Mario", "Rossi", "mario.rossi@example.com");
        iscrizione = iscrizioneRepository.save(iscrizione);
        
        // Test GET /enrollments/{id} endpoint
        mockMvc.perform(get("/enrollments/" + iscrizione.getIscrizioneId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.partecipanteNome").value("Mario"))
                .andExpect(jsonPath("$.partecipanteCognome").value("Rossi"))
                .andExpect(jsonPath("$.partecipanteEmail").value("mario.rossi@example.com"));
    }
    
    @Test
    void testGetEnrollmentsByCourse() throws Exception {
        // Create test data
        Corso corso = new Corso("Test Course", java.time.LocalDateTime.now(), "Test Location", 10);
        corso = corsoRepository.save(corso);
        
        Iscrizione iscrizione1 = new Iscrizione(corso.getCorsoId(), "Mario", "Rossi", "mario@example.com");
        Iscrizione iscrizione2 = new Iscrizione(corso.getCorsoId(), "Luigi", "Verdi", "luigi@example.com");
        iscrizioneRepository.save(iscrizione1);
        iscrizioneRepository.save(iscrizione2);
        
        // Test GET /enrollments/course/{courseId} endpoint
        mockMvc.perform(get("/enrollments/course/" + corso.getCorsoId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2));
    }
}


