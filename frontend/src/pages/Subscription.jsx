import React, { useState, useEffect } from 'react'
import { Container, Row, Col, Card, Button, Badge, Alert, Spinner, Modal } from 'react-bootstrap'
import { Crown, Check, X, Star, Zap, Shield, Users, Calendar, Euro } from 'lucide-react'
import { getSubscriptionTypes, getUserSubscription, updateUserSubscription } from '../services/api'

/**
 * Subscription Page Component
 * Componente Pagina Abbonamenti
 * 
 * Subscription management page with plans and features
 * Pagina gestione abbonamenti con piani e funzionalità
 */
const Subscription = () => {
  const [subscriptionTypes, setSubscriptionTypes] = useState([])
  const [userSubscription, setUserSubscription] = useState(null)
  const [isLoading, setIsLoading] = useState(true)
  const [error, setError] = useState('')
  const [showUpgradeModal, setShowUpgradeModal] = useState(false)
  const [selectedPlan, setSelectedPlan] = useState(null)

  // Get current user from localStorage / Recupera utente corrente da localStorage
  const currentUser = JSON.parse(localStorage.getItem('user') || '{}')

  // Load subscription data / Carica dati abbonamento
  useEffect(() => {
    loadSubscriptionData()
  }, [])

  const loadSubscriptionData = async () => {
    try {
      setIsLoading(true)
      
      // Load subscription types and user subscription in parallel / Carica tipi abbonamento e abbonamento utente in parallelo
      const [typesResponse, userResponse] = await Promise.all([
        getSubscriptionTypes(),
        getUserSubscription(currentUser.id)
      ])
      
      setSubscriptionTypes(typesResponse)
      setUserSubscription(userResponse)
      
    } catch (err) {
      setError(err.message || 'Errore nel caricamento dei dati abbonamento')
    } finally {
      setIsLoading(false)
    }
  }

  // Handle plan upgrade / Gestisce upgrade piano
  const handleUpgrade = async (planType) => {
    try {
      setSelectedPlan(planType)
      setShowUpgradeModal(true)
    } catch (err) {
      setError(err.message || 'Errore nell\'upgrade del piano')
    }
  }

  // Confirm upgrade / Conferma upgrade
  const confirmUpgrade = async () => {
    try {
      const expirationDate = new Date()
      expirationDate.setMonth(expirationDate.getMonth() + 1)
      
      await updateUserSubscription(currentUser.id, {
        subscriptionType: selectedPlan.type,
        expirationDate: expirationDate.toISOString()
      })
      
      setShowUpgradeModal(false)
      setSelectedPlan(null)
      loadSubscriptionData() // Reload data / Ricarica dati
      
    } catch (err) {
      setError(err.message || 'Errore nell\'aggiornamento dell\'abbonamento')
    }
  }

  // Get plan icon / Ottieni icona piano
  const getPlanIcon = (planType) => {
    switch (planType) {
      case 'FREE': return <Users className="text-muted" size={24} />
      case 'BASIC': return <Star className="text-primary" size={24} />
      case 'PREMIUM': return <Crown className="text-warning" size={24} />
      case 'ENTERPRISE': return <Shield className="text-success" size={24} />
      default: return <Users size={24} />
    }
  }

  // Get plan color / Ottieni colore piano
  const getPlanColor = (planType) => {
    switch (planType) {
      case 'FREE': return 'secondary'
      case 'BASIC': return 'primary'
      case 'PREMIUM': return 'warning'
      case 'ENTERPRISE': return 'success'
      default: return 'secondary'
    }
  }

  // Format date / Formatta data
  const formatDate = (dateString) => {
    if (!dateString) return 'N/A'
    return new Date(dateString).toLocaleDateString('it-IT', {
      year: 'numeric',
      month: 'long',
      day: 'numeric'
    })
  }

  // Calculate days remaining / Calcola giorni rimanenti
  const getDaysRemaining = (expirationDate) => {
    if (!expirationDate) return null
    const now = new Date()
    const exp = new Date(expirationDate)
    const diffTime = exp - now
    const diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24))
    return Math.max(0, diffDays)
  }

  if (isLoading) {
    return (
      <Container className="py-5">
        <div className="d-flex justify-content-center align-items-center min-vh-50">
          <Spinner animation="border" variant="primary" />
          <span className="ms-3">Caricamento abbonamenti...</span>
        </div>
      </Container>
    )
  }

  if (error) {
    return (
      <Container className="py-5">
        <Alert variant="danger">
          <Alert.Heading>Errore</Alert.Heading>
          {error}
        </Alert>
      </Container>
    )
  }

  return (
    <Container className="py-5">
      {/* Header / Intestazione */}
      <Row className="mb-5">
        <Col>
          <div className="text-center">
            <h1 className="display-5 fw-bold text-primary mb-3">
              <Crown className="me-3" size={40} />
              Gestione Abbonamenti
            </h1>
            <p className="lead text-muted">
              Scegli il piano perfetto per le tue esigenze di apprendimento
            </p>
          </div>
        </Col>
      </Row>

      {/* Current Subscription / Abbonamento Corrente */}
      {userSubscription && (
        <Row className="mb-5">
          <Col>
            <Card className="border-primary">
              <Card.Body>
                <Row className="align-items-center">
                  <Col md={8}>
                    <div className="d-flex align-items-center mb-3">
                      {getPlanIcon(userSubscription.subscriptionType)}
                      <div className="ms-3">
                        <h4 className="fw-bold mb-1">
                          Piano {userSubscription.subscriptionType}
                        </h4>
                        <Badge bg={getPlanColor(userSubscription.subscriptionType)} className="fs-6">
                          {userSubscription.isActive ? 'Attivo' : 'Scaduto'}
                        </Badge>
                      </div>
                    </div>
                    <div className="row g-3">
                      <Col sm={6}>
                        <div className="d-flex align-items-center">
                          <Calendar className="text-muted me-2" size={16} />
                          <small className="text-muted">
                            Scadenza: {formatDate(userSubscription.subscriptionExpiresAt)}
                          </small>
                        </div>
                      </Col>
                      <Col sm={6}>
                        <div className="d-flex align-items-center">
                          <Users className="text-muted me-2" size={16} />
                          <small className="text-muted">
                            Max corsi: {userSubscription.maxCourses}
                          </small>
                        </div>
                      </Col>
                    </div>
                    {userSubscription.daysRemaining !== null && (
                      <div className="mt-2">
                        <small className="text-muted">
                          Giorni rimanenti: <strong>{userSubscription.daysRemaining}</strong>
                        </small>
                      </div>
                    )}
                  </Col>
                  <Col md={4} className="text-end">
                    {!userSubscription.isActive && (
                      <Button variant="warning" size="sm">
                        Rinnova
                      </Button>
                    )}
                  </Col>
                </Row>
              </Card.Body>
            </Card>
          </Col>
        </Row>
      )}

      {/* Subscription Plans / Piani Abbonamento */}
      <Row className="g-4">
        {Array.isArray(subscriptionTypes) && subscriptionTypes.map((plan) => (
          <Col key={plan.type} md={6} lg={3}>
            <Card className={`h-100 ${plan.type === userSubscription?.subscriptionType ? 'border-primary' : 'border-0'} shadow-sm`}>
              <Card.Body className="text-center p-4">
                {/* Plan Header / Intestazione Piano */}
                <div className="mb-4">
                  <div className={`bg-${getPlanColor(plan.type)} bg-opacity-10 rounded-circle d-inline-flex align-items-center justify-content-center mb-3`} style={{width: '60px', height: '60px'}}>
                    {getPlanIcon(plan.type)}
                  </div>
                  <h5 className="fw-bold">{plan.displayName}</h5>
                  <div className="display-6 fw-bold text-primary">
                    {plan.price === 0 ? 'Gratuito' : `€${plan.price}`}
                    {plan.price > 0 && <small className="text-muted fs-6">/mese</small>}
                  </div>
                </div>

                {/* Plan Features / Funzionalità Piano */}
                <div className="mb-4">
                  <div className="d-flex align-items-center mb-2">
                    <Check className="text-success me-2" size={16} />
                    <small>{plan.maxCourses} corsi inclusi</small>
                  </div>
                  <div className="d-flex align-items-center mb-2">
                    <Check className="text-success me-2" size={16} />
                    <small>Accesso ai materiali</small>
                  </div>
                  {plan.hasAdvancedFeatures && (
                    <>
                      <div className="d-flex align-items-center mb-2">
                        <Zap className="text-warning me-2" size={16} />
                        <small>Funzionalità avanzate</small>
                      </div>
                      <div className="d-flex align-items-center mb-2">
                        <Shield className="text-info me-2" size={16} />
                        <small>Supporto prioritario</small>
                      </div>
                    </>
                  )}
                </div>

                {/* Action Button / Pulsante Azione */}
                {plan.type === userSubscription?.subscriptionType ? (
                  <Button variant="outline-primary" disabled className="w-100">
                    Piano Attuale
                  </Button>
                ) : (
                  <Button
                    variant={plan.type === 'FREE' ? 'outline-secondary' : 'primary'}
                    className="w-100"
                    onClick={() => handleUpgrade(plan)}
                  >
                    {plan.type === 'FREE' ? 'Piano Gratuito' : 'Scegli Piano'}
                  </Button>
                )}
              </Card.Body>
            </Card>
          </Col>
        ))}
      </Row>

      {/* Upgrade Modal / Modal Upgrade */}
      <Modal show={showUpgradeModal} onHide={() => setShowUpgradeModal(false)} centered>
        <Modal.Header closeButton>
          <Modal.Title>Conferma Upgrade</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          {selectedPlan && (
            <div className="text-center">
              <div className="mb-3">
                {getPlanIcon(selectedPlan.type)}
              </div>
              <h5>Piano {selectedPlan.displayName}</h5>
              <p className="text-muted">
                Stai per aggiornare al piano <strong>{selectedPlan.displayName}</strong>
              </p>
              <div className="bg-light p-3 rounded">
                <div className="d-flex justify-content-between">
                  <span>Prezzo mensile:</span>
                  <strong>€{selectedPlan.price}</strong>
                </div>
                <div className="d-flex justify-content-between">
                  <span>Corsi inclusi:</span>
                  <strong>{selectedPlan.maxCourses}</strong>
                </div>
                <div className="d-flex justify-content-between">
                  <span>Funzionalità avanzate:</span>
                  <strong>{selectedPlan.hasAdvancedFeatures ? 'Sì' : 'No'}</strong>
                </div>
              </div>
            </div>
          )}
        </Modal.Body>
        <Modal.Footer>
          <Button variant="outline-secondary" onClick={() => setShowUpgradeModal(false)}>
            Annulla
          </Button>
          <Button variant="primary" onClick={confirmUpgrade}>
            Conferma Upgrade
          </Button>
        </Modal.Footer>
      </Modal>
    </Container>
  )
}

export default Subscription
