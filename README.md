# 🎓 Course Management System

**Sistema completo di gestione corsi e iscrizioni** con API RESTful e frontend React moderno.

## 🚀 Quick Start

### Prerequisiti
- **Java 17+** - Backend Spring Boot
- **Node.js 18+** - Frontend React
- **Maven 3.9+** - Build tool Java

### 🏃‍♂️ Avvio Rapido

#### 1. Database H2 (Raccomandato per Sviluppo)

```powershell
# Avvia Backend
.\scripts\start\start-backend.ps1

# Avvia Frontend (in un altro terminale)
.\scripts\start\start-frontend.ps1
```

#### 2. Database PostgreSQL (Produzione)

```powershell
# Configura PostgreSQL
$env:DB_PASSWORD = "your_password"
.\scripts\setup\setup-local-postgres.ps1

# Avvia Backend con PostgreSQL
$env:DB_PASSWORD = "your_password"
.\scripts\start\start-backend.ps1
```

## 🌐 Accesso Sistema

| Servizio | URL | Descrizione |
|----------|-----|-------------|
| **Frontend** | http://localhost:5173 | Interfaccia utente React |
| **Backend API** | http://localhost:8080 | API RESTful |
| **Swagger UI** | http://localhost:8080/swagger-ui.html | Documentazione API |
| **Health Check** | http://localhost:8080/actuator/health | Monitoraggio sistema |
| **H2 Console** | http://localhost:8080/h2-console | Database H2 (se utilizzato) |

## 🏗️ Architettura

```
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   Frontend      │    │   Backend       │    │   Database      │
│   React 18      │◄──►│   Spring Boot   │◄──►│   H2/PostgreSQL │
│   Bootstrap 5   │    │   Java 17       │    │   JPA/Hibernate │
│   React Query   │    │   REST API      │    │   SQL Scripts   │
└─────────────────┘    └─────────────────┘    └─────────────────┘
```

## 🎯 Funzionalità Principali

### 📚 Gestione Corsi
- ✅ **CRUD Completo** - Creazione, lettura, aggiornamento, eliminazione
- ✅ **Ricerca Avanzata** - Filtri per titolo, luogo, docente, materia, date, durata
- ✅ **Pagine Dettaglio** - Informazioni complete per ogni corso
- ✅ **Gestione Disponibilità** - Controllo posti disponibili

### 👥 Gestione Iscrizioni
- ✅ **Iscrizione Corsi** - Processo completo di iscrizione
- ✅ **Visualizzazione Iscrizioni** - Lista con filtri e ricerca
- ✅ **Stati Iscrizione** - Attivo, completato, cancellato
- ✅ **Informazioni Partecipanti** - Dati completi studenti

### 🎨 Interfaccia Utente
- ✅ **Design Responsive** - Bootstrap 5 per tutti i dispositivi
- ✅ **3D Backgrounds** - Three.js per effetti visivi
- ✅ **Animazioni** - Transizioni fluide e moderne
- ✅ **Navigation** - Menu intuitivo e breadcrumb

## 🔌 API Endpoints

### 📚 Courses
```http
GET    /courses              # Lista corsi con filtri
GET    /courses/{id}         # Dettaglio corso
POST   /courses              # Crea corso
PUT    /courses/{id}         # Aggiorna corso
DELETE /courses/{id}         # Elimina corso
```

### 👥 Enrollments
```http
GET    /enrollments          # Lista iscrizioni
GET    /enrollments/{id}     # Dettaglio iscrizione
POST   /enrollments          # Crea iscrizione
PUT    /enrollments/{id}     # Aggiorna iscrizione
DELETE /enrollments/{id}     # Elimina iscrizione
```

## 🛠️ Tecnologie

### Backend
- **Spring Boot 3.2.0** - Framework Java
- **Java 17** - Linguaggio di programmazione
- **Spring Data JPA** - ORM e database
- **Hibernate** - Persistence layer
- **Swagger/OpenAPI** - Documentazione API
- **Spring Actuator** - Monitoraggio

### Frontend
- **React 18** - UI Framework
- **Bootstrap 5** - CSS Framework
- **React Query** - Data fetching
- **React Router** - Navigazione
- **Three.js** - Grafica 3D
- **Vite** - Build tool

### Database
- **H2 Database** - Sviluppo (in-memory)
- **PostgreSQL** - Produzione
- **JPA/Hibernate** - ORM
- **SQL Scripts** - Schema database

## 🧪 Testing

### Test E2E con Playwright

```powershell
# Installa dipendenze test
npm install
npx playwright install

# Esegui tutti i test
.\scripts\test\run-tests.ps1

# Test specifici
npx playwright test tests/database.spec.js
npx playwright test tests/api-endpoints.spec.js
npx playwright test tests/frontend-ui.spec.js
npx playwright test tests/integration.spec.js

# Test con interfaccia
npx playwright test --ui
```

### Copertura Test

- ✅ **Database Schema** - Validazione struttura
- ✅ **API Endpoints** - GET/POST/PUT/DELETE
- ✅ **Swagger UI** - Documentazione
- ✅ **Frontend UI** - Interfaccia utente
- ✅ **Integrazione** - Comunicazione frontend-backend
- ✅ **Responsive Design** - Design adattivo

## 📁 Struttura Progetto

```
Esame_De_Gregorio/
├── backend/                 # Spring Boot Backend
│   ├── src/main/java/      # Codice sorgente Java
│   ├── src/main/resources/ # Configurazioni
│   └── target/             # Classi compilate
├── frontend/               # React Frontend
│   ├── src/               # Codice sorgente React
│   ├── public/            # Assets statici
│   └── dist/              # File compilati
├── database/              # Script database
├── docs/                  # Documentazione
├── scripts/               # Script automazione
├── tests/                 # File di test
└── docker-compose.yml     # Configurazione Docker
```

## 🚀 Sviluppo

### Backend
```bash
cd backend
./mvnw spring-boot:run
```

### Frontend
```bash
cd frontend
npm install
npm run dev
```

### Docker
```bash
docker-compose up -d
```

## 📚 Documentazione

- **[System Overview](docs/SYSTEM_OVERVIEW.md)** - Panoramica sistema
- **[API Documentation](docs/API_DOCUMENTATION.md)** - Documentazione API
- **[Frontend Guide](docs/FRONTEND_GUIDE.md)** - Guida sviluppo frontend
- **[Guida Avvio](GUIDA_AVVIO.md)** - Guida dettagliata avvio

## 🔧 Configurazione

### Database
- **H2**: `jdbc:h2:mem:testdb` (Sviluppo)
- **PostgreSQL**: Configurabile per produzione

### CORS
- Configurato per `localhost:5173` e `localhost:3000`
- Supporto per sviluppo e produzione

## 📈 Performance

- **Caching**: React Query per ottimizzazione
- **Lazy Loading**: Componenti caricati on-demand
- **Debounce**: Ricerca ottimizzata
- **Compression**: Assets compressi

## 🔒 Sicurezza

- **CORS**: Configurazione sicura
- **Validation**: Input validation completa
- **Error Handling**: Gestione errori robusta
- **Sanitization**: Pulizia dati input

## 📊 Monitoraggio

- **Health Check**: `http://localhost:8080/actuator/health`
- **API Docs**: `http://localhost:8080/swagger-ui.html`
- **Frontend**: `http://localhost:5173`

---

**Sistema Enterprise-Ready** con architettura scalabile, performance ottimizzate e user experience moderna.

## 📄 Licenza

MIT License