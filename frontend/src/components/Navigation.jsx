import React, { useState } from 'react'
import { Link, useLocation, useNavigate } from 'react-router-dom'
import { Navbar, Nav, Container, Offcanvas, Button, Dropdown } from 'react-bootstrap'
import { Home, BookOpen, Users, UserPlus, Plus, LogIn, User, Crown, LogOut } from 'lucide-react'
import { useAuth } from '../contexts/AuthContext'

/**
 * Bootstrap Navigation Component
 * Componente Navigazione Bootstrap
 * 
 * Professional navigation using Bootstrap components
 * Navigazione professionale usando componenti Bootstrap
 */
const Navigation = () => {
  const location = useLocation()
  const navigate = useNavigate()
  const [show, setShow] = useState(false)
  const { user, logout, isAuthenticated, getUserDisplayName } = useAuth()

  const handleClose = () => setShow(false)
  const handleShow = () => setShow(true)

  // Handle logout / Gestisce logout
  const handleLogout = () => {
    logout()
    navigate('/')
  }

  // Check if current path is active / Controlla se il percorso corrente è attivo
  const isActive = (path) => {
    return location.pathname === path
  }

  return (
    <>
      {/* Bootstrap Navbar / Navbar Bootstrap */}
      <Navbar bg="light" expand="lg" className="shadow-sm">
        <Container>
          {/* Brand / Logo */}
          <Navbar.Brand as={Link} to="/" className="d-flex align-items-center">
            <div className="bg-primary rounded p-2 me-2">
              <BookOpen className="text-white" size={20} />
            </div>
            <span className="fw-bold text-primary">CourseHub</span>
          </Navbar.Brand>

          {/* Toggle Button / Pulsante Toggle */}
          <Navbar.Toggle 
            aria-controls="offcanvasNavbar" 
            onClick={handleShow}
            className="border-0"
          />

          {/* Desktop Navigation / Navigazione Desktop */}
          <Navbar.Collapse id="basic-navbar-nav" className="d-none d-lg-flex">
            <Nav className="me-auto">
              <Nav.Link 
                as={Link} 
                to="/" 
                className={isActive('/') ? 'active fw-semibold' : ''}
              >
                <Home className="me-1" size={16} />
                Dashboard
              </Nav.Link>
              <Nav.Link 
                as={Link} 
                to="/landing" 
                className={isActive('/landing') ? 'active fw-semibold' : ''}
              >
                <BookOpen className="me-1" size={16} />
                Courses
              </Nav.Link>
              <Nav.Link 
                as={Link} 
                to="/enrollments" 
                className={isActive('/enrollments') ? 'active fw-semibold' : ''}
              >
                <Users className="me-1" size={16} />
                Enrollments
              </Nav.Link>
            </Nav>
            
            {/* Action Buttons / Pulsanti Azione */}
            <div className="d-flex gap-2">
              {isAuthenticated() ? (
                <>
                  <Button 
                    as={Link} 
                    to="/create-enrollment" 
                    variant="primary" 
                    size="sm"
                    className="d-flex align-items-center"
                  >
                    <UserPlus size={16} className="me-1" />
                    New Enrollment
                  </Button>
                  
                  <Button 
                    as={Link} 
                    to="/create-course" 
                    variant="outline-primary" 
                    size="sm"
                    className="d-flex align-items-center"
                  >
                    <Plus size={16} className="me-1" />
                    New Course
                  </Button>

                  <Dropdown>
                    <Dropdown.Toggle variant="outline-secondary" size="sm" className="d-flex align-items-center">
                      <User size={16} className="me-1" />
                      <span>{getUserDisplayName()}</span>
                    </Dropdown.Toggle>
                    <Dropdown.Menu>
                      <Dropdown.Item as={Link} to="/subscription">
                        <Crown size={16} className="me-2" />
                        Abbonamenti
                      </Dropdown.Item>
                      <Dropdown.Divider />
                      <Dropdown.Item onClick={handleLogout}>
                        <LogOut size={16} className="me-2" />
                        Logout
                      </Dropdown.Item>
                    </Dropdown.Menu>
                  </Dropdown>
                </>
              ) : (
                <>
                  <Button 
                    as={Link} 
                    to="/login" 
                    variant="outline-primary" 
                    size="sm"
                    className="d-flex align-items-center"
                  >
                    <LogIn size={16} className="me-1" />
                    <span>Login</span>
                  </Button>
                  
                  <Button 
                    as={Link} 
                    to="/register" 
                    variant="primary" 
                    size="sm"
                    className="d-flex align-items-center"
                  >
                    <UserPlus size={16} className="me-1" />
                    <span>Registrati</span>
                  </Button>
                </>
              )}
            </div>
          </Navbar.Collapse>
        </Container>
      </Navbar>

      {/* Mobile Offcanvas / Offcanvas Mobile */}
      <Offcanvas show={show} onHide={handleClose} placement="end">
        <Offcanvas.Header closeButton>
          <Offcanvas.Title className="d-flex align-items-center">
            <div className="bg-primary rounded p-2 me-2">
              <BookOpen className="text-white" size={20} />
            </div>
            <span className="fw-bold text-primary">CourseHub</span>
          </Offcanvas.Title>
        </Offcanvas.Header>
        <Offcanvas.Body>
          <Nav className="flex-column">
            <Nav.Link 
              as={Link} 
              to="/" 
              className={`py-3 ${isActive('/') ? 'active fw-semibold' : ''}`}
              onClick={handleClose}
            >
              <Home className="me-2" size={18} />
              Dashboard
            </Nav.Link>
            <Nav.Link 
              as={Link} 
              to="/landing" 
              className={`py-3 ${isActive('/landing') ? 'active fw-semibold' : ''}`}
              onClick={handleClose}
            >
              <BookOpen className="me-2" size={18} />
              Courses
            </Nav.Link>
            <Nav.Link 
              as={Link} 
              to="/enrollments" 
              className={`py-3 ${isActive('/enrollments') ? 'active fw-semibold' : ''}`}
              onClick={handleClose}
            >
              <Users className="me-2" size={18} />
              Enrollments
            </Nav.Link>
            
            <hr className="my-3" />
            
            <h6 className="text-muted text-uppercase small fw-bold mb-3">Quick Actions</h6>
            
            <Button 
              as={Link} 
              to="/create-enrollment" 
              variant="primary" 
              className="mb-2 d-flex align-items-center justify-content-center"
              onClick={handleClose}
            >
              <UserPlus size={16} className="me-2" />
              New Enrollment
            </Button>
            <Button 
              as={Link} 
              to="/create-course" 
              variant="outline-primary" 
              className="d-flex align-items-center justify-content-center"
              onClick={handleClose}
            >
              <Plus size={16} className="me-2" />
              New Course
            </Button>
          </Nav>
        </Offcanvas.Body>
      </Offcanvas>
    </>
  )
}

export default Navigation