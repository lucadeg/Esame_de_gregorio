-- Database Schema for Course Management System
-- Schema Database per Sistema Gestione Corsi
-- Generated at: 2025-10-02 10:11:35

-- Create database / Crea database
CREATE DATABASE course_management;

-- Use database / Usa database
\c course_management;

-- Create courses table / Crea tabella corsi
CREATE TABLE corsi (
    corso_id SERIAL PRIMARY KEY,
    titolo VARCHAR(50) NOT NULL,
    data_ora_inizio TIMESTAMP NOT NULL,
    luogo VARCHAR(100) NOT NULL,
    disponibilita INT NOT NULL CHECK (disponibilita >= 0)
);

-- Create enrollments table / Crea tabella iscrizioni
CREATE TABLE iscrizioni (
    iscrizione_id SERIAL PRIMARY KEY,
    corso_id INT NOT NULL,
    partecipante_nome VARCHAR(30) NOT NULL,
    partecipante_cognome VARCHAR(30) NOT NULL,
    partecipante_email VARCHAR(50) NOT NULL,
    data_ora_iscrizione TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (corso_id) REFERENCES corsi(corso_id) ON DELETE CASCADE
);

-- Create indexes for better performance / Crea indici per migliore performance
CREATE INDEX idx_corsi_data_inizio ON corsi(data_ora_inizio);
CREATE INDEX idx_iscrizioni_corso_id ON iscrizioni(corso_id);
CREATE INDEX idx_iscrizioni_email ON iscrizioni(partecipante_email);

-- Insert sample data / Inserisci dati di esempio
INSERT INTO corsi (titolo, data_ora_inizio, luogo, disponibilita) VALUES
('Introduzione a Next.js', '2025-11-15 09:00:00', 'Aula Magna - Università', 30),
('Workshop React Avanzato', '2025-11-20 14:00:00', 'Laboratorio Informatica', 20),
('Database Design con PostgreSQL', '2025-11-25 10:00:00', 'Sala Conferenze', 25),
('API REST con Spring Boot', '2025-12-01 15:30:00', 'Aula 101', 15),
('Docker e Containerizzazione', '2025-12-05 09:30:00', 'Laboratorio Sistemi', 18);

-- Create function to update course availability / Crea funzione per aggiornare disponibilità corso
CREATE OR REPLACE FUNCTION update_course_availability()
RETURNS TRIGGER AS $$
BEGIN
    IF TG_OP = 'INSERT' THEN
        UPDATE corsi 
        SET disponibilita = disponibilita - 1 
        WHERE corso_id = NEW.corso_id;
        RETURN NEW;
    ELSIF TG_OP = 'DELETE' THEN
        UPDATE corsi 
        SET disponibilita = disponibilita + 1 
        WHERE corso_id = OLD.corso_id;
        RETURN OLD;
    END IF;
    RETURN NULL;
END;
$$ LANGUAGE plpgsql;

-- Create trigger for automatic availability update / Crea trigger per aggiornamento automatico disponibilità
CREATE TRIGGER trigger_update_availability
    AFTER INSERT OR DELETE ON iscrizioni
    FOR EACH ROW
    EXECUTE FUNCTION update_course_availability();
