import React, { useState, useEffect } from 'react'
import { Link } from 'react-router-dom'
import { BookOpen, MapPin, Calendar, Users, Search, Filter, TrendingUp, FileText } from 'lucide-react'
import { useQuery } from 'react-query'
import { getCourses, getEnrollments } from '../services/api'
import { Container, Row, Col, Card, Button, Badge, Form, InputGroup } from 'react-bootstrap'
import { useAuth } from '../contexts/AuthContext'
import UserProfile from '../components/UserProfile'
// import Course3DVisualization from '../components/Course3DVisualization'
// import KnowledgeFlow from '../components/KnowledgeFlow'
// import AnimatedStats from '../components/AnimatedStats'

/**
 * Home Page Component
 * Componente Pagina Home
 * 
 * Main page showing all available courses with filters
 * Pagina principale che mostra tutti i corsi disponibili con filtri
 */
const Home = () => {
  const { isAuthenticated } = useAuth()
  const [filters, setFilters] = useState({
    titolo: '',
    luogo: '',
    docente: '',
    categoria: '',
    livello: '',
    dataInizio: '',
    dataFine: '',
    durataMin: '',
    durataMax: '',
    prezzoMin: '',
    prezzoMax: '',
    disponibili: false
  })

  // Search filters state / Stato filtri di ricerca
  const [searchFilters, setSearchFilters] = useState({
    titolo: '',
    luogo: '',
    docente: '',
    categoria: '',
    livello: '',
    dataInizio: '',
    dataFine: '',
    durataMin: '',
    durataMax: '',
    prezzoMin: '',
    prezzoMax: '',
    disponibili: false
  })

  // Fetch courses with React Query / Recupera corsi con React Query
  const { data: searchData, isLoading, error, refetch } = useQuery(
    ['courses', searchFilters],
    () => getCourses(searchFilters),
    {
      staleTime: 30000, // 30 seconds / 30 secondi
      cacheTime: 300000, // 5 minutes / 5 minuti
      enabled: false, // Disabilita query automatica / Disable automatic query
      retry: 2, // Retry on failure / Riprova in caso di errore
      retryDelay: 1000 // 1 second delay / Ritardo di 1 secondo
    }
  )

  // Extract courses and metadata / Estrai corsi e metadati
  const courses = searchData?.courses || []
  const totalFound = searchData?.totalFound || 0
  const totalAvailable = searchData?.totalAvailable || 0
  const filtersApplied = searchData?.filtersApplied || {}


  // Fetch enrollments for statistics / Recupera iscrizioni per statistiche
  const { data: enrollments } = useQuery(
    'enrollments',
    () => getEnrollments(),
    {
      staleTime: 60000, // 1 minute / 1 minuto
      cacheTime: 300000, // 5 minutes / 5 minuti
    }
  )

  // Handle filter input changes with debounce / Gestisce cambiamenti input filtri con debounce
  const handleFilterChange = (key, value) => {
    setFilters(prev => ({
      ...prev,
      [key]: value
    }))
  }

  // Debounced search effect / Effetto ricerca con debounce
  useEffect(() => {
    const timeoutId = setTimeout(() => {
      setSearchFilters(filters)
      refetch()
    }, 500) // 500ms debounce

    return () => clearTimeout(timeoutId)
  }, [filters, refetch])

  // Handle search button click / Gestisce click bottone ricerca
  const handleSearch = () => {
    setSearchFilters(filters)
    refetch() // Trigger manual search / Attiva ricerca manuale
  }

  // Handle reset filters / Gestisce reset filtri
  const handleReset = () => {
    const resetFilters = {
      titolo: '',
      luogo: '',
      docente: '',
      categoria: '',
      livello: '',
      dataInizio: '',
      dataFine: '',
      durataMin: '',
      durataMax: '',
      prezzoMin: '',
      prezzoMax: '',
      disponibili: false
    }
    setFilters(resetFilters)
    setSearchFilters(resetFilters)
    refetch()
  }

  // Load all courses on component mount / Carica tutti i corsi al mount del componente
  useEffect(() => {
    refetch()
  }, [])

  // Format date for display / Formatta data per visualizzazione
  const formatDate = (dateString) => {
    return new Date(dateString).toLocaleDateString('it-IT', {
      year: 'numeric',
      month: 'long',
      day: 'numeric',
      hour: '2-digit',
      minute: '2-digit'
    })
  }

  // Get availability status / Ottieni stato disponibilità
  const getAvailabilityStatus = (disponibilita) => {
    if (disponibilita > 10) return { text: 'Disponibile', class: 'status-available' }
    if (disponibilita > 0) return { text: 'Pochi posti', class: 'status-available' }
    return { text: 'Completo', class: 'status-full' }
  }

  if (isLoading) {
    return (
      <div className="flex justify-center items-center min-h-64">
        <div className="loading-spinner"></div>
        <span className="ml-2 text-gray-600">Caricamento corsi...</span>
      </div>
    )
  }

  // Error display component / Componente visualizzazione errori
  if (error) {
    return (
      <Container className="py-4">
        <Row className="justify-content-center">
          <Col md={8}>
            <Card className="border-danger">
              <Card.Body className="text-center py-5">
                <div className="text-danger mb-4">
                  <Search size={48} className="mb-3" />
                  <h4>Errore nella Ricerca</h4>
                </div>
                <p className="text-muted mb-4">{error.message}</p>
                <div className="d-flex gap-2 justify-content-center">
                  <Button 
                    variant="primary" 
                    onClick={() => refetch()}
                    disabled={isLoading}
                  >
                    {isLoading ? 'Riprova...' : 'Riprova Ricerca'}
                  </Button>
                  <Button 
                    variant="outline-secondary" 
                    onClick={handleReset}
                  >
                    Reset Filtri
                  </Button>
                </div>
              </Card.Body>
            </Card>
          </Col>
        </Row>
      </Container>
    )
  }

  return (
    <Container className="py-4">
      {/* Header / Intestazione */}
      <Row className="text-center mb-5">
        <Col>
          <h1 className="display-4 fw-bold text-primary mb-3">
            Course Management System
          </h1>
          <p className="lead text-muted">
            Gestisci le iscrizioni ai corsi e workshop in modo efficiente
          </p>
        </Col>
      </Row>

      {/* User Profile Section / Sezione Profilo Utente */}
      {isAuthenticated() && (
        <Row className="mb-5">
          <Col>
            <UserProfile />
          </Col>
        </Row>
      )}

      {/* Statistics Section / Sezione Statistiche */}
      <Row className="g-4 mb-5">
        <Col md={6} lg={3}>
          <Card className="h-100 border-0 shadow-sm">
            <Card.Body className="d-flex align-items-center">
              <div className="bg-primary bg-opacity-10 rounded-circle d-flex align-items-center justify-content-center me-3" style={{width: '60px', height: '60px'}}>
                <BookOpen className="text-primary" size={30} />
              </div>
              <div>
                <h3 className="display-6 fw-bold text-primary mb-0">{courses?.length || 0}</h3>
                <p className="text-muted mb-0">Total Courses</p>
              </div>
            </Card.Body>
          </Card>
        </Col>
        
        <Col md={6} lg={3}>
          <Card className="h-100 border-0 shadow-sm">
            <Card.Body className="d-flex align-items-center">
              <div className="bg-success bg-opacity-10 rounded-circle d-flex align-items-center justify-content-center me-3" style={{width: '60px', height: '60px'}}>
                <Users className="text-success" size={30} />
              </div>
              <div>
                <h3 className="display-6 fw-bold text-success mb-0">{enrollments?.length || 0}</h3>
                <p className="text-muted mb-0">Enrollments</p>
              </div>
            </Card.Body>
          </Card>
        </Col>
        
        <Col md={6} lg={3}>
          <Card className="h-100 border-0 shadow-sm">
            <Card.Body className="d-flex align-items-center">
              <div className="bg-warning bg-opacity-10 rounded-circle d-flex align-items-center justify-content-center me-3" style={{width: '60px', height: '60px'}}>
                <Calendar className="text-warning" size={30} />
              </div>
              <div>
                <h3 className="display-6 fw-bold text-warning mb-0">
                  {courses?.filter(c => c.disponibilita > 0).length || 0}
                </h3>
                <p className="text-muted mb-0">Available</p>
              </div>
            </Card.Body>
          </Card>
        </Col>
        
        <Col md={6} lg={3}>
          <Card className="h-100 border-0 shadow-sm">
            <Card.Body className="d-flex align-items-center">
              <div className="bg-info bg-opacity-10 rounded-circle d-flex align-items-center justify-content-center me-3" style={{width: '60px', height: '60px'}}>
                <TrendingUp className="text-info" size={30} />
              </div>
              <div>
                <h3 className="display-6 fw-bold text-info mb-0">
                  {courses?.length > 0 ? Math.round(courses.reduce((sum, c) => sum + c.disponibilita, 0) / courses.length) : 0}
                </h3>
                <p className="text-muted mb-0">Avg Capacity</p>
              </div>
            </Card.Body>
          </Card>
        </Col>
      </Row>

      {/* Filters / Filtri */}
      <Card className="mb-5">
        <Card.Header className="bg-light">
          <div className="d-flex align-items-center">
            <Filter className="text-primary me-2" size={20} />
            <h5 className="mb-0 fw-semibold">Filtri di Ricerca</h5>
          </div>
        </Card.Header>
        <Card.Body>
          <Row className="g-3">
            <Col md={6} lg={3}>
              <Form.Label htmlFor="titolo">Titolo Corso</Form.Label>
              <Form.Control
                type="text"
                id="titolo"
                placeholder="Cerca per titolo..."
                value={filters.titolo}
                onChange={(e) => handleFilterChange('titolo', e.target.value)}
              />
            </Col>
            <Col md={6} lg={3}>
              <Form.Label htmlFor="luogo">Luogo</Form.Label>
              <Form.Control
                type="text"
                id="luogo"
                placeholder="Cerca per luogo..."
                value={filters.luogo}
                onChange={(e) => handleFilterChange('luogo', e.target.value)}
              />
            </Col>
            <Col md={6} lg={3}>
              <Form.Label htmlFor="docente">Docente</Form.Label>
              <Form.Control
                type="text"
                id="docente"
                placeholder="Nome docente..."
                value={filters.docente}
                onChange={(e) => handleFilterChange('docente', e.target.value)}
              />
            </Col>
            <Col md={6} lg={3}>
              <Form.Label htmlFor="categoria">Categoria</Form.Label>
              <Form.Select
                id="categoria"
                value={filters.categoria || ''}
                onChange={(e) => handleFilterChange('categoria', e.target.value)}
              >
                <option value="">Tutte le categorie</option>
                <option value="Frontend Development">Frontend Development</option>
                <option value="Backend Development">Backend Development</option>
                <option value="Full Stack">Full Stack</option>
                <option value="Data Science">Data Science</option>
                <option value="Mobile Development">Mobile Development</option>
              </Form.Select>
            </Col>
            <Col md={6} lg={3}>
              <Form.Label htmlFor="livello">Livello</Form.Label>
              <Form.Select
                id="livello"
                value={filters.livello || ''}
                onChange={(e) => handleFilterChange('livello', e.target.value)}
              >
                <option value="">Tutti i livelli</option>
                <option value="Principiante">Principiante</option>
                <option value="Intermedio">Intermedio</option>
                <option value="Avanzato">Avanzato</option>
                <option value="Esperto">Esperto</option>
              </Form.Select>
            </Col>
            <Col md={6} lg={3}>
              <Form.Label htmlFor="dataInizio">Data Inizio</Form.Label>
              <Form.Control
                type="date"
                id="dataInizio"
                value={filters.dataInizio}
                onChange={(e) => handleFilterChange('dataInizio', e.target.value)}
              />
            </Col>
            <Col md={6} lg={3}>
              <Form.Label htmlFor="dataFine">Data Fine</Form.Label>
              <Form.Control
                type="date"
                id="dataFine"
                value={filters.dataFine}
                onChange={(e) => handleFilterChange('dataFine', e.target.value)}
              />
            </Col>
            <Col md={6} lg={3}>
              <Form.Label htmlFor="durataMin">Durata Min (ore)</Form.Label>
              <Form.Control
                type="number"
                id="durataMin"
                placeholder="Min ore"
                min="0"
                value={filters.durataMin}
                onChange={(e) => handleFilterChange('durataMin', e.target.value)}
              />
            </Col>
            <Col md={6} lg={3}>
              <Form.Label htmlFor="durataMax">Durata Max (ore)</Form.Label>
              <Form.Control
                type="number"
                id="durataMax"
                placeholder="Max ore"
                min="0"
                value={filters.durataMax}
                onChange={(e) => handleFilterChange('durataMax', e.target.value)}
              />
            </Col>
            <Col md={6} lg={3}>
              <Form.Label htmlFor="prezzoMin">Prezzo Min (€)</Form.Label>
              <Form.Control
                type="number"
                id="prezzoMin"
                placeholder="Min prezzo"
                min="0"
                step="0.01"
                value={filters.prezzoMin}
                onChange={(e) => handleFilterChange('prezzoMin', e.target.value)}
              />
            </Col>
            <Col md={6} lg={3}>
              <Form.Label htmlFor="prezzoMax">Prezzo Max (€)</Form.Label>
              <Form.Control
                type="number"
                id="prezzoMax"
                placeholder="Max prezzo"
                min="0"
                step="0.01"
                value={filters.prezzoMax}
                onChange={(e) => handleFilterChange('prezzoMax', e.target.value)}
              />
            </Col>
            <Col md={12} className="d-flex align-items-end">
              <Form.Check
                type="checkbox"
                id="disponibili"
                label="Solo corsi disponibili"
                checked={filters.disponibili}
                onChange={(e) => handleFilterChange('disponibili', e.target.checked)}
              />
            </Col>
          </Row>
          
          {/* Search Buttons / Pulsanti Ricerca */}
          <Row className="mt-3">
            <Col className="d-flex gap-2">
              <Button 
                variant="primary" 
                onClick={handleSearch}
                disabled={isLoading}
                className="d-flex align-items-center"
              >
                <Search className="me-2" size={16} />
                {isLoading ? 'Ricerca...' : 'Cerca'}
              </Button>
              
              <Button 
                variant="outline-secondary" 
                onClick={handleReset}
                className="d-flex align-items-center"
              >
                <Filter className="me-2" size={16} />
                Reset
              </Button>
            </Col>
          </Row>
        </Card.Body>
      </Card>

      {/* Search Results Info / Informazioni Risultati Ricerca */}
      {Object.keys(filtersApplied).length > 0 && (
        <Card className="mb-4 border-info">
          <Card.Body className="py-3">
            <Row className="align-items-center">
              <Col md={8}>
                <div className="d-flex align-items-center">
                  <Search className="text-info me-2" size={20} />
                  <span className="fw-semibold text-info">
                    Ricerca con filtri: {totalFound} corsi trovati su {totalAvailable} disponibili
                  </span>
                </div>
                <div className="mt-2">
                  <small className="text-muted">
                    Filtri applicati: {Object.entries(filtersApplied).map(([key, value]) => 
                      `${key}: ${value}`
                    ).join(', ')}
                  </small>
                </div>
              </Col>
              <Col md={4} className="text-end">
                <Button 
                  variant="outline-info" 
                  size="sm"
                  onClick={handleReset}
                >
                  <Filter className="me-1" size={14} />
                  Reset Filtri
                </Button>
              </Col>
            </Row>
          </Card.Body>
        </Card>
      )}

      {/* No Results Message / Messaggio Nessun Risultato */}
      {courses?.length === 0 && !isLoading && (
        <Card className="mb-4 border-warning">
          <Card.Body className="text-center py-5">
            <div className="text-warning mb-4">
              <Search size={48} className="mb-3" />
              <h4>Nessun Corso Trovato</h4>
            </div>
            <p className="text-muted mb-4">
              Non sono stati trovati corsi che corrispondono ai criteri di ricerca.
            </p>
            <div className="d-flex gap-2 justify-content-center">
              <Button 
                variant="outline-warning" 
                onClick={handleReset}
              >
                <Filter className="me-1" size={16} />
                Reset Filtri
              </Button>
              <Button 
                variant="warning" 
                onClick={() => {
                  setSearchFilters({})
                  refetch()
                }}
              >
                Mostra Tutti i Corsi
              </Button>
            </div>
          </Card.Body>
        </Card>
      )}

      {/* Courses Grid / Griglia Corsi */}
      <Row className="g-4">
        {Array.isArray(courses) && courses.map((course) => {
          const availability = getAvailabilityStatus(course.disponibilita)
          
          return (
            <Col key={course.corsoId} md={6} lg={4}>
              <Card className="h-100 border-0 shadow-sm course-card-hover">
                <Card.Body className="d-flex flex-column">
                  <div className="d-flex align-items-start justify-content-between mb-3">
                    <div className="d-flex align-items-center">
                      <BookOpen className="text-primary me-2" size={20} />
                      <h5 className="card-title fw-bold mb-0 line-clamp-2">
                        {course.titolo}
                      </h5>
                    </div>
                    <Badge
                      bg={availability.text === 'Disponibile' ? 'success' : (availability.text === 'Pochi posti' ? 'warning' : 'danger')}
                      className="ms-3"
                    >
                      {availability.text}
                    </Badge>
                  </div>

                  <div className="mb-3">
                    <div className="d-flex align-items-center text-muted mb-2">
                      <Calendar className="me-2" size={16} />
                      <small>{formatDate(course.dataOraInizio)}</small>
                    </div>

                    <div className="d-flex align-items-center text-muted mb-2">
                      <MapPin className="me-2" size={16} />
                      <small>{course.luogo}</small>
                    </div>

                    <div className="d-flex align-items-center text-muted mb-2">
                      <Users className="me-2" size={16} />
                      <small>{course.disponibilita} posti disponibili</small>
                    </div>
                  </div>

                  <div className="d-flex gap-2 mt-auto">
                    <Button
                      as={Link}
                      to={`/create-enrollment/${course.corsoId}`}
                      variant="primary"
                      className="flex-fill"
                      disabled={course.disponibilita === 0}
                    >
                      Iscriviti
                    </Button>
                    <Button
                      as={Link}
                      to={`/course/${course.corsoId}`}
                      variant="outline-primary"
                      className="flex-fill"
                    >
                      <FileText className="me-1" size={14} />
                      Scheda Corso
                    </Button>
                    <Button
                      as={Link}
                      to={`/enrollments?corsoId=${course.corsoId}`}
                      variant="outline-secondary"
                      size="sm"
                    >
                      <Users size={14} />
                    </Button>
                  </div>
                </Card.Body>
              </Card>
            </Col>
          )
        })}
      </Row>

    </Container>
  )
}

export default Home

