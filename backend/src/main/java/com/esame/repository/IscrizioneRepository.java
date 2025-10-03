package com.esame.repository;

import com.esame.model.Iscrizione;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Enrollment Repository Interface
 * Interfaccia Repository Iscrizione
 * 
 * Data access layer for Enrollment entity
 * Livello di accesso ai dati per l'entità Iscrizione
 */
@Repository
public interface IscrizioneRepository extends JpaRepository<Iscrizione, Long> {
    
    /**
     * Find enrollments by course ID
     * Trova iscrizioni per ID corso
     * 
     * @param corsoId Course ID to filter by / ID del corso per cui filtrare
     * @return List of enrollments for the specified course / Lista di iscrizioni per il corso specificato
     */
    List<Iscrizione> findByCorsoId(Long corsoId);
    
    /**
     * Find enrollments by participant email
     * Trova iscrizioni per email partecipante
     * 
     * @param email Participant email to search for / Email del partecipante da cercare
     * @return List of enrollments for the specified email / Lista di iscrizioni per l'email specificata
     */
    List<Iscrizione> findByPartecipanteEmail(String email);
    
    /**
     * Find enrollments by participant name
     * Trova iscrizioni per nome partecipante
     * 
     * @param nome Participant first name / Nome del partecipante
     * @param cognome Participant last name / Cognome del partecipante
     * @return List of enrollments for the specified participant / Lista di iscrizioni per il partecipante specificato
     */
    @Query("SELECT i FROM Iscrizione i WHERE LOWER(i.partecipanteNome) LIKE LOWER(CONCAT('%', :nome, '%')) AND LOWER(i.partecipanteCognome) LIKE LOWER(CONCAT('%', :cognome, '%'))")
    List<Iscrizione> findByPartecipanteNomeAndCognome(@Param("nome") String nome, @Param("cognome") String cognome);
    
    /**
     * Find enrollments created after specified date
     * Trova iscrizioni create dopo la data specificata
     * 
     * @param dataInizio Start date filter / Filtro data di inizio
     * @return List of enrollments created after the date / Lista di iscrizioni create dopo la data
     */
    @Query("SELECT i FROM Iscrizione i WHERE i.dataOraIscrizione >= :dataInizio")
    List<Iscrizione> findEnrollmentsAfterDate(@Param("dataInizio") LocalDateTime dataInizio);
    
    /**
     * Count enrollments for a specific course
     * Conta iscrizioni per un corso specifico
     * 
     * @param corsoId Course ID to count enrollments for / ID del corso per cui contare le iscrizioni
     * @return Number of enrollments for the course / Numero di iscrizioni per il corso
     */
    @Query("SELECT COUNT(i) FROM Iscrizione i WHERE i.corsoId = :corsoId")
    Long countByCorsoId(@Param("corsoId") Long corsoId);
    
    /**
     * Check if participant is already enrolled in a course
     * Controlla se il partecipante è già iscritto a un corso
     * 
     * @param corsoId Course ID / ID del corso
     * @param email Participant email / Email del partecipante
     * @return True if already enrolled, false otherwise / True se già iscritto, false altrimenti
     */
    @Query("SELECT COUNT(i) > 0 FROM Iscrizione i WHERE i.corsoId = :corsoId AND i.partecipanteEmail = :email")
    boolean existsByCorsoIdAndPartecipanteEmail(@Param("corsoId") Long corsoId, @Param("email") String email);
    
    /**
     * Find enrollments by participant name containing
     * Trova iscrizioni per nome partecipante contenente
     * 
     * @param nome Participant name to search for / Nome partecipante da cercare
     * @return List of enrollments matching the criteria / Lista di iscrizioni che corrispondono ai criteri
     */
    @Query("SELECT i FROM Iscrizione i WHERE LOWER(i.partecipanteNome) LIKE LOWER(CONCAT('%', :nome, '%'))")
    List<Iscrizione> findByPartecipanteNomeContainingIgnoreCase(@Param("nome") String nome);
    
    /**
     * Find enrollments by participant surname containing
     * Trova iscrizioni per cognome partecipante contenente
     * 
     * @param cognome Participant surname to search for / Cognome partecipante da cercare
     * @return List of enrollments matching the criteria / Lista di iscrizioni che corrispondono ai criteri
     */
    @Query("SELECT i FROM Iscrizione i WHERE LOWER(i.partecipanteCognome) LIKE LOWER(CONCAT('%', :cognome, '%'))")
    List<Iscrizione> findByPartecipanteCognomeContainingIgnoreCase(@Param("cognome") String cognome);
    
    /**
     * Find enrollments by participant email containing
     * Trova iscrizioni per email partecipante contenente
     * 
     * @param email Participant email to search for / Email partecipante da cercare
     * @return List of enrollments matching the criteria / Lista di iscrizioni che corrispondono ai criteri
     */
    @Query("SELECT i FROM Iscrizione i WHERE LOWER(i.partecipanteEmail) LIKE LOWER(CONCAT('%', :email, '%'))")
    List<Iscrizione> findByPartecipanteEmailContainingIgnoreCase(@Param("email") String email);
    
    /**
     * Find enrollments created between dates
     * Trova iscrizioni create tra date
     * 
     * @param dataInizio Start date / Data inizio
     * @param dataFine End date / Data fine
     * @return List of enrollments created between dates / Lista di iscrizioni create tra le date
     */
    @Query("SELECT i FROM Iscrizione i WHERE i.dataOraIscrizione BETWEEN :dataInizio AND :dataFine")
    List<Iscrizione> findByDataOraIscrizioneBetween(@Param("dataInizio") LocalDateTime dataInizio, @Param("dataFine") LocalDateTime dataFine);
    
    /**
     * Find recent enrollments (last 7 days)
     * Trova iscrizioni recenti (ultimi 7 giorni)
     * 
     * @param dataInizio Start date for recent enrollments / Data inizio per iscrizioni recenti
     * @return List of recent enrollments / Lista di iscrizioni recenti
     */
    @Query("SELECT i FROM Iscrizione i WHERE i.dataOraIscrizione >= :dataInizio ORDER BY i.dataOraIscrizione DESC")
    List<Iscrizione> findRecentEnrollments(@Param("dataInizio") LocalDateTime dataInizio);
    
    /**
     * Count enrollments by participant email
     * Conta iscrizioni per email partecipante
     * 
     * @param email Participant email / Email partecipante
     * @return Number of enrollments / Numero di iscrizioni
     */
    @Query("SELECT COUNT(i) FROM Iscrizione i WHERE i.partecipanteEmail = :email")
    Long countByPartecipanteEmail(@Param("email") String email);
    
    /**
     * Find enrollments by course and participant email
     * Trova iscrizioni per corso e email partecipante
     * 
     * @param corsoId Course ID / ID del corso
     * @param email Participant email / Email partecipante
     * @return List of enrollments / Lista di iscrizioni
     */
    List<Iscrizione> findByCorsoIdAndPartecipanteEmail(Long corsoId, String email);
    
    /**
     * Find enrollments by participant name and surname
     * Trova iscrizioni per nome e cognome partecipante
     * 
     * @param nome Participant first name / Nome partecipante
     * @param cognome Participant last name / Cognome partecipante
     * @return List of enrollments / Lista di iscrizioni
     */
    List<Iscrizione> findByPartecipanteNomeAndPartecipanteCognome(String nome, String cognome);
}
