import React, { useState } from 'react'
import { useParams, Link, useNavigate } from 'react-router-dom'
import { useQuery } from 'react-query'
import { getCourseById, getEnrollments } from '../services/api'
import { 
  Container, 
  Row, 
  Col, 
  Card, 
  Button, 
  Badge, 
  Alert,
  Spinner,
  Tab,
  Tabs,
  ListGroup,
  ProgressBar,
  Modal
} from 'react-bootstrap'
import { 
  BookOpen, 
  Calendar, 
  MapPin, 
  Users, 
  Clock, 
  Award, 
  Euro, 
  User, 
  FileText, 
  CheckCircle,
  ArrowLeft,
  Star,
  Share2,
  Heart,
  Download,
  Play,
  MessageCircle,
  BarChart3,
  Target,
  Zap,
  Shield,
  Globe
} from 'lucide-react'

/**
 * Course Detail Page Component
 * Componente Pagina Dettaglio Corso
 * 
 * Complete course information page with all database fields
 * Pagina informazioni complete del corso con tutti i campi del database
 */
const CourseDetail = () => {
  const { courseId } = useParams()
  const navigate = useNavigate()
  const [activeTab, setActiveTab] = useState('overview')
  const [showEnrollmentModal, setShowEnrollmentModal] = useState(false)

  // Fetch course details / Recupera dettagli corso
  const { data: course, isLoading, error } = useQuery(
    ['course', courseId],
    () => getCourseById(courseId),
    {
      enabled: !!courseId,
      staleTime: 30000,
      cacheTime: 300000,
    }
  )

  // Fetch course enrollments / Recupera iscrizioni corso
  const { data: enrollments } = useQuery(
    ['enrollments', courseId],
    () => getEnrollments(courseId),
    {
      enabled: !!courseId,
      staleTime: 30000,
      cacheTime: 300000,
    }
  )

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

  // Format duration / Formatta durata
  const formatDuration = (hours) => {
    if (!hours) return 'Non specificata'
    if (hours < 24) return `${hours} ore`
    const days = Math.floor(hours / 24)
    const remainingHours = hours % 24
    return `${days} giorni${remainingHours > 0 ? ` e ${remainingHours} ore` : ''}`
  }

  // Get availability status / Ottieni stato disponibilità
  const getAvailabilityStatus = (disponibilita) => {
    if (disponibilita > 10) return { text: 'Disponibile', variant: 'success', icon: CheckCircle }
    if (disponibilita > 0) return { text: 'Pochi posti', variant: 'warning', icon: Clock }
    return { text: 'Completo', variant: 'danger', icon: Users }
  }

  // Calculate progress percentage / Calcola percentuale progresso
  const getProgressPercentage = (progresso) => {
    if (!progresso) return 0
    return Math.min(Math.max(progresso, 0), 100)
  }

  // Handle enrollment / Gestisce iscrizione
  const handleEnrollment = () => {
    if (course?.disponibilita > 0) {
      navigate(`/create-enrollment/${courseId}`)
    } else {
      setShowEnrollmentModal(true)
    }
  }

  if (isLoading) {
    return (
      <Container className="py-5">
        <div className="d-flex justify-content-center align-items-center min-vh-50">
          <Spinner animation="border" variant="primary" />
          <span className="ms-3">Caricamento dettagli corso...</span>
        </div>
      </Container>
    )
  }

  if (error) {
    return (
      <Container className="py-5">
        <Alert variant="danger">
          <Alert.Heading>Errore nel caricamento del corso</Alert.Heading>
          <p>{error.message}</p>
          <hr />
          <div className="d-flex gap-2">
            <Button variant="outline-danger" onClick={() => navigate(-1)}>
              <ArrowLeft className="me-1" size={16} />
              Torna Indietro
            </Button>
            <Button variant="danger" onClick={() => window.location.reload()}>
              Riprova
            </Button>
          </div>
        </Alert>
      </Container>
    )
  }

  if (!course) {
    return (
      <Container className="py-5">
        <Alert variant="warning">
          <Alert.Heading>Corso non trovato</Alert.Heading>
          <p>Il corso richiesto non esiste o è stato rimosso.</p>
          <Button variant="outline-warning" onClick={() => navigate('/')}>
            Torna alla Home
          </Button>
        </Alert>
      </Container>
    )
  }

  const availability = getAvailabilityStatus(course.disponibilita)
  const progressPercentage = getProgressPercentage(course.progresso)

  return (
    <Container className="py-4">
      {/* Breadcrumb / Navigazione */}
      <Row className="mb-4">
        <Col>
          <nav aria-label="breadcrumb">
            <ol className="breadcrumb">
              <li className="breadcrumb-item">
                <Link to="/" className="text-decoration-none">
                  <BookOpen size={16} className="me-1" />
                  Corsi
                </Link>
              </li>
              <li className="breadcrumb-item active" aria-current="page">
                {course.titolo}
              </li>
            </ol>
          </nav>
        </Col>
      </Row>

      {/* Course Header / Intestazione Corso */}
      <Row className="mb-4">
        <Col>
          <Card className="border-0 shadow-sm">
            <Card.Body className="p-4">
              <Row className="align-items-start">
                <Col lg={8}>
                  <div className="d-flex align-items-start justify-content-between mb-3">
                    <div>
                      <h1 className="display-6 fw-bold text-primary mb-2">
                        {course.titolo}
                      </h1>
                      <p className="lead text-muted mb-3">
                        {course.descrizione || 'Corso professionale di alta qualità'}
                      </p>
                    </div>
                    <Badge 
                      bg={availability.variant} 
                      className="fs-6 px-3 py-2"
                    >
                      <availability.icon className="me-1" size={16} />
                      {availability.text}
                    </Badge>
                  </div>

                  {/* Quick Info / Info Rapide */}
                  <Row className="g-3 mb-4">
                    <Col sm={6} md={3}>
                      <div className="d-flex align-items-center">
                        <Calendar className="text-primary me-2" size={20} />
                        <div>
                          <small className="text-muted d-block">Data Inizio</small>
                          <strong>{formatDate(course.dataOraInizio)}</strong>
                        </div>
                      </div>
                    </Col>
                    <Col sm={6} md={3}>
                      <div className="d-flex align-items-center">
                        <Clock className="text-primary me-2" size={20} />
                        <div>
                          <small className="text-muted d-block">Durata</small>
                          <strong>{formatDuration(course.durataOre)}</strong>
                        </div>
                      </div>
                    </Col>
                    <Col sm={6} md={3}>
                      <div className="d-flex align-items-center">
                        <MapPin className="text-primary me-2" size={20} />
                        <div>
                          <small className="text-muted d-block">Luogo</small>
                          <strong>{course.luogo}</strong>
                        </div>
                      </div>
                    </Col>
                    <Col sm={6} md={3}>
                      <div className="d-flex align-items-center">
                        <Users className="text-primary me-2" size={20} />
                        <div>
                          <small className="text-muted d-block">Posti</small>
                          <strong>{course.disponibilita} disponibili</strong>
                        </div>
                      </div>
                    </Col>
                  </Row>

                  {/* Action Buttons / Pulsanti Azione */}
                  <div className="d-flex flex-wrap gap-2">
                    <Button 
                      variant="primary" 
                      size="lg"
                      onClick={handleEnrollment}
                      disabled={course.disponibilita === 0}
                      className="d-flex align-items-center"
                    >
                      <User className="me-2" size={20} />
                      {course.disponibilita > 0 ? 'Iscriviti Ora' : 'Corso Completo'}
                    </Button>
                    
                    <Button 
                      variant="outline-primary" 
                      size="lg"
                      className="d-flex align-items-center"
                    >
                      <Heart className="me-2" size={20} />
                      Salva
                    </Button>
                    
                    <Button 
                      variant="outline-secondary" 
                      size="lg"
                      className="d-flex align-items-center"
                    >
                      <Share2 className="me-2" size={20} />
                      Condividi
                    </Button>
                  </div>
                </Col>

                <Col lg={4}>
                  <Card className="bg-light border-0">
                    <Card.Body className="text-center">
                      <div className="display-4 fw-bold text-primary mb-2">
                        {course.prezzo ? `${course.prezzo}€` : 'Gratuito'}
                      </div>
                      <p className="text-muted mb-3">Prezzo del corso</p>
                      
                      {course.dataTestFinale && (
                        <div className="mb-3">
                          <small className="text-muted d-block">Test Finale</small>
                          <strong>{formatDate(course.dataTestFinale)}</strong>
                        </div>
                      )}
                      
                      {course.dataCompletamento && (
                        <div className="mb-3">
                          <small className="text-muted d-block">Completamento</small>
                          <strong>{formatDate(course.dataCompletamento)}</strong>
                        </div>
                      )}
                    </Card.Body>
                  </Card>
                </Col>
              </Row>
            </Card.Body>
          </Card>
        </Col>
      </Row>

      {/* Course Content Tabs / Tab Contenuto Corso */}
      <Row>
        <Col>
          <Card className="border-0 shadow-sm">
            <Card.Body className="p-0">
              <Tabs
                activeKey={activeTab}
                onSelect={(k) => setActiveTab(k)}
                className="border-bottom"
              >
                <Tab eventKey="overview" title="Panoramica">
                  <div className="p-4">
                    <Row>
                      <Col lg={8}>
                        <h4 className="fw-bold mb-3">Descrizione del Corso</h4>
                        <p className="text-muted mb-4">
                          {course.informazioniGenerali || course.descrizione || 
                           'Questo corso offre una formazione completa e professionale nel settore. ' +
                           'Imparerai le competenze necessarie per eccellere nel tuo campo di interesse.'}
                        </p>

                        {course.programma && (
                          <div className="mb-4">
                            <h5 className="fw-bold mb-3">Programma del Corso</h5>
                            <div className="bg-light p-3 rounded">
                              <pre className="mb-0 text-muted" style={{whiteSpace: 'pre-wrap'}}>
                                {course.programma}
                              </pre>
                            </div>
                          </div>
                        )}

                        {course.docenti && (
                          <div className="mb-4">
                            <h5 className="fw-bold mb-3">Docenti</h5>
                            <div className="d-flex align-items-center">
                              <Award className="text-primary me-2" size={20} />
                              <span>{course.docenti}</span>
                            </div>
                          </div>
                        )}
                      </Col>

                      <Col lg={4}>
                        <Card className="bg-primary bg-opacity-10 border-0">
                          <Card.Body>
                            <h5 className="fw-bold text-primary mb-3">Cosa Imparerai</h5>
                            <ListGroup variant="flush">
                              <ListGroup.Item className="border-0 bg-transparent">
                                <CheckCircle className="text-success me-2" size={16} />
                                Competenze pratiche avanzate
                              </ListGroup.Item>
                              <ListGroup.Item className="border-0 bg-transparent">
                                <CheckCircle className="text-success me-2" size={16} />
                                Metodologie professionali
                              </ListGroup.Item>
                              <ListGroup.Item className="border-0 bg-transparent">
                                <CheckCircle className="text-success me-2" size={16} />
                                Progetti reali
                              </ListGroup.Item>
                              <ListGroup.Item className="border-0 bg-transparent">
                                <CheckCircle className="text-success me-2" size={16} />
                                Certificazione finale
                              </ListGroup.Item>
                            </ListGroup>
                          </Card.Body>
                        </Card>
                      </Col>
                    </Row>
                  </div>
                </Tab>

                <Tab eventKey="schedule" title="Calendario">
                  <div className="p-4">
                    <Row>
                      <Col md={6}>
                        <h4 className="fw-bold mb-3">Date Importanti</h4>
                        <ListGroup>
                          <ListGroup.Item className="d-flex justify-content-between align-items-center">
                            <div>
                              <strong>Inizio Corso</strong>
                              <br />
                              <small className="text-muted">{formatDate(course.dataOraInizio)}</small>
                            </div>
                            <Badge bg="primary">Inizio</Badge>
                          </ListGroup.Item>
                          
                          {course.dataOraFine && (
                            <ListGroup.Item className="d-flex justify-content-between align-items-center">
                              <div>
                                <strong>Fine Corso</strong>
                                <br />
                                <small className="text-muted">{formatDate(course.dataOraFine)}</small>
                              </div>
                              <Badge bg="info">Fine</Badge>
                            </ListGroup.Item>
                          )}
                          
                          {course.dataTestFinale && (
                            <ListGroup.Item className="d-flex justify-content-between align-items-center">
                              <div>
                                <strong>Test Finale</strong>
                                <br />
                                <small className="text-muted">{formatDate(course.dataTestFinale)}</small>
                              </div>
                              <Badge bg="warning">Test</Badge>
                            </ListGroup.Item>
                          )}
                          
                          {course.dataCompletamento && (
                            <ListGroup.Item className="d-flex justify-content-between align-items-center">
                              <div>
                                <strong>Completamento</strong>
                                <br />
                                <small className="text-muted">{formatDate(course.dataCompletamento)}</small>
                              </div>
                              <Badge bg="success">Completato</Badge>
                            </ListGroup.Item>
                          )}
                        </ListGroup>
                      </Col>

                      <Col md={6}>
                        <h4 className="fw-bold mb-3">Progresso Corso</h4>
                        {course.progresso ? (
                          <div>
                            <div className="d-flex justify-content-between mb-2">
                              <span>Completamento</span>
                              <span>{progressPercentage}%</span>
                            </div>
                            <ProgressBar 
                              now={progressPercentage} 
                              variant={progressPercentage === 100 ? 'success' : 'primary'}
                              className="mb-3"
                            />
                            <p className="text-muted small">
                              {progressPercentage === 100 
                                ? 'Corso completato con successo!' 
                                : `Mancano ${100 - progressPercentage}% per completare il corso`}
                            </p>
                          </div>
                        ) : (
                          <div className="text-center text-muted">
                            <Clock size={48} className="mb-3" />
                            <p>Il corso non è ancora iniziato</p>
                          </div>
                        )}
                      </Col>
                    </Row>
                  </div>
                </Tab>

                <Tab eventKey="enrollments" title="Iscrizioni">
                  <div className="p-4">
                    <div className="d-flex justify-content-between align-items-center mb-3">
                      <h4 className="fw-bold mb-0">Partecipanti Iscritti</h4>
                      <Badge bg="info" className="fs-6">
                        {enrollments?.length || 0} iscritti
                      </Badge>
                    </div>

                    {enrollments && enrollments.length > 0 ? (
                      <Row>
                        {enrollments.map((enrollment) => (
                          <Col key={enrollment.id} md={6} lg={4} className="mb-3">
                            <Card className="h-100">
                              <Card.Body>
                                <div className="d-flex align-items-center mb-2">
                                  <div className="bg-primary bg-opacity-10 rounded-circle d-flex align-items-center justify-content-center me-3" style={{width: '40px', height: '40px'}}>
                                    <User className="text-primary" size={20} />
                                  </div>
                                  <div>
                                    <h6 className="mb-0">{enrollment.nome} {enrollment.cognome}</h6>
                                    <small className="text-muted">{enrollment.email}</small>
                                  </div>
                                </div>
                                <div className="d-flex justify-content-between align-items-center">
                                  <Badge 
                                    bg={enrollment.stato === 'completato' ? 'success' : 
                                        enrollment.stato === 'attivo' ? 'primary' : 'warning'}
                                  >
                                    {enrollment.stato}
                                  </Badge>
                                  <small className="text-muted">
                                    {formatDate(enrollment.dataIscrizione)}
                                  </small>
                                </div>
                              </Card.Body>
                            </Card>
                          </Col>
                        ))}
                      </Row>
                    ) : (
                      <div className="text-center py-5">
                        <Users size={48} className="text-muted mb-3" />
                        <h5 className="text-muted">Nessuna iscrizione</h5>
                        <p className="text-muted">Non ci sono ancora partecipanti iscritti a questo corso.</p>
                      </div>
                    )}
                  </div>
                </Tab>
              </Tabs>
            </Card.Body>
          </Card>
        </Col>
      </Row>

      {/* Enrollment Modal / Modal Iscrizione */}
      <Modal show={showEnrollmentModal} onHide={() => setShowEnrollmentModal(false)}>
        <Modal.Header closeButton>
          <Modal.Title>Corso Completo</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          <div className="text-center">
            <Users size={48} className="text-warning mb-3" />
            <h5>Posti Esauriti</h5>
            <p className="text-muted">
              Siamo spiacenti, ma questo corso ha raggiunto il numero massimo di partecipanti.
            </p>
            <p className="text-muted">
              Contattaci per essere informato sui prossimi corsi disponibili.
            </p>
          </div>
        </Modal.Body>
        <Modal.Footer>
          <Button variant="outline-secondary" onClick={() => setShowEnrollmentModal(false)}>
            Chiudi
          </Button>
          <Button variant="primary" onClick={() => navigate('/')}>
            Vedi Altri Corsi
          </Button>
        </Modal.Footer>
      </Modal>
    </Container>
  )
}

export default CourseDetail
