-- Course Management System Database Schema
-- Schema Database Sistema Gestione Corsi
-- Generated at: 2025-10-02 21:20:00

-- Create database / Crea database
CREATE DATABASE course_management;

-- Use database / Usa database
\c course_management;

-- Create users table / Crea tabella utenti
CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(50) NOT NULL,
    cognome VARCHAR(50) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    phone_number VARCHAR(20),
    date_of_birth TIMESTAMP,
    profile_image VARCHAR(500),
    is_active BOOLEAN DEFAULT TRUE,
    email_verified BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    last_login TIMESTAMP,
    role VARCHAR(20) DEFAULT 'STUDENT',
    subscription_type VARCHAR(20) DEFAULT 'FREE',
    subscription_expires_at TIMESTAMP
);

-- Create courses table / Crea tabella corsi
CREATE TABLE corsi (
    corso_id BIGSERIAL PRIMARY KEY,
    titolo VARCHAR(255) NOT NULL,
    descrizione TEXT,
    data_ora_inizio TIMESTAMP NOT NULL,
    data_ora_fine TIMESTAMP,
    luogo VARCHAR(255) NOT NULL,
    disponibilita INT NOT NULL DEFAULT 0,
    prezzo DECIMAL(10,2),
    docenti VARCHAR(255),
    programma TEXT,
    informazioni_generali TEXT,
    data_test_finale TIMESTAMP,
    data_completamento TIMESTAMP,
    progresso INT DEFAULT 0,
    durata_ore INT
);

-- Create enrollments table / Crea tabella iscrizioni
CREATE TABLE iscrizioni (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    cognome VARCHAR(100) NOT NULL,
    email VARCHAR(255) NOT NULL,
    data_iscrizione TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    stato VARCHAR(50) DEFAULT 'attivo',
    corso_id BIGINT,
    user_id BIGINT,
    FOREIGN KEY (corso_id) REFERENCES corsi(corso_id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE SET NULL
);

-- Create indexes for better performance / Crea indici per migliore performance
CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_users_role ON users(role);
CREATE INDEX idx_users_subscription_type ON users(subscription_type);
CREATE INDEX idx_users_active ON users(is_active);
CREATE INDEX idx_corsi_data_inizio ON corsi(data_ora_inizio);
CREATE INDEX idx_corsi_luogo ON corsi(luogo);
CREATE INDEX idx_corsi_disponibilita ON corsi(disponibilita);
CREATE INDEX idx_iscrizioni_corso_id ON iscrizioni(corso_id);
CREATE INDEX idx_iscrizioni_user_id ON iscrizioni(user_id);
CREATE INDEX idx_iscrizioni_email ON iscrizioni(email);
CREATE INDEX idx_iscrizioni_stato ON iscrizioni(stato);

-- Insert sample users / Inserisci utenti di esempio
INSERT INTO users (nome, cognome, email, password, role, subscription_type) VALUES
('Mario', 'Rossi', 'mario.rossi@email.com', 'password123', 'STUDENT', 'FREE'),
('Giulia', 'Bianchi', 'giulia.bianchi@email.com', 'password123', 'INSTRUCTOR', 'PREMIUM'),
('Admin', 'System', 'admin@coursehub.com', 'admin123', 'ADMIN', 'ENTERPRISE'),
('Luca', 'Verdi', 'luca.verdi@email.com', 'password123', 'STUDENT', 'BASIC'),
('Sara', 'Neri', 'sara.neri@email.com', 'password123', 'STUDENT', 'FREE');

-- Insert sample courses / Inserisci corsi di esempio
INSERT INTO corsi (titolo, descrizione, data_ora_inizio, data_ora_fine, luogo, disponibilita, prezzo, docenti, programma, informazioni_generali, durata_ore) VALUES
('React Fundamentals', 'Corso base su React e componenti', '2025-11-15 09:00:00', '2025-11-17 17:00:00', 'Lecce - Aula Magna', 25, 199.00, 'Giulia Bianchi', '1. Introduzione React\n2. Componenti\n3. State e Props\n4. Hooks\n5. Progetto finale', 'Corso intensivo di 3 giorni per imparare React da zero', 24),
('Spring Boot Advanced', 'Sviluppo backend con Spring Boot', '2025-11-20 14:00:00', '2025-11-22 18:00:00', 'Milano - Laboratorio', 20, 299.00, 'Mario Rossi', '1. Spring Core\n2. Spring Data JPA\n3. Spring Security\n4. REST API\n5. Testing', 'Corso avanzato per sviluppatori backend', 32),
('Database Design', 'Progettazione e ottimizzazione database', '2025-11-25 10:00:00', '2025-11-27 16:00:00', 'Roma - Sala Conferenze', 15, 249.00, 'Luca Verdi', '1. Modellazione dati\n2. Normalizzazione\n3. Indici\n4. Performance\n5. Best practices', 'Corso teorico-pratico su database', 28),
('Docker & Kubernetes', 'Containerizzazione e orchestrazione', '2025-12-01 15:30:00', '2025-12-03 19:00:00', 'Torino - Lab Sistemi', 18, 349.00, 'Sara Neri', '1. Docker basics\n2. Container orchestration\n3. Kubernetes\n4. CI/CD\n5. Monitoring', 'Corso pratico su tecnologie container', 36),
('JavaScript ES6+', 'JavaScript moderno e avanzato', '2025-12-05 09:30:00', '2025-12-07 17:30:00', 'Napoli - Aula 101', 22, 179.00, 'Giulia Bianchi', '1. ES6 Features\n2. Async/Await\n3. Modules\n4. Promises\n5. Advanced patterns', 'Corso completo su JavaScript moderno', 30);

-- Insert sample enrollments / Inserisci iscrizioni di esempio
INSERT INTO iscrizioni (nome, cognome, email, corso_id, user_id, stato) VALUES
('Mario', 'Rossi', 'mario.rossi@email.com', 1, 1, 'attivo'),
('Luca', 'Verdi', 'luca.verdi@email.com', 1, 4, 'attivo'),
('Sara', 'Neri', 'sara.neri@email.com', 2, 5, 'attivo'),
('Mario', 'Rossi', 'mario.rossi@email.com', 3, 1, 'completato'),
('Luca', 'Verdi', 'luca.verdi@email.com', 2, 4, 'attivo');

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

-- Create function to update user timestamps / Crea funzione per aggiornare timestamp utenti
CREATE OR REPLACE FUNCTION update_user_timestamp()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Create trigger for user timestamp update / Crea trigger per aggiornamento timestamp utenti
CREATE TRIGGER trigger_update_user_timestamp
    BEFORE UPDATE ON users
    FOR EACH ROW
    EXECUTE FUNCTION update_user_timestamp();