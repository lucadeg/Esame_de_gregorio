import axios from 'axios'

// API Base URL / URL Base API
const API_BASE_URL = import.meta.env.VITE_API_URL || 'http://localhost:8080'

// Create axios instance / Crea istanza axios
const api = axios.create({
  baseURL: API_BASE_URL,
  timeout: 10000,
  headers: {
    'Content-Type': 'application/json',
  },
})

// Request interceptor / Intercettore richieste
api.interceptors.request.use(
  (config) => {
    console.log(`API Request: ${config.method?.toUpperCase()} ${config.url}`)
    return config
  },
  (error) => {
    console.error('API Request Error:', error)
    return Promise.reject(error)
  }
)

// Response interceptor / Intercettore risposte
api.interceptors.response.use(
  (response) => {
    console.log(`API Response: ${response.status} ${response.config.url}`)
    return response
  },
  (error) => {
    console.error('API Response Error:', error.response?.data || error.message)
    return Promise.reject(error)
  }
)

/**
 * Course API Services
 * Servizi API Corsi
 */

// Get all courses with optional filters / Recupera tutti i corsi con filtri opzionali
export const getCourses = async (filters = {}) => {
  try {
    const params = new URLSearchParams()
    
    if (filters.titolo) params.append('titolo', filters.titolo)
    if (filters.luogo) params.append('luogo', filters.luogo)
    if (filters.docente) params.append('docente', filters.docente)
    if (filters.categoria) params.append('categoria', filters.categoria)
    if (filters.livello) params.append('livello', filters.livello)
    if (filters.dataInizio) params.append('dataInizio', filters.dataInizio)
    if (filters.dataFine) params.append('dataFine', filters.dataFine)
    if (filters.durataMin) params.append('durataMin', filters.durataMin)
    if (filters.durataMax) params.append('durataMax', filters.durataMax)
    if (filters.prezzoMin) params.append('prezzoMin', filters.prezzoMin)
    if (filters.prezzoMax) params.append('prezzoMax', filters.prezzoMax)
    if (filters.disponibili) params.append('disponibili', filters.disponibili)
    
    const response = await api.get(`/courses?${params.toString()}`)
    
    // Handle different response formats / Gestisce diversi formati di risposta
    if (response.data && typeof response.data === 'object') {
      // New format with metadata / Nuovo formato con metadati
      if (response.data.courses) {
        return {
          courses: response.data.courses,
          totalFound: response.data.totalFound || 0,
          totalAvailable: response.data.totalAvailable || 0,
          filtersApplied: response.data.filtersApplied || {}
        }
      }
      // Legacy format / Formato legacy
      return {
        courses: response.data,
        totalFound: response.data.length,
        totalAvailable: response.data.length,
        filtersApplied: {}
      }
    }
    
    return {
      courses: [],
      totalFound: 0,
      totalAvailable: 0,
      filtersApplied: {}
    }
  } catch (error) {
    console.error('Error fetching courses:', error)
    
    // Handle specific error types / Gestisce tipi di errore specifici
    if (error.response) {
      const status = error.response.status
      const message = error.response.data || error.message
      
      if (status === 400) {
        throw new Error(`Errore nei parametri di ricerca: ${message}`)
      } else if (status === 500) {
        throw new Error('Errore interno del server. Riprova più tardi.')
      } else {
        throw new Error(`Errore nella ricerca: ${message}`)
      }
    } else if (error.request) {
      throw new Error('Impossibile connettersi al server. Verifica la connessione.')
    } else {
      throw new Error('Errore imprevisto durante la ricerca dei corsi.')
    }
  }
}

// Get course by ID / Recupera corso per ID
export const getCourseById = async (courseId) => {
  try {
    const response = await api.get(`/courses/${courseId}`)
    return response.data
  } catch (error) {
    console.error('Error fetching course:', error)
    throw new Error('Errore nel caricamento del corso')
  }
}

// Get upcoming courses / Recupera corsi futuri
export const getUpcomingCourses = async (startDate = null) => {
  try {
    const params = startDate ? `?dataInizio=${startDate}` : ''
    const response = await api.get(`/courses/upcoming${params}`)
    return response.data
  } catch (error) {
    console.error('Error fetching upcoming courses:', error)
    throw new Error('Errore nel caricamento dei corsi futuri')
  }
}

// Create new course / Crea nuovo corso
export const createCourse = async (courseData) => {
  try {
    const response = await api.post('/courses', courseData)
    return response.data
  } catch (error) {
    console.error('Error creating course:', error)
    
    // Handle specific error messages / Gestisce messaggi di errore specifici
    if (error.response?.status === 400) {
      throw new Error('Dati corso non validi')
    } else {
      throw new Error('Errore nella creazione del corso')
    }
  }
}

/**
 * Enrollment API Services
 * Servizi API Iscrizioni
 */

// Get all enrollments with optional course filter / Recupera tutte le iscrizioni con filtro corso opzionale
export const getEnrollments = async (courseId = null) => {
  try {
    const params = courseId ? `?corsoId=${courseId}` : ''
    const response = await api.get(`/enrollments${params}`)
    return response.data
  } catch (error) {
    console.error('Error fetching enrollments:', error)
    throw new Error('Errore nel caricamento delle iscrizioni')
  }
}

// Authentication API Services
// Servizi API Autenticazione

// User registration / Registrazione utente
export const registerUser = async (userData) => {
  try {
    console.log('API Request: POST /api/v1/auth/register', userData)
    const response = await api.post('/api/v1/auth/register', userData)
    console.log('API Response:', response.data)
    return response.data
  } catch (error) {
    console.error('Error registering user:', error)
    
    // Handle specific error responses / Gestisce risposte di errore specifiche
    if (error.response?.data) {
      const errorData = error.response.data
      if (errorData.error) {
        throw new Error(errorData.error)
      }
      if (errorData.message) {
        throw new Error(errorData.message)
      }
      if (errorData.errors) {
        // Handle validation errors / Gestisce errori di validazione
        const validationErrors = Object.values(errorData.errors).flat()
        throw new Error(validationErrors.join(', '))
      }
      // Handle specific error messages / Gestisce messaggi di errore specifici
      if (errorData.includes && errorData.includes('EMAIL_ALREADY_EXISTS')) {
        throw new Error('Un utente con questa email esiste già. Prova con un\'email diversa.')
      }
    }
    
    // Handle network errors / Gestisce errori di rete
    if (error.request) {
      throw new Error('Impossibile connettersi al server. Verifica la connessione.')
    }
    
    throw new Error('Errore durante la registrazione')
  }
}

// User login / Accesso utente
export const loginUser = async (credentials) => {
  try {
    const response = await api.post('/api/v1/auth/login', credentials)
    return response.data
  } catch (error) {
    console.error('Error logging in:', error)
    if (error.response?.data?.error) {
      throw new Error(error.response.data.error)
    }
    throw new Error('Errore durante l\'accesso')
  }
}

// Get user profile / Recupera profilo utente
export const getUserProfile = async () => {
  try {
    const response = await api.get(`/api/v1/auth/profile`)
    return response.data
  } catch (error) {
    console.error('Error fetching user profile:', error)
    throw new Error('Errore nel caricamento del profilo')
  }
}

// Update user profile / Aggiorna profilo utente
export const updateUserProfile = async (userData) => {
  try {
    const response = await api.put(`/api/v1/auth/profile`, userData)
    return response.data
  } catch (error) {
    console.error('Error updating user profile:', error)
    throw new Error('Errore nell\'aggiornamento del profilo')
  }
}

// Change user password / Cambia password utente
export const changeUserPassword = async (passwordData) => {
  try {
    const response = await api.put(`/api/v1/auth/password`, passwordData)
    return response.data
  } catch (error) {
    console.error('Error changing password:', error)
    throw new Error('Errore nel cambio password')
  }
}

// Subscription API Services
// Servizi API Abbonamenti

// Get subscription types / Recupera tipi abbonamento
export const getSubscriptionTypes = async () => {
  try {
    const response = await api.get('/subscriptions/types')
    return response.data
  } catch (error) {
    console.error('Error fetching subscription types:', error)
    throw new Error('Errore nel caricamento dei tipi abbonamento')
  }
}

// Get user subscription / Recupera abbonamento utente
export const getUserSubscription = async (userId) => {
  try {
    const response = await api.get(`/subscriptions/user/${userId}`)
    return response.data
  } catch (error) {
    console.error('Error fetching user subscription:', error)
    throw new Error('Errore nel caricamento dell\'abbonamento')
  }
}

// Update user subscription / Aggiorna abbonamento utente
export const updateUserSubscription = async (userId, subscriptionData) => {
  try {
    const response = await api.put(`/subscriptions/user/${userId}`, subscriptionData)
    return response.data
  } catch (error) {
    console.error('Error updating subscription:', error)
    throw new Error('Errore nell\'aggiornamento dell\'abbonamento')
  }
}

// Get subscription statistics / Recupera statistiche abbonamenti
export const getSubscriptionStatistics = async () => {
  try {
    const response = await api.get('/subscriptions/statistics')
    return response.data
  } catch (error) {
    console.error('Error fetching subscription statistics:', error)
    throw new Error('Errore nel caricamento delle statistiche abbonamenti')
  }
}

// Cancel user subscription / Cancella abbonamento utente
export const cancelUserSubscription = async (userId) => {
  try {
    const response = await api.delete(`/subscriptions/user/${userId}`)
    return response.data
  } catch (error) {
    console.error('Error canceling subscription:', error)
    throw new Error('Errore nella cancellazione dell\'abbonamento')
  }
}

// Get enrollment by ID / Recupera iscrizione per ID
export const getEnrollmentById = async (enrollmentId) => {
  try {
    const response = await api.get(`/enrollments/${enrollmentId}`)
    return response.data
  } catch (error) {
    console.error('Error fetching enrollment:', error)
    throw new Error('Errore nel caricamento dell\'iscrizione')
  }
}

// Create new enrollment / Crea nuova iscrizione
export const createEnrollment = async (enrollmentData) => {
  try {
    const response = await api.post('/enrollments', enrollmentData)
    return response.data
  } catch (error) {
    console.error('Error creating enrollment:', error)
    
    // Handle specific error messages / Gestisce messaggi di errore specifici
    if (error.response?.status === 409) {
      throw new Error(error.response.data || 'Partecipante già iscritto a questo corso')
    } else if (error.response?.status === 404) {
      throw new Error('Corso non trovato')
    } else if (error.response?.status === 400) {
      throw new Error('Dati non validi')
    } else {
      throw new Error('Errore nella creazione dell\'iscrizione')
    }
  }
}

// Get enrollments by course / Recupera iscrizioni per corso
export const getEnrollmentsByCourse = async (courseId) => {
  try {
    const response = await api.get(`/enrollments/course/${courseId}`)
    return response.data
  } catch (error) {
    console.error('Error fetching course enrollments:', error)
    throw new Error('Errore nel caricamento delle iscrizioni del corso')
  }
}

export default api

