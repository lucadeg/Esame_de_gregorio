import { test, expect } from '@playwright/test'

/**
 * Subscription Management E2E Tests
 * Test E2E Gestione Abbonamenti
 * 
 * End-to-end tests for subscription management functionality
 * Test end-to-end per funzionalità gestione abbonamenti
 */
test.describe('Subscription Management', () => {
  
  test.beforeEach(async ({ page }) => {
    // Login before each test / Login prima di ogni test
    await page.goto('http://localhost:5173/')
    await page.click('text=Login')
    await page.fill('input[name="email"]', 'mario.rossi@email.com')
    await page.fill('input[name="password"]', 'password123')
    await page.click('button[type="submit"]')
    
    // Wait for login to complete / Aspetta completamento login
    await expect(page.locator('text=Mario')).toBeVisible()
  })

  test('Access Subscription Page', async ({ page }) => {
    // Click on user dropdown / Clicca sul dropdown utente
    await page.click('button:has-text("Mario")')
    
    // Click on subscription link / Clicca sul link abbonamenti
    await page.click('text=Abbonamenti')
    await expect(page).toHaveURL('http://localhost:5173/subscription')
    
    // Check page title / Controlla titolo pagina
    await expect(page.locator('h1')).toContainText('Gestione Abbonamenti')
  })

  test('View Current Subscription', async ({ page }) => {
    await page.goto('http://localhost:5173/subscription')
    
    // Check current subscription is displayed / Controlla che abbonamento corrente sia mostrato
    await expect(page.locator('.card.border-primary')).toBeVisible()
    await expect(page.locator('text=Piano FREE')).toBeVisible()
    await expect(page.locator('text=Attivo')).toBeVisible()
  })

  test('View All Subscription Plans', async ({ page }) => {
    await page.goto('http://localhost:5173/subscription')
    
    // Check all subscription plans are displayed / Controlla che tutti i piani abbonamento siano mostrati
    await expect(page.locator('text=Gratuito')).toBeVisible()
    await expect(page.locator('text=Base')).toBeVisible()
    await expect(page.locator('text=Premium')).toBeVisible()
    await expect(page.locator('text=Enterprise')).toBeVisible()
    
    // Check plan features / Controlla funzionalità piani
    await expect(page.locator('text=€9.99')).toBeVisible() // Basic plan
    await expect(page.locator('text=€19.99')).toBeVisible() // Premium plan
    await expect(page.locator('text=€49.99')).toBeVisible() // Enterprise plan
  })

  test('Subscription Plan Features', async ({ page }) => {
    await page.goto('http://localhost:5173/subscription')
    
    // Check FREE plan features / Controlla funzionalità piano FREE
    const freePlan = page.locator('.card').filter({ hasText: 'Gratuito' })
    await expect(freePlan.locator('text=3 corsi inclusi')).toBeVisible()
    await expect(freePlan.locator('text=Accesso ai materiali')).toBeVisible()
    
    // Check PREMIUM plan features / Controlla funzionalità piano PREMIUM
    const premiumPlan = page.locator('.card').filter({ hasText: 'Premium' })
    await expect(premiumPlan.locator('text=25 corsi inclusi')).toBeVisible()
    await expect(premiumPlan.locator('text=Funzionalità avanzate')).toBeVisible()
    await expect(premiumPlan.locator('text=Supporto prioritario')).toBeVisible()
  })

  test('Upgrade Subscription Plan', async ({ page }) => {
    await page.goto('http://localhost:5173/subscription')
    
    // Click on BASIC plan upgrade button / Clicca sul pulsante upgrade piano BASIC
    const basicPlan = page.locator('.card').filter({ hasText: 'Base' })
    await basicPlan.locator('button:has-text("Scegli Piano")').click()
    
    // Check upgrade modal is displayed / Controlla che modal upgrade sia mostrato
    await expect(page.locator('.modal')).toBeVisible()
    await expect(page.locator('.modal-title')).toContainText('Conferma Upgrade')
    await expect(page.locator('text=Piano Base')).toBeVisible()
    await expect(page.locator('text=€9.99')).toBeVisible()
    
    // Confirm upgrade / Conferma upgrade
    await page.click('button:has-text("Conferma Upgrade")')
    
    // Modal should close / Modal dovrebbe chiudersi
    await expect(page.locator('.modal')).not.toBeVisible()
  })

  test('Cancel Subscription Upgrade', async ({ page }) => {
    await page.goto('http://localhost:5173/subscription')
    
    // Click on BASIC plan upgrade button / Clicca sul pulsante upgrade piano BASIC
    const basicPlan = page.locator('.card').filter({ hasText: 'Base' })
    await basicPlan.locator('button:has-text("Scegli Piano")').click()
    
    // Check upgrade modal is displayed / Controlla che modal upgrade sia mostrato
    await expect(page.locator('.modal')).toBeVisible()
    
    // Cancel upgrade / Annulla upgrade
    await page.click('button:has-text("Annulla")')
    
    // Modal should close / Modal dovrebbe chiudersi
    await expect(page.locator('.modal')).not.toBeVisible()
  })

  test('Current Plan Button State', async ({ page }) => {
    await page.goto('http://localhost:5173/subscription')
    
    // Check current plan shows "Piano Attuale" button / Controlla che piano corrente mostri pulsante "Piano Attuale"
    const freePlan = page.locator('.card').filter({ hasText: 'Gratuito' })
    await expect(freePlan.locator('button:has-text("Piano Attuale")')).toBeVisible()
    await expect(freePlan.locator('button:has-text("Piano Attuale")')).toBeDisabled()
  })

  test('Subscription Plan Comparison', async ({ page }) => {
    await page.goto('http://localhost:5173/subscription')
    
    // Check plan pricing / Controlla prezzi piani
    await expect(page.locator('text=Gratuito')).toBeVisible()
    await expect(page.locator('text=€9.99/mese')).toBeVisible()
    await expect(page.locator('text=€19.99/mese')).toBeVisible()
    await expect(page.locator('text=€49.99/mese')).toBeVisible()
    
    // Check plan limits / Controlla limiti piani
    await expect(page.locator('text=3 corsi inclusi')).toBeVisible()
    await expect(page.locator('text=10 corsi inclusi')).toBeVisible()
    await expect(page.locator('text=25 corsi inclusi')).toBeVisible()
    await expect(page.locator('text=100 corsi inclusi')).toBeVisible()
  })

  test('Subscription Statistics Display', async ({ page }) => {
    await page.goto('http://localhost:5173/subscription')
    
    // Check statistics are displayed / Controlla che statistiche siano mostrate
    await expect(page.locator('text=Giorni rimanenti')).toBeVisible()
    await expect(page.locator('text=Max corsi')).toBeVisible()
    await expect(page.locator('text=Scadenza')).toBeVisible()
  })

  test('Responsive Design - Mobile View', async ({ page }) => {
    // Set mobile viewport / Imposta viewport mobile
    await page.setViewportSize({ width: 375, height: 667 })
    
    await page.goto('http://localhost:5173/subscription')
    
    // Check mobile layout / Controlla layout mobile
    await expect(page.locator('h1')).toBeVisible()
    await expect(page.locator('.card')).toHaveCount(4) // 4 subscription plans
    
    // Check cards are stacked vertically on mobile / Controlla che card siano impilate verticalmente su mobile
    const cards = page.locator('.card')
    const firstCard = cards.first()
    const secondCard = cards.nth(1)
    
    const firstCardBox = await firstCard.boundingBox()
    const secondCardBox = await secondCard.boundingBox()
    
    // Second card should be below first card / Seconda card dovrebbe essere sotto la prima
    expect(secondCardBox.y).toBeGreaterThan(firstCardBox.y + firstCardBox.height)
  })

  test('Navigation Back to Home', async ({ page }) => {
    await page.goto('http://localhost:5173/subscription')
    
    // Click on home link / Clicca sul link home
    await page.click('text=Home')
    await expect(page).toHaveURL('http://localhost:5173/')
  })

  test('Subscription Plan Icons', async ({ page }) => {
    await page.goto('http://localhost:5173/subscription')
    
    // Check plan icons are displayed / Controlla che icone piani siano mostrate
    const freePlan = page.locator('.card').filter({ hasText: 'Gratuito' })
    await expect(freePlan.locator('svg')).toBeVisible()
    
    const basicPlan = page.locator('.card').filter({ hasText: 'Base' })
    await expect(basicPlan.locator('svg')).toBeVisible()
    
    const premiumPlan = page.locator('.card').filter({ hasText: 'Premium' })
    await expect(premiumPlan.locator('svg')).toBeVisible()
    
    const enterprisePlan = page.locator('.card').filter({ hasText: 'Enterprise' })
    await expect(enterprisePlan.locator('svg')).toBeVisible()
  })

  test('Subscription Plan Colors', async ({ page }) => {
    await page.goto('http://localhost:5173/subscription')
    
    // Check plan colors are different / Controlla che colori piani siano diversi
    const freePlan = page.locator('.card').filter({ hasText: 'Gratuito' })
    const basicPlan = page.locator('.card').filter({ hasText: 'Base' })
    const premiumPlan = page.locator('.card').filter({ hasText: 'Premium' })
    const enterprisePlan = page.locator('.card').filter({ hasText: 'Enterprise' })
    
    // Each plan should have different styling / Ogni piano dovrebbe avere styling diverso
    await expect(freePlan).toBeVisible()
    await expect(basicPlan).toBeVisible()
    await expect(premiumPlan).toBeVisible()
    await expect(enterprisePlan).toBeVisible()
  })

  test('Error Handling - Network Error', async ({ page }) => {
    // Mock network failure / Simula fallimento rete
    await page.route('**/subscriptions/**', route => route.abort())
    
    await page.goto('http://localhost:5173/subscription')
    
    // Should show error message / Dovrebbe mostrare messaggio di errore
    await expect(page.locator('.alert-danger')).toBeVisible()
  })

  test('Loading State Display', async ({ page }) => {
    // Slow down network / Rallenta rete
    await page.route('**/subscriptions/**', route => {
      setTimeout(() => route.continue(), 1000)
    })
    
    await page.goto('http://localhost:5173/subscription')
    
    // Should show loading spinner / Dovrebbe mostrare spinner di caricamento
    await expect(page.locator('.spinner-border')).toBeVisible()
  })
})
