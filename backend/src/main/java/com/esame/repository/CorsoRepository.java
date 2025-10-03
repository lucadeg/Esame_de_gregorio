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
     * Find courses by title starting with specified text
     * Trova corsi per titolo che inizia con il testo specificato
     * 
     * @param titolo Course title to search for / Titolo del corso da cercare
     * @return List of courses matching the criteria / Lista di corsi che corrispondono ai criteri
     */
    @Query("SELECT c FROM Corso c WHERE LOWER(c.titolo) LIKE LOWER(CONCAT(:titolo, '%'))")
    List<Corso> findByTitoloStartingWithIgnoreCase(@Param("titolo") String titolo);
    
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
     * Find courses by location starting with specified text
     * Trova corsi per luogo che inizia con il testo specificato
     * 
     * @param luogo Location to search for / Luogo da cercare
     * @return List of courses matching the criteria / Lista di corsi che corrispondono ai criteri
     */
    @Query("SELECT c FROM Corso c WHERE LOWER(c.luogo) LIKE LOWER(CONCAT(:luogo, '%'))")
    List<Corso> findByLuogoStartingWithIgnoreCase(@Param("luogo") String luogo);
    
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
    
    /**
     * Find courses by category
     * Trova corsi per categoria
     * 
     * @param categoria Category to search for / Categoria da cercare
     * @return List of courses in the category / Lista di corsi nella categoria
     */
    List<Corso> findByCategoria(String categoria);
    
    /**
     * Find courses by availability greater than specified value
     * Trova corsi per disponibilità maggiore del valore specificato
     * 
     * @param disponibilita Minimum availability / Disponibilità minima
     * @return List of courses with availability greater than specified / Lista di corsi con disponibilità maggiore di quella specificata
     */
    List<Corso> findByDisponibilitaGreaterThan(Integer disponibilita);
    
    /**
     * Find courses by teacher containing specified text
     * Trova corsi per docente contenente il testo specificato
     * 
     * @param docente Teacher name to search for / Nome docente da cercare
     * @return List of courses matching the criteria / Lista di corsi che corrispondono ai criteri
     */
    @Query("SELECT c FROM Corso c WHERE LOWER(c.docenti) LIKE LOWER(CONCAT('%', :docente, '%'))")
    List<Corso> findByDocentiContainingIgnoreCase(@Param("docente") String docente);
    
    /**
     * Find courses by level
     * Trova corsi per livello
     * 
     * @param livello Course level / Livello del corso
     * @return List of courses with specified level / Lista di corsi con il livello specificato
     */
    List<Corso> findByLivello(String livello);
    
    /**
     * Find courses by price range
     * Trova corsi per fascia di prezzo
     * 
     * @param prezzoMin Minimum price / Prezzo minimo
     * @param prezzoMax Maximum price / Prezzo massimo
     * @return List of courses in price range / Lista di corsi nella fascia di prezzo
     */
    @Query("SELECT c FROM Corso c WHERE c.prezzo BETWEEN :prezzoMin AND :prezzoMax")
    List<Corso> findByPrezzoBetween(@Param("prezzoMin") Double prezzoMin, @Param("prezzoMax") Double prezzoMax);
    
    /**
     * Find courses by duration range
     * Trova corsi per fascia di durata
     * 
     * @param durataMin Minimum duration / Durata minima
     * @param durataMax Maximum duration / Durata massima
     * @return List of courses in duration range / Lista di corsi nella fascia di durata
     */
    @Query("SELECT c FROM Corso c WHERE c.durataOre BETWEEN :durataMin AND :durataMax")
    List<Corso> findByDurataOreBetween(@Param("durataMin") Integer durataMin, @Param("durataMax") Integer durataMax);
    
    /**
     * Find courses with certification
     * Trova corsi con certificazione
     * 
     * @param certificazione Certification flag / Flag certificazione
     * @return List of courses with/without certification / Lista di corsi con/senza certificazione
     */
    List<Corso> findByCertificazione(Boolean certificazione);
    
    /**
     * Find courses by title and start date
     * Trova corsi per titolo e data di inizio
     * 
     * @param titolo Course title / Titolo del corso
     * @param dataOraInizio Start date and time / Data e ora di inizio
     * @return True if course exists / True se corso esiste
     */
    boolean existsByTitoloAndDataOraInizio(String titolo, LocalDateTime dataOraInizio);
    
    /**
     * Find courses by title and start date excluding specific course
     * Trova corsi per titolo e data di inizio escludendo corso specifico
     * 
     * @param titolo Course title / Titolo del corso
     * @param dataOraInizio Start date and time / Data e ora di inizio
     * @param corsoId Course ID to exclude / ID corso da escludere
     * @return True if course exists / True se corso esiste
     */
    @Query("SELECT COUNT(c) > 0 FROM Corso c WHERE c.titolo = :titolo AND c.dataOraInizio = :dataOraInizio AND c.corsoId != :corsoId")
    boolean existsByTitoloAndDataOraInizioAndCorsoIdNot(@Param("titolo") String titolo, @Param("dataOraInizio") LocalDateTime dataOraInizio, @Param("corsoId") Long corsoId);
}
