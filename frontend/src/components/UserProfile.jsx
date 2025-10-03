import React from 'react'
import { Card, Row, Col, Badge, Button } from 'react-bootstrap'
import { User, Mail, Calendar, Crown, LogOut, Settings } from 'lucide-react'
import { useAuth } from '../contexts/AuthContext'

/**
 * User Profile Component
 * Componente Profilo Utente
 * 
 * Displays user information and profile actions
 * Mostra informazioni utente e azioni profilo
 */
const UserProfile = () => {
  const { user, logout, getUserDisplayName } = useAuth()

  if (!user) {
    return null
  }

  // Format date / Formatta data
  const formatDate = (dateString) => {
    if (!dateString) return 'Non specificata'
    return new Date(dateString).toLocaleDateString('it-IT')
  }

  // Get subscription badge color / Ottieni colore badge abbonamento
  const getSubscriptionBadgeVariant = (subscriptionType) => {
    switch (subscriptionType) {
      case 'FREE': return 'secondary'
      case 'BASIC': return 'primary'
      case 'PREMIUM': return 'warning'
      case 'ENTERPRISE': return 'success'
      default: return 'secondary'
    }
  }

  // Get role badge color / Ottieni colore badge ruolo
  const getRoleBadgeVariant = (role) => {
    switch (role) {
      case 'ADMIN': return 'danger'
      case 'INSTRUCTOR': return 'info'
      case 'STUDENT': return 'success'
      default: return 'secondary'
    }
  }

  return (
    <Card className="shadow-sm border-0">
      <Card.Header className="bg-primary text-white">
        <div className="d-flex align-items-center justify-content-between">
          <div className="d-flex align-items-center">
            <div className="bg-white bg-opacity-20 rounded-circle p-2 me-3">
              <User className="text-white" size={20} />
            </div>
            <div>
              <h5 className="mb-0">{getUserDisplayName()}</h5>
              <small className="opacity-75">Benvenuto nel tuo profilo</small>
            </div>
          </div>
          <div className="d-flex gap-2">
            <Button
              variant="outline-light"
              size="sm"
              className="d-flex align-items-center"
            >
              <Settings size={16} className="me-1" />
              Impostazioni
            </Button>
            <Button
              variant="outline-light"
              size="sm"
              onClick={logout}
              className="d-flex align-items-center"
            >
              <LogOut size={16} className="me-1" />
              Logout
            </Button>
          </div>
        </div>
      </Card.Header>
      
      <Card.Body>
        <Row>
          <Col md={6}>
            <h6 className="text-muted mb-3">Informazioni Personali</h6>
            <div className="mb-3">
              <div className="d-flex align-items-center mb-2">
                <Mail size={16} className="text-muted me-2" />
                <strong>Email:</strong>
                <span className="ms-2">{user.email}</span>
              </div>
              {user.phoneNumber && (
                <div className="d-flex align-items-center mb-2">
                  <span className="text-muted me-2">📞</span>
                  <strong>Telefono:</strong>
                  <span className="ms-2">{user.phoneNumber}</span>
                </div>
              )}
              {user.dateOfBirth && (
                <div className="d-flex align-items-center mb-2">
                  <Calendar size={16} className="text-muted me-2" />
                  <strong>Data di nascita:</strong>
                  <span className="ms-2">{formatDate(user.dateOfBirth)}</span>
                </div>
              )}
            </div>
          </Col>
          
          <Col md={6}>
            <h6 className="text-muted mb-3">Account & Abbonamento</h6>
            <div className="mb-3">
              <div className="d-flex align-items-center mb-2">
                <strong>Ruolo:</strong>
                <Badge 
                  variant={getRoleBadgeVariant(user.role)} 
                  className="ms-2"
                >
                  {user.role}
                </Badge>
              </div>
              <div className="d-flex align-items-center mb-2">
                <Crown size={16} className="text-muted me-2" />
                <strong>Abbonamento:</strong>
                <Badge 
                  variant={getSubscriptionBadgeVariant(user.subscriptionType)} 
                  className="ms-2"
                >
                  {user.subscriptionType}
                </Badge>
              </div>
              {user.subscriptionExpiresAt && (
                <div className="d-flex align-items-center mb-2">
                  <Calendar size={16} className="text-muted me-2" />
                  <strong>Scadenza:</strong>
                  <span className="ms-2">{formatDate(user.subscriptionExpiresAt)}</span>
                </div>
              )}
            </div>
          </Col>
        </Row>
        
        <Row>
          <Col>
            <h6 className="text-muted mb-3">Stato Account</h6>
            <div className="d-flex gap-3">
              <div className="d-flex align-items-center">
                <div className={`rounded-circle me-2 ${user.isActive ? 'bg-success' : 'bg-danger'}`} 
                     style={{width: '8px', height: '8px'}}></div>
                <span className={user.isActive ? 'text-success' : 'text-danger'}>
                  {user.isActive ? 'Attivo' : 'Disattivato'}
                </span>
              </div>
              <div className="d-flex align-items-center">
                <div className={`rounded-circle me-2 ${user.emailVerified ? 'bg-success' : 'bg-warning'}`} 
                     style={{width: '8px', height: '8px'}}></div>
                <span className={user.emailVerified ? 'text-success' : 'text-warning'}>
                  {user.emailVerified ? 'Email verificata' : 'Email da verificare'}
                </span>
              </div>
            </div>
          </Col>
        </Row>
      </Card.Body>
    </Card>
  )
}

export default UserProfile
