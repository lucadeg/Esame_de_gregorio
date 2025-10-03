const { test, expect } = require('@playwright/test');

/**
 * Registration Test Suite
 * Suite di Test Registrazione
 * 
 * Comprehensive tests for user registration functionality
 * Test completi per la funzionalità di registrazione utente
 */
test.describe('User Registration', () => {
  
  test('should register new user successfully', async ({ page, request }) => {
    // Test successful registration / Test registrazione riuscita
    const userData = {
      nome: 'Test',
      cognome: 'User',
      email: `test.user.${Date.now()}@example.com`,
      password: 'password123',
      phoneNumber: '1234567890'
    };
    
    const response = await request.post('http://localhost:8080/api/v1/auth/register', {
      data: userData
    });
    
    expect(response.status()).toBe(201);
    const responseData = await response.json();
    expect(responseData.success).toBe(true);
    expect(responseData.data.email).toBe(userData.email);
    expect(responseData.data.nome).toBe(userData.nome);
    expect(responseData.data.cognome).toBe(userData.cognome);
  });
  
  test('should handle duplicate email error', async ({ page, request }) => {
    // Test duplicate email handling / Test gestione email duplicata
    const userData = {
      nome: 'Test',
      cognome: 'User',
      email: 'duplicate@example.com',
      password: 'password123'
    };
    
    // First registration should succeed / Prima registrazione dovrebbe riuscire
    const response1 = await request.post('http://localhost:8080/api/v1/auth/register', {
      data: userData
    });
    expect(response1.status()).toBe(201);
    
    // Second registration should fail / Seconda registrazione dovrebbe fallire
    const response2 = await request.post('http://localhost:8080/api/v1/auth/register', {
      data: userData
    });
    expect(response2.status()).toBe(400);
  });
  
  test('should validate required fields', async ({ page, request }) => {
    // Test validation of required fields / Test validazione campi obbligatori
    const invalidData = {
      nome: '',
      cognome: 'User',
      email: 'test@example.com',
      password: 'password123'
    };
    
    const response = await request.post('http://localhost:8080/api/v1/auth/register', {
      data: invalidData
    });
    
    expect(response.status()).toBe(400);
  });
  
  test('should validate email format', async ({ page, request }) => {
    // Test email format validation / Test validazione formato email
    const invalidData = {
      nome: 'Test',
      cognome: 'User',
      email: 'invalid-email',
      password: 'password123'
    };
    
    const response = await request.post('http://localhost:8080/api/v1/auth/register', {
      data: invalidData
    });
    
    expect(response.status()).toBe(400);
  });
  
  test('should validate password length', async ({ page, request }) => {
    // Test password length validation / Test validazione lunghezza password
    const invalidData = {
      nome: 'Test',
      cognome: 'User',
      email: 'test@example.com',
      password: '123' // Too short / Troppo corta
    };
    
    const response = await request.post('http://localhost:8080/api/v1/auth/register', {
      data: invalidData
    });
    
    expect(response.status()).toBe(400);
  });
  
  test('should handle frontend registration form', async ({ page }) => {
    // Test frontend registration form / Test form registrazione frontend
    await page.goto('http://localhost:5173/register');
    
    // Check if registration form is present / Controlla se form registrazione è presente
    await expect(page.locator('form')).toBeVisible();
    await expect(page.locator('input[name="nome"]')).toBeVisible();
    await expect(page.locator('input[name="cognome"]')).toBeVisible();
    await expect(page.locator('input[name="email"]')).toBeVisible();
    await expect(page.locator('input[name="password"]')).toBeVisible();
    await expect(page.locator('input[name="confirmPassword"]')).toBeVisible();
    
    // Fill registration form / Compila form registrazione
    await page.fill('input[name="nome"]', 'Frontend');
    await page.fill('input[name="cognome"]', 'Test');
    await page.fill('input[name="email"]', `frontend.test.${Date.now()}@example.com`);
    await page.fill('input[name="password"]', 'password123');
    await page.fill('input[name="confirmPassword"]', 'password123');
    
    // Submit form / Invia form
    await page.click('button[type="submit"]');
    
    // Check for success message or redirect / Controlla messaggio successo o redirect
    await expect(page.locator('text=Registrazione completata')).toBeVisible({ timeout: 10000 });
  });
  
  test('should handle password mismatch', async ({ page }) => {
    // Test password mismatch handling / Test gestione password non corrispondenti
    await page.goto('http://localhost:5173/register');
    
    // Fill form with mismatched passwords / Compila form con password non corrispondenti
    await page.fill('input[name="nome"]', 'Test');
    await page.fill('input[name="cognome"]', 'User');
    await page.fill('input[name="email"]', 'test@example.com');
    await page.fill('input[name="password"]', 'password123');
    await page.fill('input[name="confirmPassword"]', 'different123');
    
    // Submit form / Invia form
    await page.click('button[type="submit"]');
    
    // Check for error message / Controlla messaggio errore
    await expect(page.locator('text=Le password non coincidono')).toBeVisible();
  });
  
  test('should handle network errors gracefully', async ({ page }) => {
    // Test network error handling / Test gestione errori di rete
    await page.goto('http://localhost:5173/register');
    
    // Mock network failure / Simula fallimento rete
    await page.route('**/api/v1/auth/register', route => route.abort());
    
    // Fill and submit form / Compila e invia form
    await page.fill('input[name="nome"]', 'Test');
    await page.fill('input[name="cognome"]', 'User');
    await page.fill('input[name="email"]', 'test@example.com');
    await page.fill('input[name="password"]', 'password123');
    await page.fill('input[name="confirmPassword"]', 'password123');
    
    await page.click('button[type="submit"]');
    
    // Check for error message / Controlla messaggio errore
    await expect(page.locator('text=Errore durante la registrazione')).toBeVisible();
  });
});
