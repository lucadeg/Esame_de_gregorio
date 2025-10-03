import React, { useState } from 'react'
import { useSearchParams, Link } from 'react-router-dom'
import { Users, Filter, Calendar, Mail, User, BookOpen, Clock, MapPin, Award, Euro } from 'lucide-react'
import { useQuery } from 'react-query'
import { getEnrollments, getCourses } from '../services/api'

/**
 * Enhanced Enrollments Page Component
 * Componente Pagina Iscrizioni Migliorata
 * 
 * Page showing all enrollments with complete course information
 * Pagina che mostra tutte le iscrizioni con informazioni complete del corso
 */
const EnrollmentsEnhanced = () => {
  const [searchParams, setSearchParams] = useSearchParams()
  const [filters, setFilters] = useState({
    corsoId: searchParams.get('corsoId') || '',
    search: ''
  })

  // Fetch enrollments with React Query / Recupera iscrizioni con React Query
  const { data: enrollments, isLoading, error, refetch } = useQuery(
    ['enrollments', filters.corsoId],
    () => getEnrollments(filters.corsoId ? parseInt(filters.corsoId) : null),
    {
      staleTime: 30000, // 30 seconds / 30 secondi
      cacheTime: 300000, // 5 minutes / 5 minuti
    }
  )

  // Fetch courses for filter dropdown / Recupera corsi per dropdown filtro
  const { data: coursesData } = useQuery('courses', () => getCourses())
  const courses = coursesData?.courses || []

  // Handle filter changes / Gestisce cambiamenti filtri
  const handleFilterChange = (key, value) => {
    setFilters(prev => ({
      ...prev,
      [key]: value
    }))
    
    if (key === 'corsoId') {
      const newParams = new URLSearchParams(searchParams)
      if (value) {
        newParams.set('corsoId', value)
      } else {
        newParams.delete('corsoId')
      }
      setSearchParams(newParams)
    }
  }

  // Filter enrollments by search term / Filtra iscrizioni per termine di ricerca
  const filteredEnrollments = Array.isArray(enrollments) ? enrollments.filter(enrollment => {
    if (!filters.search) return true
    
    const searchTerm = filters.search.toLowerCase()
    return (
      enrollment.partecipanteNome.toLowerCase().includes(searchTerm) ||
      enrollment.partecipanteCognome.toLowerCase().includes(searchTerm) ||
      enrollment.partecipanteEmail.toLowerCase().includes(searchTerm)
    )
  }) : []

  // Get course information for each enrollment / Ottieni informazioni corso per ogni iscrizione
  const getCourseInfo = (corsoId) => {
    return Array.isArray(courses) ? courses.find(course => course.corsoId === corsoId) : null
  }

  // Format date for display / Formatta data per visualizzazione
  const formatDate = (dateString) => {
    return new Date(dateString).toLocaleDateString('it-IT', {
      year: 'numeric',
      month: 'long',
      day: 'numeric'
    })
  }

  // Format time for display / Formatta ora per visualizzazione
  const formatTime = (dateString) => {
    return new Date(dateString).toLocaleTimeString('it-IT', {
      hour: '2-digit',
      minute: '2-digit'
    })
  }

  if (isLoading) {
    return (
      <div className="flex justify-center items-center min-h-64">
        <div className="loading-spinner"></div>
        <span className="ml-2 text-gray-600">Caricamento iscrizioni...</span>
      </div>
    )
  }

  // Debug: Log per vedere cosa sta succedendo
  console.log('EnrollmentsEnhanced - isLoading:', isLoading)
  console.log('EnrollmentsEnhanced - error:', error)
  console.log('EnrollmentsEnhanced - enrollments:', enrollments)

  if (error) {
    return (
      <div className="text-center py-8">
        <div className="text-red-600 mb-4">Errore nel caricamento delle iscrizioni</div>
        <button onClick={() => refetch()} className="btn-primary">
          Riprova
        </button>
      </div>
    )
  }

  return (
    <div className="space-y-6">
      {/* Header / Intestazione */}
      <div className="flex items-center justify-between">
        <div>
          <h1 className="text-3xl font-bold text-gray-900 flex items-center">
            <Users className="w-8 h-8 mr-3 text-primary-600" />
            Gestione Iscrizioni
          </h1>
          <p className="text-gray-600 mt-2">
            Visualizza e gestisci tutte le iscrizioni ai corsi
          </p>
        </div>
        
      </div>

      {/* Filters / Filtri */}
      <div className="card">
        <div className="flex items-center mb-4">
          <Filter className="w-5 h-5 text-gray-500 mr-2" />
          <h2 className="text-lg font-semibold text-gray-800">Filtri di Ricerca</h2>
        </div>
        
        <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
          <div>
            <label className="form-label">Corso</label>
            <select
              className="form-input"
              value={filters.corsoId}
              onChange={(e) => handleFilterChange('corsoId', e.target.value)}
            >
              <option value="">Tutti i corsi</option>
              {Array.isArray(courses) && courses.map((course) => (
                <option key={course.corsoId} value={course.corsoId}>
                  {course.titolo}
                </option>
              ))}
            </select>
          </div>
          
          <div>
            <label className="form-label">Cerca Partecipante</label>
            <input
              type="text"
              className="form-input"
              placeholder="Nome, cognome o email..."
              value={filters.search}
              onChange={(e) => handleFilterChange('search', e.target.value)}
            />
          </div>
        </div>
      </div>

      {/* Statistics / Statistiche */}
      <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
        <div className="card text-center">
          <div className="text-2xl font-bold text-primary-600">
            {enrollments?.length || 0}
          </div>
          <div className="text-sm text-gray-600">Totale Iscrizioni</div>
        </div>
        <div className="card text-center">
          <div className="text-2xl font-bold text-green-600">
            {courses?.length || 0}
          </div>
          <div className="text-sm text-gray-600">Corsi Disponibili</div>
        </div>
        <div className="card text-center">
          <div className="text-2xl font-bold text-blue-600">
            {filteredEnrollments.length}
          </div>
          <div className="text-sm text-gray-600">Risultati Filtrati</div>
        </div>
      </div>

      {/* Enrollments List / Lista Iscrizioni */}
      <div className="space-y-4">
        {filteredEnrollments.length === 0 ? (
          <div className="text-center py-8">
            <Users className="w-16 h-16 text-gray-300 mx-auto mb-4" />
            <h3 className="text-lg font-medium text-gray-900 mb-2">Nessuna iscrizione trovata</h3>
            <p className="text-gray-600 mb-4">Prova a modificare i filtri di ricerca</p>
            <Link to="/create-enrollment" className="btn-primary">
              Crea Prima Iscrizione
            </Link>
          </div>
        ) : (
          filteredEnrollments.map((enrollment) => {
            const courseInfo = getCourseInfo(enrollment.corsoId)
            return (
              <div key={enrollment.iscrizioneId} className="card hover:shadow-md transition-shadow">
                <div className="flex items-start justify-between">
                  <div className="flex-1">
                    <div className="flex items-center mb-3">
                      <User className="w-5 h-5 text-gray-500 mr-2" />
                      <h3 className="text-lg font-semibold text-gray-900">
                        {enrollment.partecipanteNome} {enrollment.partecipanteCognome}
                      </h3>
                    </div>
                    
                    <div className="grid grid-cols-1 md:grid-cols-2 gap-4 text-sm text-gray-600 mb-4">
                      <div className="flex items-center">
                        <Mail className="w-4 h-4 mr-2" />
                        <span>{enrollment.partecipanteEmail}</span>
                      </div>
                      
                      <div className="flex items-center">
                        <Calendar className="w-4 h-4 mr-2" />
                        <span>Iscritto il {formatDate(enrollment.dataOraIscrizione)} alle ore {formatTime(enrollment.dataOraIscrizione)}</span>
                      </div>
                    </div>

                    {courseInfo && (
                      <div className="mt-4 p-4 bg-gradient-to-r from-blue-50 to-indigo-50 rounded-lg border border-blue-200">
                        <div className="flex items-center mb-3">
                          <BookOpen className="w-5 h-5 mr-2 text-blue-600" />
                          <span className="font-semibold text-gray-900 text-lg">{courseInfo.titolo}</span>
                        </div>
                        
                        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4 text-sm">
                          <div className="flex items-center">
                            <Calendar className="w-4 h-4 mr-2 text-gray-500" />
                            <div>
                              <strong className="text-gray-700">Data inizio:</strong>
                              <div className="text-gray-600">{formatDate(courseInfo.dataOraInizio)}</div>
                            </div>
                          </div>
                          
                          <div className="flex items-center">
                            <MapPin className="w-4 h-4 mr-2 text-gray-500" />
                            <div>
                              <strong className="text-gray-700">Luogo:</strong>
                              <div className="text-gray-600">{courseInfo.luogo}</div>
                            </div>
                          </div>
                          
                          {courseInfo.docenti && (
                            <div className="flex items-center">
                              <User className="w-4 h-4 mr-2 text-gray-500" />
                              <div>
                                <strong className="text-gray-700">Docenti:</strong>
                                <div className="text-gray-600">{courseInfo.docenti}</div>
                              </div>
                            </div>
                          )}
                          
                          {courseInfo.livello && (
                            <div>
                              <strong className="text-gray-700">Livello:</strong>
                              <div className="text-gray-600">{courseInfo.livello}</div>
                            </div>
                          )}
                          
                          {courseInfo.durataOre && (
                            <div className="flex items-center">
                              <Clock className="w-4 h-4 mr-2 text-gray-500" />
                              <div>
                                <strong className="text-gray-700">Durata:</strong>
                                <div className="text-gray-600">{courseInfo.durataOre} ore</div>
                              </div>
                            </div>
                          )}
                          
                          {courseInfo.dataTest && (
                            <div>
                              <strong className="text-gray-700">Data test:</strong>
                              <div className="text-gray-600">{formatDate(courseInfo.dataTest)}</div>
                            </div>
                          )}
                          
                          {courseInfo.dataCompletamento && (
                            <div>
                              <strong className="text-gray-700">Data completamento:</strong>
                              <div className="text-gray-600">{formatDate(courseInfo.dataCompletamento)}</div>
                            </div>
                          )}
                          
                          {courseInfo.prezzo && (
                            <div className="flex items-center">
                              <Euro className="w-4 h-4 mr-2 text-gray-500" />
                              <div>
                                <strong className="text-gray-700">Prezzo:</strong>
                                <div className="text-gray-600">€{courseInfo.prezzo}</div>
                              </div>
                            </div>
                          )}
                          
                          {courseInfo.certificazione && (
                            <div className="flex items-center">
                              <Award className="w-4 h-4 mr-2 text-green-500" />
                              <div>
                                <strong className="text-gray-700">Certificazione:</strong>
                                <div className="text-green-600 font-medium">Inclusa</div>
                              </div>
                            </div>
                          )}
                        </div>
                        
                        {courseInfo.programma && (
                          <div className="mt-4">
                            <strong className="text-sm text-gray-700 font-medium">Programma del Corso:</strong>
                            <div className="text-sm text-gray-600 mt-2 p-3 bg-white rounded border whitespace-pre-line">
                              {courseInfo.programma}
                            </div>
                          </div>
                        )}
                        
                        {courseInfo.informazioniGenerali && (
                          <div className="mt-4">
                            <strong className="text-sm text-gray-700 font-medium">Informazioni Generali:</strong>
                            <div className="text-sm text-gray-600 mt-2 p-3 bg-white rounded border">
                              {courseInfo.informazioniGenerali}
                            </div>
                          </div>
                        )}
                      </div>
                    )}
                  </div>
                  
                  <div className="flex items-center space-x-2">
                    <span className="px-3 py-1 bg-green-100 text-green-800 text-sm font-medium rounded-full">
                      Attiva
                    </span>
                  </div>
                </div>
              </div>
            )
          })
        )}
      </div>
    </div>
  )
}

export default EnrollmentsEnhanced
