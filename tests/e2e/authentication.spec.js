import { test, expect } from '@playwright/test'

/**
 * Authentication E2E Tests
 * Test E2E Autenticazione
 * 
 * End-to-end tests for user authentication and registration
 * Test end-to-end per autenticazione e registrazione utenti
 */
test.describe('Authentication System', () => {
  
  test.beforeEach(async ({ page }) => {
    // Navigate to home page before each test / Naviga alla home page prima di ogni test
    await page.goto('http://localhost:5173/')
  })

  test('User Registration Flow', async ({ page }) => {
    // Click on register button / Clicca sul pulsante registrati
    await page.click('text=Registrati')
    await expect(page).toHaveURL('http://localhost:5173/register')

    // Fill registration form / Compila form registrazione
    await page.fill('input[name="nome"]', 'Test User')
    await page.fill('input[name="cognome"]', 'Test Surname')
    await page.fill('input[name="email"]', 'test.user@example.com')
    await page.fill('input[name="phoneNumber"]', '+39 123 456 7890')
    await page.fill('input[name="dateOfBirth"]', '1990-01-01')
    await page.fill('input[name="password"]', 'password123')
    await page.fill('input[name="confirmPassword"]', 'password123')

    // Submit registration form / Invia form registrazione
    await page.click('button[type="submit"]')

    // Wait for success message / Aspetta messaggio di successo
    await expect(page.locator('.alert-success')).toBeVisible()
    await expect(page.locator('.alert-success')).toContainText('Registrazione completata con successo')

    // Should redirect to login page / Dovrebbe reindirizzare alla pagina login
    await expect(page).toHaveURL('http://localhost:5173/login')
  })

  test('User Login Flow', async ({ page }) => {
    // Click on login button / Clicca sul pulsante login
    await page.click('text=Login')
    await expect(page).toHaveURL('http://localhost:5173/login')

    // Fill login form with demo credentials / Compila form login con credenziali demo
    await page.fill('input[name="email"]', 'mario.rossi@email.com')
    await page.fill('input[name="password"]', 'password123')

    // Submit login form / Invia form login
    await page.click('button[type="submit"]')

    // Should redirect to home page / Dovrebbe reindirizzare alla home page
    await expect(page).toHaveURL('http://localhost:5173/')

    // Should show user dropdown menu / Dovrebbe mostrare menu dropdown utente
    await expect(page.locator('text=Mario')).toBeVisible()
  })

  test('User Logout Flow', async ({ page }) => {
    // First login / Prima effettua login
    await page.click('text=Login')
    await page.fill('input[name="email"]', 'mario.rossi@email.com')
    await page.fill('input[name="password"]', 'password123')
    await page.click('button[type="submit"]')

    // Wait for login to complete / Aspetta completamento login
    await expect(page.locator('text=Mario')).toBeVisible()

    // Click on user dropdown / Clicca sul dropdown utente
    await page.click('button:has-text("Mario")')

    // Click logout / Clicca logout
    await page.click('text=Logout')

    // Should show login/register buttons / Dovrebbe mostrare pulsanti login/registrati
    await expect(page.locator('text=Login')).toBeVisible()
    await expect(page.locator('text=Registrati')).toBeVisible()
  })

  test('Login with Invalid Credentials', async ({ page }) => {
    await page.click('text=Login')
    await page.fill('input[name="email"]', 'invalid@email.com')
    await page.fill('input[name="password"]', 'wrongpassword')
    await page.click('button[type="submit"]')

    // Should show error message / Dovrebbe mostrare messaggio di errore
    await expect(page.locator('.alert-danger')).toBeVisible()
    await expect(page.locator('.alert-danger')).toContainText('Credenziali non valide')
  })

  test('Registration with Existing Email', async ({ page }) => {
    await page.click('text=Registrati')
    await page.fill('input[name="nome"]', 'Test User')
    await page.fill('input[name="cognome"]', 'Test Surname')
    await page.fill('input[name="email"]', 'mario.rossi@email.com') // Existing email
    await page.fill('input[name="password"]', 'password123')
    await page.fill('input[name="confirmPassword"]', 'password123')
    await page.click('button[type="submit"]')

    // Should show error message / Dovrebbe mostrare messaggio di errore
    await expect(page.locator('.alert-danger')).toBeVisible()
    await expect(page.locator('.alert-danger')).toContainText('Email già registrata')
  })

  test('Registration with Mismatched Passwords', async ({ page }) => {
    await page.click('text=Registrati')
    await page.fill('input[name="nome"]', 'Test User')
    await page.fill('input[name="cognome"]', 'Test Surname')
    await page.fill('input[name="email"]', 'newuser@example.com')
    await page.fill('input[name="password"]', 'password123')
    await page.fill('input[name="confirmPassword"]', 'differentpassword')
    await page.click('button[type="submit"]')

    // Should show error message / Dovrebbe mostrare messaggio di errore
    await expect(page.locator('.alert-danger')).toBeVisible()
    await expect(page.locator('.alert-danger')).toContainText('Le password non coincidono')
  })

  test('Registration with Short Password', async ({ page }) => {
    await page.click('text=Registrati')
    await page.fill('input[name="nome"]', 'Test User')
    await page.fill('input[name="cognome"]', 'Test Surname')
    await page.fill('input[name="email"]', 'newuser@example.com')
    await page.fill('input[name="password"]', '123') // Too short
    await page.fill('input[name="confirmPassword"]', '123')
    await page.click('button[type="submit"]')

    // Should show error message / Dovrebbe mostrare messaggio di errore
    await expect(page.locator('.alert-danger')).toBeVisible()
    await expect(page.locator('.alert-danger')).toContainText('La password deve essere di almeno 6 caratteri')
  })

  test('Form Validation - Required Fields', async ({ page }) => {
    await page.click('text=Registrati')
    
    // Try to submit empty form / Prova a inviare form vuoto
    await page.click('button[type="submit"]')

    // Should show validation errors / Dovrebbe mostrare errori di validazione
    await expect(page.locator('input[name="nome"]:invalid')).toBeVisible()
    await expect(page.locator('input[name="cognome"]:invalid')).toBeVisible()
    await expect(page.locator('input[name="email"]:invalid')).toBeVisible()
    await expect(page.locator('input[name="password"]:invalid')).toBeVisible()
  })

  test('Password Visibility Toggle', async ({ page }) => {
    await page.click('text=Login')
    
    // Check password field type / Controlla tipo campo password
    const passwordField = page.locator('input[name="password"]')
    await expect(passwordField).toHaveAttribute('type', 'password')
    
    // Click visibility toggle / Clicca toggle visibilità
    await page.click('button:has(svg)')
    
    // Password should be visible / Password dovrebbe essere visibile
    await expect(passwordField).toHaveAttribute('type', 'text')
    
    // Click again to hide / Clicca di nuovo per nascondere
    await page.click('button:has(svg)')
    await expect(passwordField).toHaveAttribute('type', 'password')
  })

  test('Navigation Between Auth Pages', async ({ page }) => {
    // Start from home / Inizia dalla home
    await page.click('text=Login')
    await expect(page).toHaveURL('http://localhost:5173/login')
    
    // Navigate to register / Naviga a registrazione
    await page.click('text=Registrati')
    await expect(page).toHaveURL('http://localhost:5173/register')
    
    // Navigate back to login / Naviga indietro al login
    await page.click('text=Accedi')
    await expect(page).toHaveURL('http://localhost:5173/login')
  })

  test('Demo Credentials Display', async ({ page }) => {
    await page.click('text=Login')
    
    // Check demo credentials are displayed / Controlla che credenziali demo siano mostrate
    await expect(page.locator('text=Credenziali Demo')).toBeVisible()
    await expect(page.locator('text=mario.rossi@email.com')).toBeVisible()
    await expect(page.locator('text=giulia.bianchi@email.com')).toBeVisible()
    await expect(page.locator('text=admin@coursehub.com')).toBeVisible()
  })

  test('Responsive Design - Mobile View', async ({ page }) => {
    // Set mobile viewport / Imposta viewport mobile
    await page.setViewportSize({ width: 375, height: 667 })
    
    await page.click('text=Login')
    
    // Check mobile navigation / Controlla navigazione mobile
    await expect(page.locator('.navbar-toggler')).toBeVisible()
    
    // Check form is still functional / Controlla che form sia ancora funzionale
    await page.fill('input[name="email"]', 'mario.rossi@email.com')
    await page.fill('input[name="password"]', 'password123')
    await page.click('button[type="submit"]')
    
    // Should still work on mobile / Dovrebbe funzionare ancora su mobile
    await expect(page).toHaveURL('http://localhost:5173/')
  })
})
