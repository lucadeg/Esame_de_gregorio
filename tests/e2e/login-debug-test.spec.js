const { test, expect } = require('@playwright/test');

/**
 * Login Debug Test Suite
 * Suite di Test Debug Login
 * 
 * Comprehensive debugging tests for login functionality
 * Test completi di debug per la funzionalità di login
 */
test.describe('Login Debug Tests', () => {
  
  test('should debug login process step by step', async ({ page, request }) => {
    console.log('=== DEBUG LOGIN PROCESS ===');
    
    // Step 1: Register a new user / Passo 1: Registra nuovo utente
    console.log('Step 1: Registering new user...');
    const userData = {
      nome: 'Debug',
      cognome: 'User',
      email: `debug.user.${Date.now()}@example.com`,
      password: 'password123'
    };
    
    const registerResponse = await request.post('http://localhost:8080/api/v1/auth/register', {
      data: userData
    });
    
    console.log('Register response status:', registerResponse.status());
    console.log('Register response data:', await registerResponse.json());
    
    expect(registerResponse.status()).toBe(201);
    
    // Step 2: Test login with correct credentials / Passo 2: Test login con credenziali corrette
    console.log('Step 2: Testing login with correct credentials...');
    const loginData = {
      email: userData.email,
      password: userData.password
    };
    
    const loginResponse = await request.post('http://localhost:8080/api/v1/auth/login', {
      data: loginData
    });
    
    console.log('Login response status:', loginResponse.status());
    console.log('Login response headers:', loginResponse.headers());
    
    if (loginResponse.status() !== 200) {
      const errorData = await loginResponse.text();
      console.log('Login error response:', errorData);
    } else {
      const loginResult = await loginResponse.json();
      console.log('Login success response:', loginResult);
    }
    
    // Step 3: Test login with wrong password / Passo 3: Test login con password sbagliata
    console.log('Step 3: Testing login with wrong password...');
    const wrongLoginData = {
      email: userData.email,
      password: 'wrongpassword'
    };
    
    const wrongLoginResponse = await request.post('http://localhost:8080/api/v1/auth/login', {
      data: wrongLoginData
    });
    
    console.log('Wrong login response status:', wrongLoginResponse.status());
    
    // Step 4: Test login with non-existent user / Passo 4: Test login con utente inesistente
    console.log('Step 4: Testing login with non-existent user...');
    const nonExistentLoginData = {
      email: 'nonexistent@example.com',
      password: 'password123'
    };
    
    const nonExistentLoginResponse = await request.post('http://localhost:8080/api/v1/auth/login', {
      data: nonExistentLoginData
    });
    
    console.log('Non-existent user login response status:', nonExistentLoginResponse.status());
    
    // Step 5: Check user exists in database / Passo 5: Controlla se utente esiste nel database
    console.log('Step 5: Checking if user exists in database...');
    const usersResponse = await request.get('http://localhost:8080/api/v1/users');
    console.log('Users response status:', usersResponse.status());
    
    if (usersResponse.status() === 200) {
      const users = await usersResponse.json();
      console.log('Users in database:', users);
    }
    
    // Step 6: Test health endpoint / Passo 6: Test endpoint health
    console.log('Step 6: Testing health endpoint...');
    const healthResponse = await request.get('http://localhost:8080/actuator/health');
    console.log('Health response status:', healthResponse.status());
    console.log('Health response data:', await healthResponse.json());
    
    console.log('=== DEBUG COMPLETE ===');
  });
  
  test('should test frontend login form', async ({ page }) => {
    console.log('=== FRONTEND LOGIN TEST ===');
    
    // Navigate to login page / Naviga alla pagina login
    await page.goto('http://localhost:5173/login');
    
    // Check if login form is present / Controlla se form login è presente
    const loginForm = page.locator('form');
    await expect(loginForm).toBeVisible();
    
    // Check form fields / Controlla campi form
    const emailField = page.locator('input[name="email"]');
    const passwordField = page.locator('input[name="password"]');
    const submitButton = page.locator('button[type="submit"]');
    
    await expect(emailField).toBeVisible();
    await expect(passwordField).toBeVisible();
    await expect(submitButton).toBeVisible();
    
    // Fill login form / Compila form login
    await emailField.fill('test.login.new@example.com');
    await passwordField.fill('password123');
    
    // Submit form / Invia form
    await submitButton.click();
    
    // Wait for response / Aspetta risposta
    await page.waitForTimeout(3000);
    
    // Check for error or success messages / Controlla messaggi errore o successo
    const errorMessage = page.locator('[class*="error"], [class*="alert"], [role="alert"]');
    const successMessage = page.locator('[class*="success"], [class*="message"]');
    
    if (await errorMessage.count() > 0) {
      console.log('Error message found:', await errorMessage.textContent());
    }
    
    if (await successMessage.count() > 0) {
      console.log('Success message found:', await successMessage.textContent());
    }
    
    // Check if redirected / Controlla se reindirizzato
    const currentUrl = page.url();
    console.log('Current URL after login attempt:', currentUrl);
    
    console.log('=== FRONTEND LOGIN TEST COMPLETE ===');
  });
  
  test('should test API endpoints accessibility', async ({ page, request }) => {
    console.log('=== API ENDPOINTS ACCESSIBILITY TEST ===');
    
    const endpoints = [
      'http://localhost:8080/actuator/health',
      'http://localhost:8080/api/v1/auth/register',
      'http://localhost:8080/api/v1/auth/login',
      'http://localhost:8080/courses',
      'http://localhost:8080/enrollments',
      'http://localhost:8080/swagger-ui.html',
      'http://localhost:8080/v3/api-docs'
    ];
    
    for (const endpoint of endpoints) {
      try {
        const response = await request.get(endpoint);
        console.log(`${endpoint}: ${response.status()}`);
      } catch (error) {
        console.log(`${endpoint}: ERROR - ${error.message}`);
      }
    }
    
    console.log('=== API ENDPOINTS TEST COMPLETE ===');
  });
});
