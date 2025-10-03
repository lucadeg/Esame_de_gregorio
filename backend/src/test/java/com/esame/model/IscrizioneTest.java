package com.esame.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

/**
 * Unit Tests for Iscrizione Entity
 * Test Unitari per Entità Iscrizione
 */
public class IscrizioneTest {
    
    private Iscrizione iscrizione;
    private Corso corso;
    
    @BeforeEach
    void setUp() {
        iscrizione = new Iscrizione();
        corso = new Corso("Test Course", LocalDateTime.now(), "Test Location", 10);
    }
    
    @Test
    void testIscrizioneCreation() {
        assertNotNull(iscrizione);
    }
    
    @Test
    void testIscrizioneSettersAndGetters() {
        // Test setters and getters
        iscrizione.setIscrizioneId(1L);
        iscrizione.setCorsoId(1L);
        iscrizione.setPartecipanteNome("Mario");
        iscrizione.setPartecipanteCognome("Rossi");
        iscrizione.setPartecipanteEmail("mario.rossi@example.com");
        iscrizione.setDataOraIscrizione(LocalDateTime.now());
        
        assertEquals(1L, iscrizione.getIscrizioneId());
        assertEquals(1L, iscrizione.getCorsoId());
        assertEquals("Mario", iscrizione.getPartecipanteNome());
        assertEquals("Rossi", iscrizione.getPartecipanteCognome());
        assertEquals("mario.rossi@example.com", iscrizione.getPartecipanteEmail());
        assertNotNull(iscrizione.getDataOraIscrizione());
    }
    
    @Test
    void testIscrizioneConstructorWithParameters() {
        Iscrizione newIscrizione = new Iscrizione(1L, "Mario", "Rossi", "mario.rossi@example.com");
        
        assertEquals(1L, newIscrizione.getCorsoId());
        assertEquals("Mario", newIscrizione.getPartecipanteNome());
        assertEquals("Rossi", newIscrizione.getPartecipanteCognome());
        assertEquals("mario.rossi@example.com", newIscrizione.getPartecipanteEmail());
        assertNotNull(newIscrizione.getDataOraIscrizione());
    }
    
    @Test
    void testIscrizioneConstructorWithCorso() {
        Iscrizione newIscrizione = new Iscrizione(corso, "Mario", "Rossi", "mario.rossi@example.com");
        
        assertEquals(corso.getCorsoId(), newIscrizione.getCorsoId());
        assertEquals("Mario", newIscrizione.getPartecipanteNome());
        assertEquals("Rossi", newIscrizione.getPartecipanteCognome());
        assertEquals("mario.rossi@example.com", newIscrizione.getPartecipanteEmail());
        assertNotNull(newIscrizione.getDataOraIscrizione());
    }
    
    @Test
    void testIscrizioneToString() {
        iscrizione.setPartecipanteNome("Mario");
        iscrizione.setPartecipanteCognome("Rossi");
        String toString = iscrizione.toString();
        assertNotNull(toString);
        assertTrue(toString.contains("Mario"));
        assertTrue(toString.contains("Rossi"));
    }
    
    @Test
    void testIscrizioneEquals() {
        Iscrizione iscrizione1 = new Iscrizione();
        Iscrizione iscrizione2 = new Iscrizione();
        
        iscrizione1.setIscrizioneId(1L);
        iscrizione2.setIscrizioneId(1L);
        
        assertEquals(iscrizione1, iscrizione2);
    }
    
    @Test
    void testIscrizioneHashCode() {
        iscrizione.setIscrizioneId(1L);
        int hashCode = iscrizione.hashCode();
        assertNotNull(hashCode);
    }
    
    @Test
    void testIscrizioneCorsoRelationship() {
        iscrizione.setCorso(corso);
        assertEquals(corso, iscrizione.getCorso());
    }
}


