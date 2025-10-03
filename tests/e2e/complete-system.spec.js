import { test, expect } from '@playwright/test'

/**
 * Complete System E2E Tests
 * Test E2E Sistema Completo
 * 
 * Comprehensive end-to-end tests for the entire system
 * Test end-to-end completi per l'intero sistema
 */
test.describe('Complete System Integration', () => {
  
  test('Full User Journey - Registration to Course Enrollment', async ({ page }) => {
    // 1. User Registration / Registrazione Utente
    await page.goto('http://localhost:5173/')
    await page.click('text=Registrati')
    
    await page.fill('input[name="nome"]', 'Test User')
    await page.fill('input[name="cognome"]', 'Test Surname')
    await page.fill('input[name="email"]', 'testuser@example.com')
    await page.fill('input[name="password"]', 'password123')
    await page.fill('input[name="confirmPassword"]', 'password123')
    await page.click('button[type="submit"]')
    
    // Wait for redirect to login / Aspetta reindirizzamento a login
    await expect(page).toHaveURL('http://localhost:5173/login')
    
    // 2. User Login / Accesso Utente
    await page.fill('input[name="email"]', 'testuser@example.com')
    await page.fill('input[name="password"]', 'password123')
    await page.click('button[type="submit"]')
    
    // Should redirect to home / Dovrebbe reindirizzare alla home
    await expect(page).toHaveURL('http://localhost:5173/')
    await expect(page.locator('text=Test User')).toBeVisible()
    
    // 3. View Courses / Visualizza Corsi
    await expect(page.locator('.card')).toHaveCount.greaterThan(0)
    await expect(page.locator('text=React Fundamentals')).toBeVisible()
    
    // 4. Search Courses / Cerca Corsi
    await page.fill('input[name="titolo"]', 'React')
    await page.click('button:has-text("Cerca")')
    
    // Should show filtered results / Dovrebbe mostrare risultati filtrati
    await expect(page.locator('.card')).toHaveCount.greaterThan(0)
    
    // 5. View Course Details / Visualizza Dettagli Corso
    await page.click('text=Scheda Corso')
    await expect(page).toHaveURL(/\/course\/\d+/)
    
    // Should show course details / Dovrebbe mostrare dettagli corso
    await expect(page.locator('h1')).toBeVisible()
    await expect(page.locator('text=Informazioni')).toBeVisible()
    
    // 6. Create Enrollment / Crea Iscrizione
    await page.click('text=Iscriviti')
    await expect(page).toHaveURL(/\/create-enrollment\/\d+/)
    
    // Fill enrollment form / Compila form iscrizione
    await page.fill('input[name="nome"]', 'Test User')
    await page.fill('input[name="cognome"]', 'Test Surname')
    await page.fill('input[name="email"]', 'testuser@example.com')
    await page.click('button[type="submit"]')
    
    // Should redirect to enrollments / Dovrebbe reindirizzare alle iscrizioni
    await expect(page).toHaveURL('http://localhost:5173/enrollments')
    
    // 7. View Enrollments / Visualizza Iscrizioni
    await expect(page.locator('text=Test User')).toBeVisible()
    await expect(page.locator('text=Test Surname')).toBeVisible()
    
    // 8. Check Subscription / Controlla Abbonamento
    await page.click('button:has-text("Test User")')
    await page.click('text=Abbonamenti')
    await expect(page).toHaveURL('http://localhost:5173/subscription')
    
    // Should show subscription info / Dovrebbe mostrare info abbonamento
    await expect(page.locator('text=Piano FREE')).toBeVisible()
    await expect(page.locator('text=Attivo')).toBeVisible()
  })

  test('Admin User Journey - Course Management', async ({ page }) => {
    // 1. Admin Login / Login Admin
    await page.goto('http://localhost:5173/')
    await page.click('text=Login')
    await page.fill('input[name="email"]', 'admin@coursehub.com')
    await page.fill('input[name="password"]', 'admin123')
    await page.click('button[type="submit"]')
    
    await expect(page).toHaveURL('http://localhost:5173/')
    await expect(page.locator('text=Admin')).toBeVisible()
    
    // 2. Create New Course / Crea Nuovo Corso
    await page.click('text=New Course')
    await expect(page).toHaveURL('http://localhost:5173/create-course')
    
    // Fill course form / Compila form corso
    await page.fill('input[name="titolo"]', 'Test Course')
    await page.fill('textarea[name="descrizione"]', 'Test course description')
    await page.fill('input[name="dataOraInizio"]', '2025-12-01T09:00')
    await page.fill('input[name="dataOraFine"]', '2025-12-03T17:00')
    await page.fill('input[name="luogo"]', 'Test Location')
    await page.fill('input[name="disponibilita"]', '20')
    await page.fill('input[name="prezzo"]', '199.99')
    await page.fill('input[name="docenti"]', 'Test Instructor')
    await page.fill('textarea[name="programma"]', 'Test program content')
    await page.fill('textarea[name="informazioniGenerali"]', 'Test general information')
    await page.fill('input[name="durataOre"]', '24')
    
    await page.click('button[type="submit"]')
    
    // Should redirect to home / Dovrebbe reindirizzare alla home
    await expect(page).toHaveURL('http://localhost:5173/')
    
    // 3. Verify Course Creation / Verifica Creazione Corso
    await expect(page.locator('text=Test Course')).toBeVisible()
    
    // 4. View Course Details / Visualizza Dettagli Corso
    await page.click('text=Scheda Corso')
    await expect(page).toHaveURL(/\/course\/\d+/)
    await expect(page.locator('text=Test Course')).toBeVisible()
    
    // 5. View Enrollments / Visualizza Iscrizioni
    await page.goto('http://localhost:5173/enrollments')
    await expect(page.locator('h1')).toContainText('Gestione Iscrizioni')
  })

  test('Instructor User Journey - Course Management', async ({ page }) => {
    // 1. Instructor Login / Login Istruttore
    await page.goto('http://localhost:5173/')
    await page.click('text=Login')
    await page.fill('input[name="email"]', 'giulia.bianchi@email.com')
    await page.fill('input[name="password"]', 'password123')
    await page.click('button[type="submit"]')
    
    await expect(page).toHaveURL('http://localhost:5173/')
    await expect(page.locator('text=Giulia')).toBeVisible()
    
    // 2. View Courses / Visualizza Corsi
    await expect(page.locator('.card')).toHaveCount.greaterThan(0)
    
    // 3. Search and Filter Courses / Cerca e Filtra Corsi
    await page.fill('input[name="docente"]', 'Giulia')
    await page.click('button:has-text("Cerca")')
    
    // Should show filtered results / Dovrebbe mostrare risultati filtrati
    await expect(page.locator('.card')).toHaveCount.greaterThan(0)
    
    // 4. View Course Details / Visualizza Dettagli Corso
    await page.click('text=Scheda Corso')
    await expect(page).toHaveURL(/\/course\/\d+/)
    
    // 5. Check Subscription / Controlla Abbonamento
    await page.click('button:has-text("Giulia")')
    await page.click('text=Abbonamenti')
    await expect(page.locator('text=Piano PREMIUM')).toBeVisible()
  })

  test('System Performance - Multiple Users', async ({ page, context }) => {
    // Create multiple browser contexts / Crea contesti browser multipli
    const context2 = await context.browser().newContext()
    const page2 = await context2.newPage()
    
    // User 1 - Student / Utente 1 - Studente
    await page.goto('http://localhost:5173/')
    await page.click('text=Login')
    await page.fill('input[name="email"]', 'mario.rossi@email.com')
    await page.fill('input[name="password"]', 'password123')
    await page.click('button[type="submit"]')
    
    // User 2 - Instructor / Utente 2 - Istruttore
    await page2.goto('http://localhost:5173/')
    await page2.click('text=Login')
    await page2.fill('input[name="email"]', 'giulia.bianchi@email.com')
    await page2.fill('input[name="password"]', 'password123')
    await page2.click('button[type="submit"]')
    
    // Both users should be logged in / Entrambi gli utenti dovrebbero essere loggati
    await expect(page.locator('text=Mario')).toBeVisible()
    await expect(page2.locator('text=Giulia')).toBeVisible()
    
    // User 1 browses courses / Utente 1 naviga corsi
    await page.click('text=React Fundamentals')
    
    // User 2 creates course / Utente 2 crea corso
    await page2.click('text=New Course')
    await page2.fill('input[name="titolo"]', 'Concurrent Test Course')
    await page2.fill('input[name="luogo"]', 'Test Location')
    await page2.fill('input[name="disponibilita"]', '10')
    await page2.click('button[type="submit"]')
    
    // Both operations should complete successfully / Entrambe le operazioni dovrebbero completarsi con successo
    await expect(page).toHaveURL(/\/course\/\d+/)
    await expect(page2).toHaveURL('http://localhost:5173/')
    
    await context2.close()
  })

  test('Error Handling - Network Failures', async ({ page }) => {
    // Mock network failure / Simula fallimento rete
    await page.route('**/courses**', route => route.abort())
    
    await page.goto('http://localhost:5173/')
    
    // Should show error message / Dovrebbe mostrare messaggio di errore
    await expect(page.locator('.alert-danger')).toBeVisible()
    await expect(page.locator('text=Errore nella Ricerca')).toBeVisible()
  })

  test('Data Persistence - Page Refresh', async ({ page }) => {
    // Login and navigate / Login e naviga
    await page.goto('http://localhost:5173/')
    await page.click('text=Login')
    await page.fill('input[name="email"]', 'mario.rossi@email.com')
    await page.fill('input[name="password"]', 'password123')
    await page.click('button[type="submit"]')
    
    await expect(page.locator('text=Mario')).toBeVisible()
    
    // Refresh page / Aggiorna pagina
    await page.reload()
    
    // User should still be logged in / Utente dovrebbe essere ancora loggato
    await expect(page.locator('text=Mario')).toBeVisible()
  })

  test('Cross-Browser Compatibility', async ({ page }) => {
    // Test basic functionality across different browsers / Testa funzionalità base su browser diversi
    await page.goto('http://localhost:5173/')
    
    // Check all main elements are present / Controlla che tutti gli elementi principali siano presenti
    await expect(page.locator('text=Course Management System')).toBeVisible()
    await expect(page.locator('text=Login')).toBeVisible()
    await expect(page.locator('text=Registrati')).toBeVisible()
    
    // Test navigation / Testa navigazione
    await page.click('text=Login')
    await expect(page).toHaveURL('http://localhost:5173/login')
    
    await page.click('text=Registrati')
    await expect(page).toHaveURL('http://localhost:5173/register')
  })

  test('Accessibility - Keyboard Navigation', async ({ page }) => {
    await page.goto('http://localhost:5173/')
    
    // Navigate using keyboard / Naviga usando tastiera
    await page.keyboard.press('Tab')
    await page.keyboard.press('Tab')
    await page.keyboard.press('Enter') // Should click Login button / Dovrebbe cliccare pulsante Login
    
    await expect(page).toHaveURL('http://localhost:5173/login')
    
    // Fill form using keyboard / Compila form usando tastiera
    await page.keyboard.type('mario.rossi@email.com')
    await page.keyboard.press('Tab')
    await page.keyboard.type('password123')
    await page.keyboard.press('Tab')
    await page.keyboard.press('Enter') // Submit form / Invia form
    
    await expect(page).toHaveURL('http://localhost:5173/')
  })

  test('Mobile Responsiveness', async ({ page }) => {
    // Set mobile viewport / Imposta viewport mobile
    await page.setViewportSize({ width: 375, height: 667 })
    
    await page.goto('http://localhost:5173/')
    
    // Check mobile navigation / Controlla navigazione mobile
    await expect(page.locator('.navbar-toggler')).toBeVisible()
    
    // Test mobile menu / Testa menu mobile
    await page.click('.navbar-toggler')
    await expect(page.locator('.offcanvas')).toBeVisible()
    
    // Test mobile login / Testa login mobile
    await page.click('text=Login')
    await expect(page).toHaveURL('http://localhost:5173/login')
    
    // Form should be usable on mobile / Form dovrebbe essere utilizzabile su mobile
    await page.fill('input[name="email"]', 'mario.rossi@email.com')
    await page.fill('input[name="password"]', 'password123')
    await page.click('button[type="submit"]')
    
    await expect(page).toHaveURL('http://localhost:5173/')
  })
})
