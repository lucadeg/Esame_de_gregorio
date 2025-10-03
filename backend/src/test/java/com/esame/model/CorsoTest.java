package com.esame.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

/**
 * Unit Tests for Corso Entity
 * Test Unitari per Entità Corso
 */
public class CorsoTest {
    
    private Corso corso;
    
    @BeforeEach
    void setUp() {
        corso = new Corso();
    }
    
    @Test
    void testCorsoCreation() {
        assertNotNull(corso);
    }
    
    @Test
    void testCorsoSettersAndGetters() {
        // Test setters and getters
        corso.setCorsoId(1L);
        corso.setTitolo("Test Course");
        corso.setDataOraInizio(LocalDateTime.now());
        corso.setLuogo("Test Location");
        corso.setDisponibilita(10);
        
        assertEquals(1L, corso.getCorsoId());
        assertEquals("Test Course", corso.getTitolo());
        assertNotNull(corso.getDataOraInizio());
        assertEquals("Test Location", corso.getLuogo());
        assertEquals(10, corso.getDisponibilita());
    }
    
    @Test
    void testCorsoConstructor() {
        LocalDateTime now = LocalDateTime.now();
        Corso newCorso = new Corso("Test Course", now, "Test Location", 10);
        
        assertEquals("Test Course", newCorso.getTitolo());
        assertEquals(now, newCorso.getDataOraInizio());
        assertEquals("Test Location", newCorso.getLuogo());
        assertEquals(10, newCorso.getDisponibilita());
    }
    
    @Test
    void testCorsoToString() {
        corso.setTitolo("Test Course");
        String toString = corso.toString();
        assertNotNull(toString);
        assertTrue(toString.contains("Test Course"));
    }
    
    @Test
    void testCorsoEquals() {
        Corso corso1 = new Corso();
        Corso corso2 = new Corso();
        
        corso1.setCorsoId(1L);
        corso2.setCorsoId(1L);
        
        assertEquals(corso1, corso2);
    }
    
    @Test
    void testCorsoHashCode() {
        corso.setCorsoId(1L);
        int hashCode = corso.hashCode();
        assertNotNull(hashCode);
    }
}


