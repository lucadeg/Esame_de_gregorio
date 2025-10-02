import React from 'react'
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom'
import { QueryClient, QueryClientProvider } from 'react-query'
import { Toaster } from 'react-hot-toast'
import Home from './pages/Home'
import Enrollments from './pages/Enrollments'
import CreateEnrollment from './pages/CreateEnrollment'
import Navigation from './components/Navigation'
import './App.css'

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
      <Router>
        <div className="min-h-screen bg-gray-50">
          <Navigation />
          <main className="container mx-auto px-4 py-8">
            <Routes>
              <Route path="/" element={<Home />} />
              <Route path="/enrollments" element={<Enrollments />} />
              <Route path="/create-enrollment" element={<CreateEnrollment />} />
              <Route path="/create-enrollment/:courseId" element={<CreateEnrollment />} />
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
    </QueryClientProvider>
  )
}

export default App
