import React, { useState, useEffect } from 'react'
import { Link } from 'react-router-dom'
import { useQuery } from 'react-query'
import { getCourses } from '../services/api'
import { Container, Row, Col, Card, Button, Badge, Carousel } from 'react-bootstrap'
import { 
  BookOpen, 
  Star, 
  Users, 
  Clock, 
  Award, 
  TrendingUp, 
  CheckCircle, 
  ArrowRight,
  Play,
  Shield,
  Zap,
  Heart,
  Sparkles,
  FileText
} from 'lucide-react'

/**
 * Course Landing Page Component
 * Componente Pagina Landing Corsi
 * 
 * Dynamic and attractive landing page for course purchases
 * Pagina landing dinamica e accattivante per l'acquisto dei corsi
 */
const CourseLanding = () => {
  const [currentTestimonial, setCurrentTestimonial] = useState(0)
  const [hoveredCourse, setHoveredCourse] = useState(null)

  // Fetch courses / Recupera corsi
  const { data: searchData, isLoading } = useQuery('courses', () => getCourses(), {
    staleTime: 30000,
    cacheTime: 300000,
  })

  // Extract courses from response / Estrai corsi dalla risposta
  const courses = searchData?.courses || []

  // Testimonials data / Dati testimonianze
  const testimonials = [
    {
      name: "Marco Rossi",
      role: "Sviluppatore Frontend",
      content: "I corsi sono fantastici! Ho imparato React in modo pratico e veloce.",
      rating: 5,
      avatar: "MR"
    },
    {
      name: "Sara Bianchi",
      role: "UI/UX Designer",
      content: "La qualità dei contenuti è eccellente. Consigliatissimo!",
      rating: 5,
      avatar: "SB"
    },
    {
      name: "Luca Verdi",
      role: "Full Stack Developer",
      content: "Ho trovato lavoro grazie a questi corsi. Insegnanti preparati e materiali aggiornati.",
      rating: 5,
      avatar: "LV"
    }
  ]

  // Auto-rotate testimonials / Rotazione automatica testimonianze
  useEffect(() => {
    const interval = setInterval(() => {
      setCurrentTestimonial((prev) => (prev + 1) % testimonials.length)
    }, 5000)
    return () => clearInterval(interval)
  }, [testimonials.length])

  // Format date / Formatta data
  const formatDate = (dateString) => {
    return new Date(dateString).toLocaleDateString('it-IT', {
      year: 'numeric',
      month: 'long',
      day: 'numeric'
    })
  }

  // Get availability status / Ottieni stato disponibilità
  const getAvailabilityStatus = (disponibilita) => {
    if (disponibilita > 10) return { text: 'Disponibile', class: 'bg-green-100 text-green-800' }
    if (disponibilita > 0) return { text: 'Pochi posti', class: 'bg-yellow-100 text-yellow-800' }
    return { text: 'Completo', class: 'bg-red-100 text-red-800' }
  }

  if (isLoading) {
    return (
      <div className="flex justify-center items-center min-h-64">
        <div className="loading-spinner"></div>
        <span className="ml-2 text-gray-600">Caricamento corsi...</span>
      </div>
    )
  }

  return (
    <div className="min-vh-100">
      {/* Hero Section / Sezione Hero */}
      <section className="bg-primary bg-gradient py-5">
        <Container className="py-5">
          <Row className="align-items-center">
            <Col lg={6} className="text-center text-lg-start">
              <Badge bg="warning" className="mb-3 fs-6">
                <Sparkles className="me-1" size={16} />
                Corsi di Alta Qualità
              </Badge>
              
              <h1 className="display-4 fw-bold text-white mb-4">
                Impara le Tecnologie
                <span className="text-warning"> del Futuro</span>
              </h1>
              
              <p className="lead text-light mb-4">
                Corsi professionali per sviluppatori, designer e professionisti IT.
                Inizia il tuo percorso di crescita oggi stesso!
              </p>
              
              <div className="d-flex flex-column flex-sm-row gap-3">
                <Button 
                  as={Link} 
                  to="/create-enrollment" 
                  variant="warning" 
                  size="lg"
                  className="d-flex align-items-center"
                >
                  Inizia Subito
                  <ArrowRight className="ms-2" size={20} />
                </Button>
                
                <Button 
                  variant="outline-light" 
                  size="lg"
                  className="d-flex align-items-center"
                >
                  <Play className="me-2" size={20} />
                  Guarda Demo
                </Button>
              </div>
            </Col>
            
            <Col lg={6} className="text-center">
              <div className="bg-white bg-opacity-10 rounded-4 p-5">
                <BookOpen size={80} className="text-white mb-3" />
                <h3 className="text-white">Corsi Professionali</h3>
                <p className="text-light">Impara con i migliori esperti del settore</p>
              </div>
            </Col>
          </Row>
        </Container>
      </section>

      {/* Welcome Offer Section / Sezione Offerta Benvenuto */}
      <section className="bg-success bg-gradient py-5">
        <Container>
          <Row className="align-items-center">
            <Col lg={6}>
              <Badge bg="warning" className="mb-3 fs-6">
                <Sparkles className="me-1" size={16} />
                Offerta Speciale
              </Badge>
              
              <h2 className="display-5 fw-bold text-white mb-3">Benvenuto!</h2>
              <p className="lead text-light mb-4">
                Scopri la nostra offerta esclusiva per nuovi studenti
              </p>

              {/* Offer Card / Card Offerta */}
              <Card className="bg-white bg-opacity-10 border-0 mb-4">
                <Card.Body className="text-center">
                  <div className="display-1 fw-bold text-warning mb-2">50%</div>
                  <h4 className="text-white mb-2">Sconto</h4>
                  <p className="text-light mb-3">Sul primo corso</p>
                  <small className="text-warning">Valido fino al 31 dicembre 2025</small>
                </Card.Body>
              </Card>

              {/* Features / Caratteristiche */}
              <Row className="mb-4">
                <Col sm={6}>
                  <div className="d-flex align-items-center mb-2">
                    <CheckCircle className="text-success me-2" size={20} />
                    <span className="text-white">Accesso illimitato</span>
                  </div>
                  <div className="d-flex align-items-center mb-2">
                    <CheckCircle className="text-success me-2" size={20} />
                    <span className="text-white">Certificazione inclusa</span>
                  </div>
                </Col>
                <Col sm={6}>
                  <div className="d-flex align-items-center mb-2">
                    <CheckCircle className="text-success me-2" size={20} />
                    <span className="text-white">Supporto 24/7</span>
                  </div>
                  <div className="d-flex align-items-center mb-2">
                    <CheckCircle className="text-success me-2" size={20} />
                    <span className="text-white">Garanzia soddisfatti</span>
                  </div>
                </Col>
              </Row>

              <Button 
                as={Link} 
                to="/create-enrollment" 
                variant="warning" 
                size="lg"
                className="d-flex align-items-center"
              >
                Approfitta Ora
                <ArrowRight className="ms-2" size={20} />
              </Button>
              <p className="text-warning small mt-2">Nessun impegno, cancelli quando vuoi</p>
            </Col>

            {/* Stats / Statistiche */}
            <Col lg={6}>
              <Row className="g-4">
                <Col sm={6}>
                  <Card className="bg-white bg-opacity-10 border-0 text-center">
                    <Card.Body>
                      <div className="display-4 fw-bold text-warning mb-2">500+</div>
                      <p className="text-light mb-0">Studenti Soddisfatti</p>
                    </Card.Body>
                  </Card>
                </Col>
                <Col sm={6}>
                  <Card className="bg-white bg-opacity-10 border-0 text-center">
                    <Card.Body>
                      <div className="display-4 fw-bold text-info mb-2">50+</div>
                      <p className="text-light mb-0">Corsi Disponibili</p>
                    </Card.Body>
                  </Card>
                </Col>
                <Col sm={6}>
                  <Card className="bg-white bg-opacity-10 border-0 text-center">
                    <Card.Body>
                      <div className="display-4 fw-bold text-warning mb-2">95%</div>
                      <p className="text-light mb-0">Tasso di Soddisfazione</p>
                    </Card.Body>
                  </Card>
                </Col>
                <Col sm={6}>
                  <Card className="bg-white bg-opacity-10 border-0 text-center">
                    <Card.Body>
                      <div className="display-4 fw-bold text-info mb-2">24/7</div>
                      <p className="text-light mb-0">Supporto Disponibile</p>
                    </Card.Body>
                  </Card>
                </Col>
              </Row>
            </Col>
          </Row>
        </Container>
      </section>

      {/* Features Section / Sezione Caratteristiche */}
      <section className="bg-light py-5">
        <Container>
          <Row className="text-center mb-5">
            <Col>
              <h2 className="display-5 fw-bold mb-3">Perché Scegliere i Nostri Corsi?</h2>
              <p className="lead text-muted">
                Offriamo un'esperienza di apprendimento unica e professionale
              </p>
            </Col>
          </Row>

          <Row className="g-4">
            <Col md={4}>
              <Card className="h-100 border-0 shadow-sm">
                <Card.Body className="text-center p-4">
                  <div className="bg-primary bg-opacity-10 rounded-circle d-inline-flex align-items-center justify-content-center mb-3" style={{width: '80px', height: '80px'}}>
                    <BookOpen className="text-primary" size={40} />
                  </div>
                  <h4 className="fw-bold mb-3">Contenuti Aggiornati</h4>
                  <p className="text-muted">
                    I nostri corsi sono sempre aggiornati con le ultime tecnologie e best practices del settore.
                  </p>
                </Card.Body>
              </Card>
            </Col>

            <Col md={4}>
              <Card className="h-100 border-0 shadow-sm">
                <Card.Body className="text-center p-4">
                  <div className="bg-success bg-opacity-10 rounded-circle d-inline-flex align-items-center justify-content-center mb-3" style={{width: '80px', height: '80px'}}>
                    <Users className="text-success" size={40} />
                  </div>
                  <h4 className="fw-bold mb-3">Insegnanti Esperti</h4>
                  <p className="text-muted">
                    I nostri docenti sono professionisti del settore con anni di esperienza pratica.
                  </p>
                </Card.Body>
              </Card>
            </Col>

            <Col md={4}>
              <Card className="h-100 border-0 shadow-sm">
                <Card.Body className="text-center p-4">
                  <div className="bg-warning bg-opacity-10 rounded-circle d-inline-flex align-items-center justify-content-center mb-3" style={{width: '80px', height: '80px'}}>
                    <Award className="text-warning" size={40} />
                  </div>
                  <h4 className="fw-bold mb-3">Certificazioni</h4>
                  <p className="text-muted">
                    Ottieni certificazioni riconosciute che valorizzano il tuo curriculum professionale.
                  </p>
                </Card.Body>
              </Card>
            </Col>
          </Row>
        </Container>
      </section>

      {/* Courses Section / Sezione Corsi */}
      <section className="py-5">
        <Container>
          <Row className="text-center mb-5">
            <Col>
              <h2 className="display-5 fw-bold mb-3">I Nostri Corsi</h2>
              <p className="lead text-muted">
                Scegli il corso perfetto per le tue esigenze
              </p>
            </Col>
          </Row>

          <Row className="g-4">
            {Array.isArray(courses) && courses.slice(0, 6).map((course, index) => (
              <Col key={course.corsoId} md={6} lg={4}>
                <Card className="h-100 border-0 shadow-sm">
                  <div className="position-relative">
                    <div className="bg-primary bg-gradient d-flex align-items-center justify-content-center" style={{height: '200px'}}>
                      <BookOpen className="text-white" size={60} />
                    </div>
                    <Badge 
                      bg={getAvailabilityStatus(course.disponibilita).text === 'Disponibile' ? 'success' : 'warning'} 
                      className="position-absolute top-0 end-0 m-3"
                    >
                      {getAvailabilityStatus(course.disponibilita).text}
                    </Badge>
                  </div>

                  <Card.Body className="d-flex flex-column">
                    <h5 className="card-title fw-bold mb-3">{course.titolo}</h5>
                    
                    <p className="text-muted mb-3 flex-grow-1">
                      {course.informazioniGenerali || 'Corso professionale per sviluppatori'}
                    </p>

                    <div className="mb-3">
                      <div className="d-flex align-items-center mb-2">
                        <Clock className="text-muted me-2" size={16} />
                        <small className="text-muted">{formatDate(course.dataOraInizio)}</small>
                      </div>
                      <div className="d-flex align-items-center mb-2">
                        <Users className="text-muted me-2" size={16} />
                        <small className="text-muted">{course.disponibilita} posti disponibili</small>
                      </div>
                      {course.docenti && (
                        <div className="d-flex align-items-center mb-2">
                          <Award className="text-muted me-2" size={16} />
                          <small className="text-muted">{course.docenti}</small>
                        </div>
                      )}
                    </div>

                           <div className="d-flex justify-content-between align-items-center mt-auto">
                             <div className="d-flex align-items-center">
                               {[...Array(5)].map((_, i) => (
                                 <Star key={i} className="text-warning" size={16} />
                               ))}
                               <span className="text-muted ms-1 small">4.9</span>
                             </div>
                             
                             <div className="d-flex gap-1">
                               <Button 
                                 as={Link} 
                                 to={`/course/${course.corsoId}`} 
                                 variant="outline-primary" 
                                 size="sm"
                               >
                                 <FileText size={14} />
                               </Button>
                               <Button 
                                 as={Link} 
                                 to={`/create-enrollment/${course.corsoId}`} 
                                 variant="primary" 
                                 size="sm"
                               >
                                 Iscriviti
                               </Button>
                             </div>
                           </div>
                  </Card.Body>
                </Card>
              </Col>
            ))}
          </Row>

          <Row className="text-center mt-5">
            <Col>
              <Button 
                as={Link} 
                to="/" 
                variant="outline-primary" 
                size="lg"
                className="d-flex align-items-center mx-auto"
              >
                Vedi Tutti i Corsi
                <ArrowRight className="ms-2" size={20} />
              </Button>
            </Col>
          </Row>
        </Container>
      </section>

      {/* Testimonials Section / Sezione Testimonianze */}
      <section className="bg-light py-5">
        <Container>
          <Row className="text-center mb-5">
            <Col>
              <h2 className="display-5 fw-bold mb-3">Cosa Dicono i Nostri Studenti</h2>
              <p className="lead text-muted">
                Le testimonianze di chi ha già scelto i nostri corsi
              </p>
            </Col>
          </Row>

          <Row className="justify-content-center">
            <Col lg={8}>
              <Card className="border-0 shadow">
                <Card.Body className="p-5">
                  <Row className="align-items-center mb-4">
                    <Col xs="auto">
                      <div className="bg-primary rounded-circle d-flex align-items-center justify-content-center text-white fw-bold" style={{width: '60px', height: '60px'}}>
                        {testimonials[currentTestimonial].avatar}
                      </div>
                    </Col>
                    <Col>
                      <h5 className="fw-bold mb-1">{testimonials[currentTestimonial].name}</h5>
                      <p className="text-muted mb-0">{testimonials[currentTestimonial].role}</p>
                    </Col>
                    <Col xs="auto">
                      <div className="d-flex">
                        {[...Array(testimonials[currentTestimonial].rating)].map((_, i) => (
                          <Star key={i} className="text-warning" size={20} />
                        ))}
                      </div>
                    </Col>
                  </Row>
                  
                  <blockquote className="fs-5 text-muted fst-italic mb-0">
                    "{testimonials[currentTestimonial].content}"
                  </blockquote>
                </Card.Body>
              </Card>

              <div className="d-flex justify-content-center mt-4">
                {testimonials.map((_, index) => (
                  <button
                    key={index}
                    onClick={() => setCurrentTestimonial(index)}
                    className={`btn btn-sm rounded-circle me-2 ${
                      index === currentTestimonial ? 'btn-primary' : 'btn-outline-primary'
                    }`}
                    style={{width: '12px', height: '12px'}}
                  />
                ))}
              </div>
            </Col>
          </Row>
        </Container>
      </section>

      {/* CTA Section / Sezione Call to Action */}
      <section className="bg-primary bg-gradient py-5">
        <Container>
          <Row className="text-center">
            <Col>
              <h2 className="display-5 fw-bold text-white mb-3">
                Pronto a Iniziare il Tuo Percorso?
              </h2>
              <p className="lead text-light mb-4">
                Unisciti a migliaia di studenti che hanno già scelto i nostri corsi
              </p>
              
              <div className="d-flex flex-column flex-sm-row gap-3 justify-content-center">
                <Button 
                  as={Link} 
                  to="/create-enrollment" 
                  variant="warning" 
                  size="lg"
                  className="d-flex align-items-center"
                >
                  Inizia Ora
                  <ArrowRight className="ms-2" size={20} />
                </Button>
                
                <Button 
                  variant="outline-light" 
                  size="lg"
                >
                  Contattaci
                </Button>
              </div>
            </Col>
          </Row>
        </Container>
      </section>
    </div>
  )
}

export default CourseLanding
