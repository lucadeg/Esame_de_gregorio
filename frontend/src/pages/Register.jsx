import React, { useState } from 'react'
import { Link, useNavigate } from 'react-router-dom'
import { Container, Row, Col, Card, Form, Button, Alert, Spinner } from 'react-bootstrap'
import { UserPlus, User, Mail, Lock, Phone, Calendar, ArrowLeft } from 'lucide-react'
import { registerUser } from '../services/api'

/**
 * Register Page Component
 * Componente Pagina Registrazione
 * 
 * User registration page with form validation
 * Pagina registrazione utente con validazione form
 */
const Register = () => {
  const navigate = useNavigate()
  const [formData, setFormData] = useState({
    nome: '',
    cognome: '',
    email: '',
    password: '',
    confirmPassword: '',
    phoneNumber: '',
    dateOfBirth: ''
  })
  const [isLoading, setIsLoading] = useState(false)
  const [error, setError] = useState('')
  const [success, setSuccess] = useState('')

  // Handle form input changes / Gestisce cambiamenti input form
  const handleInputChange = (e) => {
    const { name, value } = e.target
    setFormData(prev => ({
      ...prev,
      [name]: value
    }))
    // Clear errors when user starts typing / Pulisci errori quando utente inizia a digitare
    if (error) setError('')
    if (success) setSuccess('')
  }

  // Validate form / Valida form
  const validateForm = () => {
    if (formData.password !== formData.confirmPassword) {
      setError('Le password non coincidono')
      return false
    }
    if (formData.password.length < 6) {
      setError('La password deve essere di almeno 6 caratteri')
      return false
    }
    return true
  }

  // Handle form submission / Gestisce invio form
  const handleSubmit = async (e) => {
    e.preventDefault()
    
    if (!validateForm()) return
    
    setIsLoading(true)
    setError('')
    setSuccess('')

    try {
      const { confirmPassword, dateOfBirth, ...userData } = formData
      
      // Prepare data for backend / Prepara dati per backend
      const registrationData = {
        ...userData
        // Skip dateOfBirth for now to avoid parsing issues / Salta dateOfBirth per ora per evitare problemi di parsing
        // ...(dateOfBirth && { dateOfBirth: dateOfBirth + 'T00:00:00' })
      }
      
      console.log('Sending registration data:', registrationData)
      
      const response = await registerUser(registrationData)
      
      console.log('Registration response:', response)
      
      setSuccess('Registrazione completata con successo!')
      
      // Redirect to login after 2 seconds / Reindirizza al login dopo 2 secondi
      setTimeout(() => {
        navigate('/login')
      }, 2000)
      
    } catch (err) {
      console.error('Registration error:', err)
      setError(err.message || 'Errore durante la registrazione')
    } finally {
      setIsLoading(false)
    }
  }

  return (
    <Container className="py-5">
      <Row className="justify-content-center">
        <Col md={8} lg={6} xl={5}>
          {/* Back Button / Pulsante Indietro */}
          <div className="mb-4">
            <Button 
              variant="outline-secondary" 
              onClick={() => navigate(-1)}
              className="d-flex align-items-center"
            >
              <ArrowLeft size={16} className="me-2" />
              Indietro
            </Button>
          </div>

          {/* Register Card / Card Registrazione */}
          <Card className="border-0 shadow-lg">
            <Card.Body className="p-5">
              {/* Header / Intestazione */}
              <div className="text-center mb-4">
                <div className="bg-primary bg-opacity-10 rounded-circle d-inline-flex align-items-center justify-content-center mb-3" style={{width: '60px', height: '60px'}}>
                  <UserPlus className="text-primary" size={30} />
                </div>
                <h2 className="fw-bold text-primary mb-2">Crea Account</h2>
                <p className="text-muted">Registrati per accedere ai corsi</p>
              </div>

              {/* Success Alert / Alert Successo */}
              {success && (
                <Alert variant="success" className="mb-4">
                  <Alert.Heading className="h6">Registrazione Completata</Alert.Heading>
                  {success}
                </Alert>
              )}

              {/* Error Alert / Alert Errore */}
              {error && (
                <Alert variant="danger" className="mb-4">
                  <Alert.Heading className="h6">Errore di Registrazione</Alert.Heading>
                  {error}
                </Alert>
              )}

              {/* Register Form / Form Registrazione */}
              <Form onSubmit={handleSubmit}>
                <Row>
                  <Col md={6}>
                    <Form.Group className="mb-3">
                      <Form.Label htmlFor="nome">Nome</Form.Label>
                      <div className="position-relative">
                        <Form.Control
                          type="text"
                          id="nome"
                          name="nome"
                          placeholder="Il tuo nome"
                          value={formData.nome}
                          onChange={handleInputChange}
                          required
                          className="ps-5"
                        />
                        <User className="position-absolute top-50 start-0 translate-middle-y text-muted ms-3" size={18} />
                      </div>
                    </Form.Group>
                  </Col>
                  <Col md={6}>
                    <Form.Group className="mb-3">
                      <Form.Label htmlFor="cognome">Cognome</Form.Label>
                      <Form.Control
                        type="text"
                        id="cognome"
                        name="cognome"
                        placeholder="Il tuo cognome"
                        value={formData.cognome}
                        onChange={handleInputChange}
                        required
                      />
                    </Form.Group>
                  </Col>
                </Row>

                <Form.Group className="mb-3">
                  <Form.Label htmlFor="email">Email</Form.Label>
                  <div className="position-relative">
                    <Form.Control
                      type="email"
                      id="email"
                      name="email"
                      placeholder="la-tua-email@example.com"
                      value={formData.email}
                      onChange={handleInputChange}
                      required
                      className="ps-5"
                    />
                    <Mail className="position-absolute top-50 start-0 translate-middle-y text-muted ms-3" size={18} />
                  </div>
                </Form.Group>

                <Form.Group className="mb-3">
                  <Form.Label htmlFor="phoneNumber">Telefono (opzionale)</Form.Label>
                  <div className="position-relative">
                    <Form.Control
                      type="tel"
                      id="phoneNumber"
                      name="phoneNumber"
                      placeholder="+39 123 456 7890"
                      value={formData.phoneNumber}
                      onChange={handleInputChange}
                      className="ps-5"
                    />
                    <Phone className="position-absolute top-50 start-0 translate-middle-y text-muted ms-3" size={18} />
                  </div>
                </Form.Group>

                <Form.Group className="mb-3">
                  <Form.Label htmlFor="dateOfBirth">Data di Nascita (opzionale)</Form.Label>
                  <div className="position-relative">
                    <Form.Control
                      type="date"
                      id="dateOfBirth"
                      name="dateOfBirth"
                      value={formData.dateOfBirth}
                      onChange={handleInputChange}
                      className="ps-5"
                    />
                    <Calendar className="position-absolute top-50 start-0 translate-middle-y text-muted ms-3" size={18} />
                  </div>
                </Form.Group>

                <Row>
                  <Col md={6}>
                    <Form.Group className="mb-3">
                      <Form.Label htmlFor="password">Password</Form.Label>
                      <div className="position-relative">
                        <Form.Control
                          type="password"
                          id="password"
                          name="password"
                          placeholder="Minimo 6 caratteri"
                          value={formData.password}
                          onChange={handleInputChange}
                          required
                          className="ps-5"
                        />
                        <Lock className="position-absolute top-50 start-0 translate-middle-y text-muted ms-3" size={18} />
                      </div>
                    </Form.Group>
                  </Col>
                  <Col md={6}>
                    <Form.Group className="mb-3">
                      <Form.Label htmlFor="confirmPassword">Conferma Password</Form.Label>
                      <Form.Control
                        type="password"
                        id="confirmPassword"
                        name="confirmPassword"
                        placeholder="Ripeti la password"
                        value={formData.confirmPassword}
                        onChange={handleInputChange}
                        required
                      />
                    </Form.Group>
                  </Col>
                </Row>

                <Button
                  type="submit"
                  variant="primary"
                  size="lg"
                  className="w-100 mb-3"
                  disabled={isLoading}
                >
                  {isLoading ? (
                    <>
                      <Spinner size="sm" className="me-2" />
                      Registrazione in corso...
                    </>
                  ) : (
                    'Registrati'
                  )}
                </Button>
              </Form>

              {/* Links / Link */}
              <div className="text-center">
                <p className="text-muted mb-0">
                  Hai già un account?{' '}
                  <Link to="/login" className="text-primary text-decoration-none fw-semibold">
                    Accedi
                  </Link>
                </p>
              </div>
            </Card.Body>
          </Card>

          {/* Terms / Termini */}
          <Card className="mt-4 border-light">
            <Card.Body className="p-3">
              <p className="small text-muted mb-0 text-center">
                Registrandoti accetti i{' '}
                <Link to="/terms" className="text-decoration-none">Termini di Servizio</Link>
                {' '}e la{' '}
                <Link to="/privacy" className="text-decoration-none">Privacy Policy</Link>
              </p>
            </Card.Body>
          </Card>
        </Col>
      </Row>
    </Container>
  )
}

export default Register
