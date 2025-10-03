import React, { useState } from 'react'
import { Link, useNavigate } from 'react-router-dom'
import { Container, Row, Col, Card, Form, Button, Alert, Spinner } from 'react-bootstrap'
import { User, Lock, Eye, EyeOff, ArrowLeft } from 'lucide-react'
import { loginUser } from '../services/api'
import { useAuth } from '../contexts/AuthContext'

/**
 * Login Page Component
 * Componente Pagina Login
 * 
 * User authentication page with form validation
 * Pagina autenticazione utente con validazione form
 */
const Login = () => {
  const navigate = useNavigate()
  const { login } = useAuth()
  const [formData, setFormData] = useState({
    email: '',
    password: ''
  })
  const [showPassword, setShowPassword] = useState(false)
  const [isLoading, setIsLoading] = useState(false)
  const [error, setError] = useState('')

  // Handle form input changes / Gestisce cambiamenti input form
  const handleInputChange = (e) => {
    const { name, value } = e.target
    setFormData(prev => ({
      ...prev,
      [name]: value
    }))
    // Clear error when user starts typing / Pulisci errore quando utente inizia a digitare
    if (error) setError('')
  }

  // Handle form submission / Gestisce invio form
  const handleSubmit = async (e) => {
    e.preventDefault()
    setIsLoading(true)
    setError('')

    try {
      const response = await loginUser(formData)
      
      // Use AuthContext to store user data and tokens / Usa AuthContext per salvare dati utente e token
      login(
        response.data.user,
        response.data.accessToken,
        response.data.refreshToken
      )
      
      // Redirect to dashboard / Reindirizza alla dashboard
      navigate('/')
      
    } catch (err) {
      setError(err.message || 'Errore durante l\'accesso')
    } finally {
      setIsLoading(false)
    }
  }

  return (
    <Container className="py-5">
      <Row className="justify-content-center">
        <Col md={6} lg={5} xl={4}>
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

          {/* Login Card / Card Login */}
          <Card className="border-0 shadow-lg">
            <Card.Body className="p-5">
              {/* Header / Intestazione */}
              <div className="text-center mb-4">
                <div className="bg-primary bg-opacity-10 rounded-circle d-inline-flex align-items-center justify-content-center mb-3" style={{width: '60px', height: '60px'}}>
                  <User className="text-primary" size={30} />
                </div>
                <h2 className="fw-bold text-primary mb-2">Benvenuto</h2>
                <p className="text-muted">Accedi al tuo account</p>
              </div>

              {/* Error Alert / Alert Errore */}
              {error && (
                <Alert variant="danger" className="mb-4">
                  <Alert.Heading className="h6">Errore di Accesso</Alert.Heading>
                  {error}
                </Alert>
              )}

              {/* Login Form / Form Login */}
              <Form onSubmit={handleSubmit}>
                <Form.Group className="mb-3">
                  <Form.Label htmlFor="email">Email</Form.Label>
                  <div className="position-relative">
                    <Form.Control
                      type="email"
                      id="email"
                      name="email"
                      placeholder="Inserisci la tua email"
                      value={formData.email}
                      onChange={handleInputChange}
                      required
                      className="ps-5"
                    />
                    <User className="position-absolute top-50 start-0 translate-middle-y text-muted ms-3" size={18} />
                  </div>
                </Form.Group>

                <Form.Group className="mb-4">
                  <Form.Label htmlFor="password">Password</Form.Label>
                  <div className="position-relative">
                    <Form.Control
                      type={showPassword ? 'text' : 'password'}
                      id="password"
                      name="password"
                      placeholder="Inserisci la tua password"
                      value={formData.password}
                      onChange={handleInputChange}
                      required
                      className="ps-5 pe-5"
                    />
                    <Lock className="position-absolute top-50 start-0 translate-middle-y text-muted ms-3" size={18} />
                    <Button
                      type="button"
                      variant="link"
                      className="position-absolute top-50 end-0 translate-middle-y p-0 me-3"
                      onClick={() => setShowPassword(!showPassword)}
                    >
                      {showPassword ? <EyeOff size={18} /> : <Eye size={18} />}
                    </Button>
                  </div>
                </Form.Group>

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
                      Accesso in corso...
                    </>
                  ) : (
                    'Accedi'
                  )}
                </Button>
              </Form>

              {/* Links / Link */}
              <div className="text-center">
                <p className="text-muted mb-2">
                  Non hai un account?{' '}
                  <Link to="/register" className="text-primary text-decoration-none fw-semibold">
                    Registrati
                  </Link>
                </p>
                <Link to="/forgot-password" className="text-muted text-decoration-none small">
                  Password dimenticata?
                </Link>
              </div>
            </Card.Body>
          </Card>

          {/* Demo Credentials / Credenziali Demo */}
          <Card className="mt-4 border-info">
            <Card.Body className="p-3">
              <h6 className="text-info mb-2">Credenziali Demo</h6>
              <div className="small text-muted">
                <div><strong>Demo User:</strong> demo@example.com / demo123</div>
                <div><strong>Test User:</strong> test.login.final3@example.com / password123</div>
              </div>
            </Card.Body>
          </Card>
        </Col>
      </Row>
    </Container>
  )
}

export default Login
