package com.esame.config;

import com.esame.model.Corso;
import com.esame.repository.CorsoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * Data Loader
 * Caricatore Dati
 * 
 * Loads sample data into the database on startup
 * Carica dati di esempio nel database all'avvio
 */
@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private CorsoRepository corsoRepository;

    @Override
    public void run(String... args) throws Exception {
        // Check if data already exists / Controlla se i dati esistono già
        if (corsoRepository.count() > 0) {
            System.out.println("Database already contains data. Skipping data loading.");
            return;
        }

        System.out.println("Loading sample data...");

        // Create sample courses / Crea corsi di esempio
        Corso corso1 = new Corso();
        corso1.setTitolo("Introduzione a React");
        corso1.setDataOraInizio(LocalDateTime.now().plusDays(7));
        corso1.setLuogo("Aula Magna, Via Roma 123, Milano");
        corso1.setDisponibilita(25);
        corso1.setProgramma("1. Fondamenti di React\n2. Componenti e Props\n3. State e Lifecycle\n4. Hooks\n5. Routing\n6. State Management");
        corso1.setDocenti("Prof. Mario Rossi, Ing. Anna Bianchi");
        corso1.setInformazioniGenerali("Corso introduttivo per sviluppatori che vogliono imparare React. Include esercitazioni pratiche e progetti reali.");
        corso1.setDataTest(LocalDateTime.now().plusDays(14));
        corso1.setDataCompletamento(LocalDateTime.now().plusDays(21));
        corso1.setDurataOre(40);
        corso1.setLivello("Principiante");
        corso1.setCategoria("Frontend Development");
        corso1.setPrezzo(299.99);
        corso1.setCertificazione(true);
        corsoRepository.save(corso1);

        Corso corso2 = new Corso();
        corso2.setTitolo("Spring Boot Avanzato");
        corso2.setDataOraInizio(LocalDateTime.now().plusDays(14));
        corso2.setLuogo("Sala Conferenze, Piazza Duomo 1, Milano");
        corso2.setDisponibilita(20);
        corso2.setProgramma("1. Spring Boot Architecture\n2. Microservices\n3. Security\n4. Testing\n5. Performance\n6. Deployment");
        corso2.setDocenti("Prof. Giuseppe Verdi, Dr. Maria Neri");
        corso2.setInformazioniGenerali("Corso avanzato per sviluppatori Java con esperienza in Spring Framework.");
        corso2.setDataTest(LocalDateTime.now().plusDays(21));
        corso2.setDataCompletamento(LocalDateTime.now().plusDays(28));
        corso2.setDurataOre(60);
        corso2.setLivello("Avanzato");
        corso2.setCategoria("Backend Development");
        corso2.setPrezzo(499.99);
        corso2.setCertificazione(true);
        corsoRepository.save(corso2);

        Corso corso3 = new Corso();
        corso3.setTitolo("Database Design");
        corso3.setDataOraInizio(LocalDateTime.now().plusDays(21));
        corso3.setLuogo("Laboratorio Informatico, Via Garibaldi 45, Milano");
        corso3.setDisponibilita(15);
        corsoRepository.save(corso3);

        Corso corso4 = new Corso();
        corso4.setTitolo("JavaScript Moderno");
        corso4.setDataOraInizio(LocalDateTime.now().plusDays(28));
        corso4.setLuogo("Aula 101, Università Statale, Milano");
        corso4.setDisponibilita(30);
        corsoRepository.save(corso4);

        Corso corso5 = new Corso();
        corso5.setTitolo("DevOps Fundamentals");
        corso5.setDataOraInizio(LocalDateTime.now().plusDays(35));
        corso5.setLuogo("Sala Riunioni, Via Brera 2, Milano");
        corso5.setDisponibilita(18);
        corsoRepository.save(corso5);

        Corso corso6 = new Corso();
        corso6.setTitolo("Machine Learning Base");
        corso6.setDataOraInizio(LocalDateTime.now().plusDays(42));
        corso6.setLuogo("Aula 205, Politecnico di Milano");
        corso6.setDisponibilita(12);
        corsoRepository.save(corso6);

        Corso corso7 = new Corso();
        corso7.setTitolo("Web Security");
        corso7.setDataOraInizio(LocalDateTime.now().plusDays(49));
        corso7.setLuogo("Centro Formazione, Via Montenapoleone 8, Milano");
        corso7.setDisponibilita(22);
        corsoRepository.save(corso7);

        Corso corso8 = new Corso();
        corso8.setTitolo("Cloud Computing");
        corso8.setDataOraInizio(LocalDateTime.now().plusDays(56));
        corso8.setLuogo("Sala Eventi, Via della Spiga 10, Milano");
        corso8.setDisponibilita(16);
        corsoRepository.save(corso8);

        System.out.println("Sample data loaded successfully!");
        System.out.println("Created " + corsoRepository.count() + " courses.");
    }
}
