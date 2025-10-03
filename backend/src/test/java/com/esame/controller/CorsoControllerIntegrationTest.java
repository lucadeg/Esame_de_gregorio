package com.esame.controller;

import com.esame.model.Corso;
import com.esame.repository.CorsoRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

/**
 * Integration Tests for CorsoController
 * Test di Integrazione per CorsoController
 */
@SpringBootTest
@AutoConfigureWebMvc
@ActiveProfiles("test")
@Transactional
public class CorsoControllerIntegrationTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @Autowired
    private CorsoRepository corsoRepository;
    
    
    @Test
    void testGetAllCourses() throws Exception {
        // Create test data
        Corso corso = new Corso("Test Course", java.time.LocalDateTime.now(), "Test Location", 10);
        corsoRepository.save(corso);
        
        // Test GET /courses endpoint
        mockMvc.perform(get("/courses")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].titolo").value("Test Course"))
                .andExpect(jsonPath("$[0].luogo").value("Test Location"))
                .andExpect(jsonPath("$[0].disponibilita").value(10));
    }
    
    @Test
    void testGetAllCoursesWithFilters() throws Exception {
        // Create test data
        Corso corso1 = new Corso("Java Course", java.time.LocalDateTime.now(), "Rome", 5);
        Corso corso2 = new Corso("Python Course", java.time.LocalDateTime.now(), "Milan", 8);
        corsoRepository.save(corso1);
        corsoRepository.save(corso2);
        
        // Test with title filter
        mockMvc.perform(get("/courses")
                .param("titolo", "Java")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].titolo").value("Java Course"));
        
        // Test with location filter
        mockMvc.perform(get("/courses")
                .param("luogo", "Milan")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].luogo").value("Milan"));
        
        // Test with availability filter
        mockMvc.perform(get("/courses")
                .param("disponibili", "true")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }
    
    @Test
    void testGetCourseById() throws Exception {
        // Create test data
        Corso corso = new Corso("Test Course", java.time.LocalDateTime.now(), "Test Location", 10);
        corso = corsoRepository.save(corso);
        
        // Test GET /courses/{id} endpoint
        mockMvc.perform(get("/courses/" + corso.getCorsoId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.titolo").value("Test Course"))
                .andExpect(jsonPath("$.luogo").value("Test Location"))
                .andExpect(jsonPath("$.disponibilita").value(10));
    }
    
    @Test
    void testGetUpcomingCourses() throws Exception {
        // Create test data
        Corso corso1 = new Corso("Past Course", java.time.LocalDateTime.now().minusDays(1), "Location", 10);
        Corso corso2 = new Corso("Future Course", java.time.LocalDateTime.now().plusDays(1), "Location", 10);
        corsoRepository.save(corso1);
        corsoRepository.save(corso2);
        
        // Test GET /courses/upcoming endpoint
        mockMvc.perform(get("/courses/upcoming")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].titolo").value("Future Course"));
    }
}


