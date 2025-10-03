import React, { useState, useEffect } from 'react'
import { useParams, useNavigate, Link } from 'react-router-dom'
import { useForm } from 'react-hook-form'
import { useMutation, useQuery } from 'react-query'
import { createEnrollment, getCourseById, getCourses } from '../services/api'
import { User, Mail, BookOpen, Calendar, MapPin, Users, ArrowLeft } from 'lucide-react'
import { Container, Row, Col, Card, Form, Button, Alert, Spinner } from 'react-bootstrap'
import toast from 'react-hot-toast'

/**
 * Create Enrollment Page Component
 * Componente Pagina Crea Iscrizione
 * 
 * Page for creating new course enrollments
 * Pagina per creare nuove iscrizioni ai corsi
 */
const CreateEnrollment = () => {
  const { courseId } = useParams()
  const navigate = useNavigate()
  const [selectedCourse, setSelectedCourse] = useState(null)

  // Form handling with React Hook Form / Gestione form con React Hook Form
  const {
    register,
    handleSubmit,
    formState: { errors },
    setValue,
    watch
  } = useForm({
    defaultValues: {
      corsoId: courseId || '',
      partecipanteNome: '',
      partecipanteCognome: '',
      partecipanteEmail: ''
    }
  })

  // Fetch courses for dropdown / Recupera corsi per dropdown
  const { data: coursesData } = useQuery('courses', () => getCourses())
  const courses = coursesData?.courses || []

  // Fetch specific course if courseId is provided / Recupera corso specifico se courseId è fornito
  const { data: course, isLoading: courseLoading } = useQuery(
    ['course', courseId],
    () => getCourseById(courseId),
    {
      enabled: !!courseId,
      onSuccess: (data) => {
        setSelectedCourse(data)
        setValue('corsoId', data.corsoId)
      }
    }
  )

  // Create enrollment mutation / Mutazione crea iscrizione
  const createEnrollmentMutation = useMutation(createEnrollment, {
    onSuccess: (data) => {
      toast.success('Iscrizione creata con successo!')
      navigate('/enrollments')
    },
    onError: (error) => {
      toast.error(error.message || 'Errore nella creazione dell\'iscrizione')
    }
  })

  // Handle course selection change / Gestisce cambio selezione corso
  const handleCourseChange = (selectedCourseId) => {
    const course = courses?.find(c => c.corsoId === parseInt(selectedCourseId))
    setSelectedCourse(course)
    setValue('corsoId', selectedCourseId)
  }

  // Handle form submission / Gestisce invio form
  const onSubmit = (data) => {
    createEnrollmentMutation.mutate({
      corsoId: parseInt(data.corsoId),
      partecipanteNome: data.partecipanteNome,
      partecipanteCognome: data.partecipanteCognome,
      partecipanteEmail: data.partecipanteEmail
    })
  }

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
    if (disponibilita > 10) return { text: 'Disponibile', class: 'bg-success' }
    if (disponibilita > 0) return { text: 'Pochi posti', class: 'bg-warning' }
    return { text: 'Completo', class: 'bg-danger' }
  }

  if (courseLoading) {
    return (
      <Container className="py-5">
        <div className="d-flex justify-content-center align-items-center" style={{ minHeight: '200px' }}>
          <Spinner animation="border" variant="primary" className="me-3" />
          <span className="text-muted">Caricamento corso...</span>
        </div>
      </Container>
    )
  }

  return (
    <Container className="py-5">
      {/* Header / Intestazione */}
      <Row className="mb-4">
        <Col>
          <div className="d-flex justify-content-between align-items-center">
            <div>
              <h1 className="display-4 fw-bold text-primary d-flex align-items-center">
                <User className="me-3" size={32} />
                Nuova Iscrizione
              </h1>
              <p className="text-muted">
                Registra un nuovo partecipante a un corso
              </p>
            </div>
            
            <Button
              as={Link}
              to="/"
              variant="outline-secondary"
              className="d-flex align-items-center"
            >
              <ArrowLeft size={16} className="me-2" />
              Torna ai Corsi
            </Button>
          </div>
        </Col>
      </Row>

      <Row>
        {/* Course Selection / Selezione Corso */}
        <Col lg={4}>
          <Card className="h-100">
            <Card.Header>
              <h2 className="h5 fw-bold text-primary d-flex align-items-center mb-0">
                <BookOpen size={20} className="me-2" />
                Selezione Corso
              </h2>
            </Card.Header>
            <Card.Body>
              <Form.Group className="mb-3">
                <Form.Label>Corso</Form.Label>
                <Form.Select
                  {...register('corsoId', { required: 'Seleziona un corso' })}
                  onChange={(e) => handleCourseChange(e.target.value)}
                  disabled={!!courseId}
                  isInvalid={!!errors.corsoId}
                >
                  <option value="">Seleziona un corso...</option>
                  {Array.isArray(courses) && courses.map((course) => (
                    <option key={course.corsoId} value={course.corsoId}>
                      {course.titolo}
                    </option>
                  ))}
                </Form.Select>
                {errors.corsoId && (
                  <Form.Control.Feedback type="invalid">
                    {errors.corsoId.message}
                  </Form.Control.Feedback>
                )}
              </Form.Group>

              {selectedCourse && (
                <div className="border-top pt-3">
                  <h3 className="h6 fw-bold text-dark mb-3">Dettagli Corso</h3>
                  <div className="small text-muted">
                    <div className="d-flex align-items-center mb-2">
                      <Calendar size={16} className="me-2" />
                      <span>{formatDate(selectedCourse.dataOraInizio)}</span>
                    </div>
                    <div className="d-flex align-items-center mb-2">
                      <MapPin size={16} className="me-2" />
                      <span>{selectedCourse.luogo}</span>
                    </div>
                    <div className="d-flex align-items-center mb-2">
                      <Users size={16} className="me-2" />
                      <span>{selectedCourse.disponibilita} posti disponibili</span>
                    </div>
                    <div className="mt-2">
                      <span className={`badge ${getAvailabilityStatus(selectedCourse.disponibilita).class}`}>
                        {getAvailabilityStatus(selectedCourse.disponibilita).text}
                      </span>
                    </div>
                  </div>
                </div>
              )}
            </Card.Body>
          </Card>
        </Col>

        {/* Enrollment Form / Form Iscrizione */}
        <Col lg={8}>
          <Card>
            <Card.Header>
              <h2 className="h5 fw-bold text-primary d-flex align-items-center mb-0">
                <User size={20} className="me-2" />
                Dati Partecipante
              </h2>
            </Card.Header>
            <Card.Body>
              <Form onSubmit={handleSubmit(onSubmit)}>
                <Row>
                  <Col md={6}>
                    <Form.Group className="mb-3">
                      <Form.Label>Nome *</Form.Label>
                      <div className="position-relative">
                        <Form.Control
                          type="text"
                          className="ps-5"
                          placeholder="Nome del partecipante"
                          {...register('partecipanteNome', {
                            required: 'Il nome è obbligatorio',
                            maxLength: {
                              value: 30,
                              message: 'Il nome non può superare i 30 caratteri'
                            }
                          })}
                          isInvalid={!!errors.partecipanteNome}
                        />
                        <User className="position-absolute top-50 start-0 translate-middle-y text-muted ms-3" size={18} />
                      </div>
                      {errors.partecipanteNome && (
                        <Form.Control.Feedback type="invalid">
                          {errors.partecipanteNome.message}
                        </Form.Control.Feedback>
                      )}
                    </Form.Group>
                  </Col>

                  <Col md={6}>
                    <Form.Group className="mb-3">
                      <Form.Label>Cognome *</Form.Label>
                      <div className="position-relative">
                        <Form.Control
                          type="text"
                          className="ps-5"
                          placeholder="Cognome del partecipante"
                          {...register('partecipanteCognome', {
                            required: 'Il cognome è obbligatorio',
                            maxLength: {
                              value: 30,
                              message: 'Il cognome non può superare i 30 caratteri'
                            }
                          })}
                          isInvalid={!!errors.partecipanteCognome}
                        />
                        <User className="position-absolute top-50 start-0 translate-middle-y text-muted ms-3" size={18} />
                      </div>
                      {errors.partecipanteCognome && (
                        <Form.Control.Feedback type="invalid">
                          {errors.partecipanteCognome.message}
                        </Form.Control.Feedback>
                      )}
                    </Form.Group>
                  </Col>
                </Row>

                <Form.Group className="mb-4">
                  <Form.Label>Email *</Form.Label>
                  <div className="position-relative">
                    <Form.Control
                      type="email"
                      className="ps-5"
                      placeholder="email@esempio.com"
                      {...register('partecipanteEmail', {
                        required: 'L\'email è obbligatoria',
                        pattern: {
                          value: /^[A-Z0-9._%+-]+@[A-Z0-9.-]+\.[A-Z]{2,}$/i,
                          message: 'Inserisci un indirizzo email valido'
                        },
                        maxLength: {
                          value: 50,
                          message: 'L\'email non può superare i 50 caratteri'
                        }
                      })}
                      isInvalid={!!errors.partecipanteEmail}
                    />
                    <Mail className="position-absolute top-50 start-0 translate-middle-y text-muted ms-3" size={18} />
                  </div>
                  {errors.partecipanteEmail && (
                    <Form.Control.Feedback type="invalid">
                      {errors.partecipanteEmail.message}
                    </Form.Control.Feedback>
                  )}
                </Form.Group>

                <div className="d-flex justify-content-end gap-3 pt-3 border-top">
                  <Button
                    as={Link}
                    to="/"
                    variant="outline-secondary"
                  >
                    Annulla
                  </Button>
                  <Button
                    type="submit"
                    variant="primary"
                    disabled={createEnrollmentMutation.isLoading || !selectedCourse}
                  >
                    {createEnrollmentMutation.isLoading ? (
                      <>
                        <Spinner size="sm" className="me-2" />
                        Creazione...
                      </>
                    ) : (
                      'Crea Iscrizione'
                    )}
                  </Button>
                </div>
              </Form>
            </Card.Body>
          </Card>
        </Col>
      </Row>
    </Container>
  )
}

export default CreateEnrollment

