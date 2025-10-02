package com.esame.repository;

import com.esame.model.Corso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Course Repository Interface
 * Interfaccia Repository Corso
 * 
 * Data access layer for Course entity
 * Livello di accesso ai dati per l'entità Corso
 */
@Repository
public interface CorsoRepository extends JpaRepository<Corso, Long> {
    
    /**
     * Find courses by title containing specified text
     * Trova corsi per titolo contenente il testo specificato
     * 
     * @param titolo Course title to search for / Titolo del corso da cercare
     * @return List of courses matching the criteria / Lista di corsi che corrispondono ai criteri
     */
    @Query("SELECT c FROM Corso c WHERE LOWER(c.titolo) LIKE LOWER(CONCAT('%', :titolo, '%'))")
    List<Corso> findByTitoloContainingIgnoreCase(@Param("titolo") String titolo);
    
    /**
     * Find courses by location containing specified text
     * Trova corsi per luogo contenente il testo specificato
     * 
     * @param luogo Location to search for / Luogo da cercare
     * @return List of courses matching the criteria / Lista di corsi che corrispondono ai criteri
     */
    @Query("SELECT c FROM Corso c WHERE LOWER(c.luogo) LIKE LOWER(CONCAT('%', :luogo, '%'))")
    List<Corso> findByLuogoContainingIgnoreCase(@Param("luogo") String luogo);
    
    /**
     * Find courses with available spots
     * Trova corsi con posti disponibili
     * 
     * @return List of courses with availability > 0 / Lista di corsi con disponibilità > 0
     */
    @Query("SELECT c FROM Corso c WHERE c.disponibilita > 0")
    List<Corso> findAvailableCourses();
    
    /**
     * Find courses starting after specified date
     * Trova corsi che iniziano dopo la data specificata
     * 
     * @param dataInizio Start date filter / Filtro data di inizio
     * @return List of courses starting after the date / Lista di corsi che iniziano dopo la data
     */
    @Query("SELECT c FROM Corso c WHERE c.dataOraInizio >= :dataInizio")
    List<Corso> findCoursesStartingAfter(@Param("dataInizio") LocalDateTime dataInizio);
    
    /**
     * Find courses with availability and starting after specified date
     * Trova corsi con disponibilità e che iniziano dopo la data specificata
     * 
     * @param dataInizio Start date filter / Filtro data di inizio
     * @return List of available courses starting after the date / Lista di corsi disponibili che iniziano dopo la data
     */
    @Query("SELECT c FROM Corso c WHERE c.disponibilita > 0 AND c.dataOraInizio >= :dataInizio")
    List<Corso> findAvailableCoursesStartingAfter(@Param("dataInizio") LocalDateTime dataInizio);
}
