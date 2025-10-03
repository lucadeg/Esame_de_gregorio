# 🔌 API Documentation

## 📋 Overview

**Course Management System API** - RESTful API completa per gestione corsi e iscrizioni.

**Base URL**: `http://localhost:8080`  
**Content-Type**: `application/json`  
**Documentation**: `http://localhost:8080/swagger-ui.html`

## 🎯 Endpoints

### 📚 Courses API

#### Get All Courses
```http
GET /courses
```

**Query Parameters:**
- `titolo` (string, optional) - Filter by course title
- `luogo` (string, optional) - Filter by location
- `docente` (string, optional) - Filter by teacher
- `materia` (string, optional) - Filter by subject
- `dataInizio` (string, optional) - Filter by start date (YYYY-MM-DD)
- `dataFine` (string, optional) - Filter by end date (YYYY-MM-DD)
- `durataMin` (integer, optional) - Minimum duration in hours
- `durataMax` (integer, optional) - Maximum duration in hours
- `disponibili` (boolean, optional) - Show only available courses

**Response:**
```json
{
  "courses": [
    {
      "corsoId": 1,
      "titolo": "React Advanced",
      "descrizione": "Advanced React development",
      "dataOraInizio": "2024-01-15T09:00:00",
      "dataOraFine": "2024-01-20T17:00:00",
      "luogo": "Lecce",
      "disponibilita": 15,
      "prezzo": 299.00,
      "docenti": "Mario Rossi",
      "programma": "React hooks, state management...",
      "informazioniGenerali": "Course details...",
      "dataTestFinale": "2024-01-20T16:00:00",
      "dataCompletamento": "2024-01-20T17:00:00",
      "progresso": 100,
      "durataOre": 40
    }
  ],
  "totalFound": 1,
  "totalAvailable": 5,
  "filtersApplied": {
    "titolo": "React",
    "disponibili": true
  }
}
```

#### Get Course by ID
```http
GET /courses/{id}
```

**Response:**
```json
{
  "corsoId": 1,
  "titolo": "React Advanced",
  "descrizione": "Advanced React development",
  "dataOraInizio": "2024-01-15T09:00:00",
  "dataOraFine": "2024-01-20T17:00:00",
  "luogo": "Lecce",
  "disponibilita": 15,
  "prezzo": 299.00,
  "docenti": "Mario Rossi",
  "programma": "React hooks, state management...",
  "informazioniGenerali": "Course details...",
  "dataTestFinale": "2024-01-20T16:00:00",
  "dataCompletamento": "2024-01-20T17:00:00",
  "progresso": 100,
  "durataOre": 40
}
```

#### Create Course
```http
POST /courses
```

**Request Body:**
```json
{
  "titolo": "New Course",
  "descrizione": "Course description",
  "dataOraInizio": "2024-02-01T09:00:00",
  "dataOraFine": "2024-02-05T17:00:00",
  "luogo": "Milano",
  "disponibilita": 20,
  "prezzo": 199.00,
  "docenti": "Giulia Bianchi",
  "programma": "Course program...",
  "informazioniGenerali": "General information...",
  "durataOre": 30
}
```

#### Update Course
```http
PUT /courses/{id}
```

#### Delete Course
```http
DELETE /courses/{id}
```

### 👥 Enrollments API

#### Get All Enrollments
```http
GET /enrollments
```

**Query Parameters:**
- `corsoId` (integer, optional) - Filter by course ID

**Response:**
```json
[
  {
    "id": 1,
    "nome": "Mario",
    "cognome": "Rossi",
    "email": "mario.rossi@email.com",
    "dataIscrizione": "2024-01-10T10:00:00",
    "stato": "attivo",
    "corso": {
      "corsoId": 1,
      "titolo": "React Advanced",
      "luogo": "Lecce",
      "dataOraInizio": "2024-01-15T09:00:00"
    }
  }
]
```

#### Get Enrollment by ID
```http
GET /enrollments/{id}
```

#### Create Enrollment
```http
POST /enrollments
```

**Request Body:**
```json
{
  "nome": "Mario",
  "cognome": "Rossi",
  "email": "mario.rossi@email.com",
  "corsoId": 1
}
```

#### Update Enrollment
```http
PUT /enrollments/{id}
```

#### Delete Enrollment
```http
DELETE /enrollments/{id}
```

## 🔧 Error Handling

### Error Responses

#### 400 Bad Request
```json
{
  "error": "Formato data inizio non valido. Usa YYYY-MM-DD"
}
```

#### 404 Not Found
```json
{
  "error": "Corso non trovato"
}
```

#### 500 Internal Server Error
```json
{
  "error": "Errore interno del server durante la ricerca dei corsi"
}
```

## 🚀 Usage Examples

### Frontend Integration

```javascript
// Get courses with filters
const courses = await getCourses({
  titolo: 'React',
  luogo: 'Lecce',
  disponibili: true
})

// Get course details
const course = await getCourseById(1)

// Create enrollment
const enrollment = await createEnrollment({
  nome: 'Mario',
  cognome: 'Rossi',
  email: 'mario.rossi@email.com',
  corsoId: 1
})
```

### cURL Examples

```bash
# Get all courses
curl -X GET "http://localhost:8080/courses"

# Get courses with filters
curl -X GET "http://localhost:8080/courses?titolo=React&disponibili=true"

# Get course by ID
curl -X GET "http://localhost:8080/courses/1"

# Create course
curl -X POST "http://localhost:8080/courses" \
  -H "Content-Type: application/json" \
  -d '{"titolo":"New Course","luogo":"Milano","disponibilita":20}'
```

## 📊 Response Codes

| Code | Description |
|------|-------------|
| 200  | Success |
| 201  | Created |
| 400  | Bad Request |
| 404  | Not Found |
| 500  | Internal Server Error |

## 🔒 CORS Configuration

**Allowed Origins:**
- `http://localhost:5173` (Vite dev server)
- `http://localhost:3000` (React dev server)
- `http://127.0.0.1:5173` (Alternative localhost)

**Allowed Methods:**
- GET, POST, PUT, DELETE, OPTIONS

**Allowed Headers:**
- Content-Type, Authorization, X-Requested-With

---

**API Enterprise-Ready** con documentazione completa, gestione errori robusta e integrazione frontend ottimizzata.
