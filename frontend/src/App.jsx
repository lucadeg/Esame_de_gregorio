import React from 'react'
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom'
import { QueryClient, QueryClientProvider } from 'react-query'
import { Toaster } from 'react-hot-toast'
import 'bootstrap/dist/css/bootstrap.min.css'
import { AuthProvider } from './contexts/AuthContext'
import Home from './pages/Home'
import EnrollmentsEnhanced from './pages/EnrollmentsEnhanced'
import CreateEnrollment from './pages/CreateEnrollment'
import CreateCourse from './pages/CreateCourse'
import CourseLanding from './pages/CourseLanding'
import CourseDetail from './pages/CourseDetail'
import Login from './pages/Login'
import Register from './pages/Register'
import Subscription from './pages/Subscription'
import Navigation from './components/Navigation'
import ProtectedRoute from './components/ProtectedRoute'
import ThreeJSBackground from './components/ThreeJSBackground'
import './App.css'
import './styles/animations.css'

// Create React Query client / Crea client React Query
const queryClient = new QueryClient({
  defaultOptions: {
    queries: {
      staleTime: 5 * 60 * 1000, // 5 minutes / 5 minuti
      cacheTime: 10 * 60 * 1000, // 10 minutes / 10 minuti
    },
  },
})

/**
 * Main App Component
 * Componente Principale App
 * 
 * Root component that sets up routing and global providers
 * Componente root che configura routing e provider globali
 */
function App() {
  return (
    <QueryClientProvider client={queryClient}>
      <AuthProvider>
        <Router>
          <div className="min-h-screen bg-gray-50 relative">
            {/* 3D Background for entire site / Sfondo 3D per tutto il sito */}
            <ThreeJSBackground />
            
            <Navigation />
            <main className="container mx-auto px-4 py-8 relative z-10">
                    <Routes>
                      <Route path="/" element={<Home />} />
                      <Route path="/landing" element={<CourseLanding />} />
                      <Route path="/enrollments" element={
                        <ProtectedRoute>
                          <EnrollmentsEnhanced />
                        </ProtectedRoute>
                      } />
                      <Route path="/create-enrollment" element={
                        <ProtectedRoute>
                          <CreateEnrollment />
                        </ProtectedRoute>
                      } />
                      <Route path="/create-enrollment/:courseId" element={
                        <ProtectedRoute>
                          <CreateEnrollment />
                        </ProtectedRoute>
                      } />
                      <Route path="/create-course" element={
                        <ProtectedRoute>
                          <CreateCourse />
                        </ProtectedRoute>
                      } />
                      <Route path="/course/:courseId" element={<CourseDetail />} />
                      <Route path="/login" element={<Login />} />
                      <Route path="/register" element={<Register />} />
                      <Route path="/subscription" element={
                        <ProtectedRoute>
                          <Subscription />
                        </ProtectedRoute>
                      } />
                    </Routes>
            </main>
            <Toaster 
              position="top-right"
              toastOptions={{
                duration: 4000,
                style: {
                  background: '#363636',
                  color: '#fff',
                },
                success: {
                  duration: 3000,
                  iconTheme: {
                    primary: '#10b981',
                    secondary: '#fff',
                  },
                },
                error: {
                  duration: 5000,
                  iconTheme: {
                    primary: '#ef4444',
                    secondary: '#fff',
                  },
                },
              }}
            />
          </div>
        </Router>
      </AuthProvider>
    </QueryClientProvider>
  )
}

export default App
