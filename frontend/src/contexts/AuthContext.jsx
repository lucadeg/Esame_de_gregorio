import React, { createContext, useContext, useState, useEffect } from 'react'

/**
 * Authentication Context
 * Contesto di Autenticazione
 * 
 * Manages global authentication state
 * Gestisce lo stato globale di autenticazione
 */
const AuthContext = createContext()

export const useAuth = () => {
  const context = useContext(AuthContext)
  if (!context) {
    throw new Error('useAuth must be used within an AuthProvider')
  }
  return context
}

export const AuthProvider = ({ children }) => {
  const [user, setUser] = useState(null)
  const [token, setToken] = useState(null)
  const [isLoading, setIsLoading] = useState(true)

  // Check if user is logged in on app start / Controlla se utente è loggato all'avvio app
  useEffect(() => {
    const checkAuth = () => {
      try {
        const storedUser = localStorage.getItem('user')
        const storedToken = localStorage.getItem('accessToken')
        
        if (storedUser && storedToken) {
          setUser(JSON.parse(storedUser))
          setToken(storedToken)
        }
      } catch (error) {
        console.error('Error checking auth:', error)
        // Clear invalid data / Pulisci dati non validi
        localStorage.removeItem('user')
        localStorage.removeItem('accessToken')
        localStorage.removeItem('refreshToken')
      } finally {
        setIsLoading(false)
      }
    }

    checkAuth()
  }, [])

  // Login function / Funzione login
  const login = (userData, accessToken, refreshToken) => {
    setUser(userData)
    setToken(accessToken)
    localStorage.setItem('user', JSON.stringify(userData))
    localStorage.setItem('accessToken', accessToken)
    localStorage.setItem('refreshToken', refreshToken)
  }

  // Logout function / Funzione logout
  const logout = () => {
    setUser(null)
    setToken(null)
    localStorage.removeItem('user')
    localStorage.removeItem('accessToken')
    localStorage.removeItem('refreshToken')
  }

  // Check if user is authenticated / Controlla se utente è autenticato
  const isAuthenticated = () => {
    return user && token
  }

  // Get user display name / Ottieni nome visualizzazione utente
  const getUserDisplayName = () => {
    if (!user) return ''
    return `${user.nome} ${user.cognome}`.trim() || user.email
  }

  const value = {
    user,
    token,
    isLoading,
    login,
    logout,
    isAuthenticated,
    getUserDisplayName
  }

  return (
    <AuthContext.Provider value={value}>
      {children}
    </AuthContext.Provider>
  )
}
