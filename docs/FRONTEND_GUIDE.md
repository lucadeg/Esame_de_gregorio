# 🎨 Frontend Development Guide

## 📋 Overview

**React Frontend** per Course Management System - Interfaccia moderna e responsive con Bootstrap 5 e componenti avanzati.

## 🏗️ Architettura Frontend

```
frontend/
├── src/
│   ├── components/          # Componenti riutilizzabili
│   │   ├── Navigation.jsx   # Navigazione principale
│   │   └── ThreeJSBackground.jsx # Sfondo 3D
│   ├── pages/               # Pagine applicazione
│   │   ├── Home.jsx         # Dashboard principale
│   │   ├── CourseDetail.jsx # Dettaglio corso
│   │   ├── CourseLanding.jsx # Landing page
│   │   ├── EnrollmentsEnhanced.jsx # Gestione iscrizioni
│   │   ├── CreateEnrollment.jsx # Creazione iscrizione
│   │   └── CreateCourse.jsx # Creazione corso
│   ├── services/            # API Services
│   │   └── api.js          # Client API
│   ├── styles/              # Stili personalizzati
│   │   └── animations.css  # Animazioni CSS
│   ├── App.jsx             # Componente principale
│   └── main.jsx            # Entry point
├── public/                  # Assets statici
├── package.json            # Dipendenze
└── vite.config.js          # Configurazione Vite
```

## 🚀 Tecnologie

### Core Framework
- **React 18** - UI Framework con hooks moderni
- **React Router DOM** - Navigazione client-side
- **React Query** - Data fetching e caching
- **Bootstrap 5** - CSS Framework responsive

### UI Components
- **React Bootstrap** - Componenti Bootstrap per React
- **Lucide React** - Icone moderne e leggere
- **Three.js** - Grafica 3D per backgrounds
- **Framer Motion** - Animazioni avanzate

### Development Tools
- **Vite** - Build tool veloce
- **ESLint** - Linting codice
- **Prettier** - Formattazione codice

## 📱 Componenti Principali

### 🧭 Navigation.jsx
**Navigazione principale con sidebar responsive**

```jsx
// Features
- Navbar Bootstrap responsive
- Offcanvas mobile menu
- Breadcrumb navigation
- Active route highlighting
- Quick action buttons
```

### 🏠 Home.jsx
**Dashboard principale con ricerca avanzata**

```jsx
// Features
- Statistics cards
- Advanced search filters
- Course grid with cards
- Error handling
- Loading states
- Real-time search
```

### 📚 CourseDetail.jsx
**Pagina dettaglio corso completa**

```jsx
// Features
- Complete course information
- Tabbed interface (Overview, Schedule, Enrollments)
- Progress tracking
- Enrollment management
- Responsive design
- Interactive elements
```

### 🎯 CourseLanding.jsx
**Landing page marketing**

```jsx
// Features
- Hero section
- Course showcase
- Testimonials carousel
- Call-to-action sections
- Welcome offers
- Statistics display
```

## 🎨 Design System

### 🎨 Color Palette
```css
:root {
  --primary: #0d6efd;      /* Bootstrap primary */
  --secondary: #6c757d;    /* Bootstrap secondary */
  --success: #198754;      /* Bootstrap success */
  --warning: #ffc107;      /* Bootstrap warning */
  --danger: #dc3545;       /* Bootstrap danger */
  --info: #0dcaf0;         /* Bootstrap info */
  --light: #f8f9fa;        /* Bootstrap light */
  --dark: #212529;         /* Bootstrap dark */
}
```

### 📐 Layout System
- **Container**: Bootstrap container responsive
- **Grid**: 12-column grid system
- **Breakpoints**: xs, sm, md, lg, xl, xxl
- **Spacing**: Bootstrap spacing utilities

### 🎭 Animations
```css
/* Custom animations */
@keyframes fadeIn {
  from { opacity: 0; transform: translateY(20px); }
  to { opacity: 1; transform: translateY(0); }
}

@keyframes slideIn {
  from { transform: translateX(-100%); }
  to { transform: translateX(0); }
}
```

## 🔧 State Management

### React Query
```jsx
// Data fetching con caching
const { data, isLoading, error } = useQuery(
  ['courses', filters],
  () => getCourses(filters),
  {
    staleTime: 30000,
    cacheTime: 300000,
    retry: 2
  }
)
```

### Local State
```jsx
// useState per componenti
const [filters, setFilters] = useState({
  titolo: '',
  luogo: '',
  disponibili: false
})
```

## 🎯 Routing

### Route Configuration
```jsx
<Routes>
  <Route path="/" element={<Home />} />
  <Route path="/landing" element={<CourseLanding />} />
  <Route path="/course/:courseId" element={<CourseDetail />} />
  <Route path="/enrollments" element={<EnrollmentsEnhanced />} />
  <Route path="/create-enrollment" element={<CreateEnrollment />} />
  <Route path="/create-enrollment/:courseId" element={<CreateEnrollment />} />
  <Route path="/create-course" element={<CreateCourse />} />
</Routes>
```

### Navigation
```jsx
// Link components
<Link to="/course/1">Course Details</Link>
<Button as={Link} to="/enrollments">Enrollments</Button>
```

## 🔍 Search & Filters

### Advanced Search
```jsx
// Multi-field search
const searchFilters = {
  titolo: 'React',
  luogo: 'Lecce',
  docente: 'Mario',
  materia: 'Frontend',
  dataInizio: '2024-01-01',
  dataFine: '2024-12-31',
  durataMin: 20,
  durataMax: 80,
  disponibili: true
}
```

### Real-time Search
```jsx
// Debounced search
const debouncedFilters = useMemo(
  () => debounce(filters, 500),
  [filters]
)
```

## 🎨 UI Components

### Card Components
```jsx
<Card className="h-100 border-0 shadow-sm">
  <Card.Body className="d-flex flex-column">
    <Card.Title>{course.titolo}</Card.Title>
    <Card.Text>{course.descrizione}</Card.Text>
    <Button variant="primary">Iscriviti</Button>
  </Card.Body>
</Card>
```

### Form Components
```jsx
<Form.Group>
  <Form.Label>Titolo Corso</Form.Label>
  <Form.Control
    type="text"
    value={filters.titolo}
    onChange={(e) => handleFilterChange('titolo', e.target.value)}
  />
</Form.Group>
```

### Modal Components
```jsx
<Modal show={showModal} onHide={handleClose}>
  <Modal.Header closeButton>
    <Modal.Title>Course Details</Modal.Title>
  </Modal.Header>
  <Modal.Body>Content here</Modal.Body>
</Modal>
```

## 📱 Responsive Design

### Breakpoints
```jsx
// Bootstrap breakpoints
<Col xs={12} sm={6} md={4} lg={3}>
  <Card>Content</Card>
</Col>
```

### Mobile Navigation
```jsx
// Offcanvas for mobile
<Offcanvas show={show} onHide={handleClose}>
  <Offcanvas.Header closeButton>
    <Offcanvas.Title>Menu</Offcanvas.Title>
  </Offcanvas.Header>
  <Offcanvas.Body>Navigation items</Offcanvas.Body>
</Offcanvas>
```

## 🚀 Performance

### Code Splitting
```jsx
// Lazy loading components
const CourseDetail = lazy(() => import('./pages/CourseDetail'))
```

### Image Optimization
```jsx
// Optimized images
<img 
  src={courseImage} 
  alt={course.titolo}
  loading="lazy"
  className="img-fluid"
/>
```

### Caching Strategy
```jsx
// React Query caching
const queryClient = new QueryClient({
  defaultOptions: {
    queries: {
      staleTime: 5 * 60 * 1000, // 5 minutes
      cacheTime: 10 * 60 * 1000, // 10 minutes
    },
  },
})
```

## 🧪 Testing

### Component Testing
```jsx
// Jest + React Testing Library
import { render, screen } from '@testing-library/react'
import Home from './pages/Home'

test('renders course cards', () => {
  render(<Home />)
  expect(screen.getByText('Course Management System')).toBeInTheDocument()
})
```

### E2E Testing
```jsx
// Playwright tests
test('course search functionality', async ({ page }) => {
  await page.goto('/')
  await page.fill('[data-testid="search-input"]', 'React')
  await page.click('[data-testid="search-button"]')
  await expect(page.locator('.course-card')).toBeVisible()
})
```

## 🔧 Development

### Scripts
```json
{
  "scripts": {
    "dev": "vite",
    "build": "vite build",
    "preview": "vite preview",
    "lint": "eslint src --ext js,jsx",
    "test": "jest"
  }
}
```

### Environment Variables
```env
VITE_API_URL=http://localhost:8080
VITE_APP_TITLE=Course Management System
```

---

**Frontend Enterprise-Ready** con architettura moderna, performance ottimizzate e user experience eccellente.
