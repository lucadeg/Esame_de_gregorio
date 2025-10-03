# 🎓 Course Management System - Overview

## 📋 Sistema Completo

**Course Management System** è una piattaforma web completa per la gestione di corsi e iscrizioni, sviluppata con tecnologie moderne e best practices enterprise.

### 🏗️ Architettura

```
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   Frontend      │    │   Backend       │    │   Database      │
│   React 18      │◄──►│   Spring Boot   │◄──►│   H2/PostgreSQL │
│   Bootstrap 5   │    │   Java 17       │    │   JPA/Hibernate │
│   React Query   │    │   REST API      │    │   SQL Scripts   │
└─────────────────┘    └─────────────────┘    └─────────────────┘
```

### 🚀 Tecnologie

#### Frontend
- **React 18** - UI Framework
- **Bootstrap 5** - CSS Framework
- **React Query** - Data Fetching
- **React Router** - Navigation
- **Lucide React** - Icons
- **Three.js** - 3D Backgrounds
- **Vite** - Build Tool

#### Backend
- **Spring Boot 3.2.0** - Framework
- **Java 17** - Language
- **Spring Data JPA** - ORM
- **Hibernate** - Persistence
- **Swagger/OpenAPI** - Documentation
- **Spring Actuator** - Monitoring

#### Database
- **H2 Database** - Development
- **PostgreSQL** - Production
- **JPA/Hibernate** - ORM
- **SQL Scripts** - Schema

### 📁 Struttura Progetto

```
Esame_De_Gregorio/
├── backend/                 # Spring Boot Backend
│   ├── src/main/java/      # Java Source Code
│   ├── src/main/resources/ # Configuration
│   └── target/             # Compiled Classes
├── frontend/               # React Frontend
│   ├── src/               # React Source Code
│   ├── public/            # Static Assets
│   └── dist/              # Built Files
├── database/              # Database Scripts
├── docs/                  # Documentation
├── scripts/               # Automation Scripts
├── tests/                 # Test Files
└── docker-compose.yml     # Docker Configuration
```

### 🎯 Funzionalità Principali

#### 👥 Gestione Corsi
- ✅ **CRUD Completo** - Creazione, lettura, aggiornamento, eliminazione
- ✅ **Ricerca Avanzata** - Filtri per titolo, luogo, docente, materia, date, durata
- ✅ **Pagine Dettaglio** - Informazioni complete per ogni corso
- ✅ **Gestione Disponibilità** - Controllo posti disponibili

#### 📝 Gestione Iscrizioni
- ✅ **Iscrizione Corsi** - Processo completo di iscrizione
- ✅ **Visualizzazione Iscrizioni** - Lista con filtri e ricerca
- ✅ **Stati Iscrizione** - Attivo, completato, cancellato
- ✅ **Informazioni Partecipanti** - Dati completi studenti

#### 🎨 Interfaccia Utente
- ✅ **Design Responsive** - Bootstrap 5 per tutti i dispositivi
- ✅ **3D Backgrounds** - Three.js per effetti visivi
- ✅ **Animazioni** - Transizioni fluide e moderne
- ✅ **Navigation** - Menu intuitivo e breadcrumb

#### 🔍 Sistema di Ricerca
- ✅ **Filtri Multipli** - Titolo, luogo, docente, materia, date, durata
- ✅ **Ricerca in Tempo Reale** - Debounce e ottimizzazione
- ✅ **Gestione Errori** - Feedback utente completo
- ✅ **Risultati Paginati** - Performance ottimizzata

### 🛠️ API Endpoints

#### Corsi
- `GET /courses` - Lista corsi con filtri
- `GET /courses/{id}` - Dettaglio corso
- `POST /courses` - Crea corso
- `PUT /courses/{id}` - Aggiorna corso
- `DELETE /courses/{id}` - Elimina corso

#### Iscrizioni
- `GET /enrollments` - Lista iscrizioni
- `GET /enrollments/{id}` - Dettaglio iscrizione
- `POST /enrollments` - Crea iscrizione
- `PUT /enrollments/{id}` - Aggiorna iscrizione
- `DELETE /enrollments/{id}` - Elimina iscrizione

### 🚀 Avvio Sistema

#### Sviluppo Locale
```bash
# Backend
cd backend
./mvnw spring-boot:run

# Frontend
cd frontend
npm install
npm run dev
```

#### Docker
```bash
docker-compose up -d
```

### 📊 Monitoraggio

- **Health Check**: `http://localhost:8080/actuator/health`
- **API Docs**: `http://localhost:8080/swagger-ui.html`
- **Frontend**: `http://localhost:5173`

### 🔧 Configurazione

#### Database
- **H2**: `jdbc:h2:mem:testdb` (Development)
- **PostgreSQL**: Configurabile per produzione

#### CORS
- Configurato per `localhost:5173` e `localhost:3000`
- Supporto per sviluppo e produzione

### 📈 Performance

- **Caching**: React Query per ottimizzazione
- **Lazy Loading**: Componenti caricati on-demand
- **Debounce**: Ricerca ottimizzata
- **Compression**: Assets compressi

### 🧪 Testing

- **Unit Tests**: JUnit per backend
- **Integration Tests**: Spring Boot Test
- **E2E Tests**: Playwright per frontend
- **API Tests**: Test completi endpoint

### 🔒 Sicurezza

- **CORS**: Configurazione sicura
- **Validation**: Input validation completa
- **Error Handling**: Gestione errori robusta
- **Sanitization**: Pulizia dati input

---

**Sistema Enterprise-Ready** con architettura scalabile, performance ottimizzate e user experience moderna.
