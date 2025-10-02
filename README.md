# Course Management System / Sistema di Gestione Corsi

## Project Overview / Panoramica Progetto

**Timestamp**: 2025-10-02 10:11:35

A complete course management system with RESTful API and React frontend, built with modern technologies and Docker containerization.

Sistema completo di gestione corsi con API RESTful e frontend React, costruito con tecnologie moderne e containerizzazione Docker.

## Technology Stack / Stack Tecnologico

- **Backend**: Java 17, Spring Boot 3.2.0, Hibernate, PostgreSQL
- **Frontend**: React 18, Vite, Tailwind CSS
- **Database**: PostgreSQL 15
- **Containerization**: Docker, Docker Compose
- **Documentation**: Swagger UI / OpenAPI 3.0
- **Build Tools**: Maven, Vite

## Project Structure / Struttura Progetto

```
├── backend/                 # Spring Boot API
│   ├── src/main/java/      # Java source code
│   ├── src/main/resources/ # Configuration files
│   └── Dockerfile          # Backend container
├── frontend/               # React application
│   ├── src/               # React source code
│   ├── public/            # Static assets
│   └── Dockerfile         # Frontend container
├── database/              # Database scripts
│   └── schema.sql        # Database schema
└── docker-compose.yml    # Multi-container setup
```

## Features / Funzionalità

### Database (5 points / 5 punti)
- ✅ **ER Model**: Complete entity relationship model
- ✅ **SQL Scripts**: Database creation and sample data
- ✅ **Triggers**: Automatic availability updates

### API Endpoints (11 points / 11 punti)
- ✅ **GET /courses**: Retrieve all courses with filters
- ✅ **GET /enrollments**: Retrieve enrollments with course filter
- ✅ **POST /enrollments**: Create new enrollment
- ✅ **Swagger UI**: Complete API documentation

### Frontend (9-12 points / 9-12 punti)
- ✅ **Home Page**: Course listing with filters
- ✅ **Enrollments**: View all enrollments with course filtering
- ✅ **Create Enrollment**: Form for new registrations
- ✅ **Responsive Design**: Mobile-friendly interface

## Quick Start / Avvio Rapido

### Prerequisites / Prerequisiti
- Docker and Docker Compose
- Java 17+ (for local development)
- Node.js 18+ (for local development)

### Using Docker / Usando Docker

1. **Clone repository / Clona repository**
```bash
git clone https://github.com/lucadeg/Esame_de_gregorio.git
cd Esame_de_gregorio
```

2. **Start all services / Avvia tutti i servizi**
```bash
docker-compose up -d
```

3. **Access applications / Accedi alle applicazioni**
- Frontend: http://localhost:5173
- Backend API: http://localhost:8080/api
- Swagger UI: http://localhost:8080/swagger-ui.html
- Database: localhost:5432

### Local Development / Sviluppo Locale

1. **Start database / Avvia database**
```bash
docker-compose up postgres -d
```

2. **Backend development / Sviluppo backend**
```bash
cd backend
./mvnw spring-boot:run
```

3. **Frontend development / Sviluppo frontend**
```bash
cd frontend
npm install
npm run dev
```

## API Documentation / Documentazione API

### Courses Endpoints / Endpoint Corsi

- `GET /api/courses` - Get all courses
- `GET /api/courses/{id}` - Get course by ID
- `GET /api/courses/upcoming` - Get upcoming courses

### Enrollments Endpoints / Endpoint Iscrizioni

- `GET /api/enrollments` - Get all enrollments
- `GET /api/enrollments/{id}` - Get enrollment by ID
- `POST /api/enrollments` - Create new enrollment
- `GET /api/enrollments/course/{courseId}` - Get enrollments by course

### Query Parameters / Parametri Query

- `titolo` - Filter by course title
- `luogo` - Filter by location
- `disponibili` - Show only available courses
- `corsoId` - Filter enrollments by course

## Database Schema / Schema Database

### Tables / Tabelle

**corsi** (courses)
- `corso_id` (PK) - Course identifier
- `titolo` - Course title
- `data_ora_inizio` - Start date and time
- `luogo` - Location
- `disponibilita` - Available spots

**iscrizioni** (enrollments)
- `iscrizione_id` (PK) - Enrollment identifier
- `corso_id` (FK) - Course reference
- `partecipante_nome` - Participant first name
- `partecipante_cognome` - Participant last name
- `partecipante_email` - Participant email
- `data_ora_iscrizione` - Enrollment timestamp

## Development Guidelines / Linee Guida Sviluppo

- All code is written in English
- Comments are bilingual (English/Italian)
- Follow enterprise-level best practices
- Use Docker for containerization
- Implement comprehensive logging

## Testing / Test

```bash
# Backend tests / Test backend
cd backend
./mvnw test

# Frontend tests / Test frontend
cd frontend
npm test

# Integration tests / Test integrazione
docker-compose up -d
# Run tests against running containers
```

## Deployment / Deploy

The application is containerized and ready for deployment on any Docker-compatible platform.

L'applicazione è containerizzata e pronta per il deploy su qualsiasi piattaforma compatibile con Docker.

## License / Licenza

MIT License - see LICENSE file for details.

Licenza MIT - vedi file LICENSE per i dettagli.