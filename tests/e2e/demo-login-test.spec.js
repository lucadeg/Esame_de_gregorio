const { test, expect } = require('@playwright/test');

/**
 * Demo Login Test
 * Test Login Demo
 * 
 * Tests the demo login functionality with correct credentials
 * Testa la funzionalità di login demo con credenziali corrette
 */
test.describe('Demo Login Tests', () => {
  
  test('should login with demo credentials', async ({ page, request }) => {
    console.log('=== DEMO LOGIN TEST ===');
    
    // Step 1: Test backend login / Passo 1: Test login backend
    console.log('1. Testing backend login...');
    const loginResponse = await request.post('http://localhost:8080/api/v1/auth/login', {
      data: {
        email: 'demo@example.com',
        password: 'demo123'
      }
    });
    
    expect(loginResponse.status()).toBe(200);
    const loginData = await loginResponse.json();
    expect(loginData.success).toBe(true);
    expect(loginData.data.accessToken).toBeDefined();
    expect(loginData.data.refreshToken).toBeDefined();
    console.log('✅ Backend login successful');
    console.log('Token received:', loginData.data.accessToken.substring(0, 50) + '...');
    
    // Step 2: Test frontend login / Passo 2: Test login frontend
    console.log('2. Testing frontend login...');
    await page.goto('http://localhost:5173/login');
    
    // Check if demo credentials are displayed / Controlla se le credenziali demo sono mostrate
    const demoCredentials = page.locator('text=Demo User: demo@example.com / demo123');
    await expect(demoCredentials).toBeVisible();
    console.log('✅ Demo credentials displayed');
    
    // Fill login form with demo credentials / Compila form login con credenziali demo
    await page.fill('input[name="email"]', 'demo@example.com');
    await page.fill('input[name="password"]', 'demo123');
    
    // Submit form / Invia form
    await page.click('button[type="submit"]');
    
    // Wait for response / Aspetta risposta
    await page.waitForTimeout(3000);
    
    // Check for success / Controlla successo
    const currentUrl = page.url();
    console.log('Current URL after login:', currentUrl);
    
    // Check for error messages / Controlla messaggi errore
    const errorMessages = page.locator('[class*="error"], [class*="alert"], [role="alert"]');
    const errorCount = await errorMessages.count();
    
    if (errorCount > 0) {
      console.log('❌ Frontend login error found');
      for (let i = 0; i < errorCount; i++) {
        const errorText = await errorMessages.nth(i).textContent();
        console.log('Error message:', errorText);
      }
    } else {
      console.log('✅ Frontend login successful');
    }
    
    // Check if user is redirected to home page / Controlla se utente è reindirizzato alla homepage
    if (currentUrl.includes('localhost:5173') && !currentUrl.includes('/login')) {
      console.log('✅ User redirected to home page');
    } else {
      console.log('❌ User not redirected properly');
    }
    
    console.log('=== DEMO LOGIN TEST COMPLETE ===');
  });
  
  test('should test with test user credentials', async ({ page, request }) => {
    console.log('=== TEST USER LOGIN TEST ===');
    
    // Step 1: Test backend login with test user / Passo 1: Test login backend con utente test
    console.log('1. Testing backend login with test user...');
    const loginResponse = await request.post('http://localhost:8080/api/v1/auth/login', {
      data: {
        email: 'test.login.final3@example.com',
        password: 'password123'
      }
    });
    
    expect(loginResponse.status()).toBe(200);
    const loginData = await loginResponse.json();
    expect(loginData.success).toBe(true);
    expect(loginData.data.accessToken).toBeDefined();
    console.log('✅ Test user backend login successful');
    
    // Step 2: Test frontend login with test user / Passo 2: Test login frontend con utente test
    console.log('2. Testing frontend login with test user...');
    await page.goto('http://localhost:5173/login');
    
    // Fill login form with test user credentials / Compila form login con credenziali utente test
    await page.fill('input[name="email"]', 'test.login.final3@example.com');
    await page.fill('input[name="password"]', 'password123');
    
    // Submit form / Invia form
    await page.click('button[type="submit"]');
    
    // Wait for response / Aspetta risposta
    await page.waitForTimeout(3000);
    
    // Check for success / Controlla successo
    const currentUrl = page.url();
    console.log('Current URL after test user login:', currentUrl);
    
    // Check for error messages / Controlla messaggi errore
    const errorMessages = page.locator('[class*="error"], [class*="alert"], [role="alert"]');
    const errorCount = await errorMessages.count();
    
    if (errorCount > 0) {
      console.log('❌ Test user frontend login error found');
      for (let i = 0; i < errorCount; i++) {
        const errorText = await errorMessages.nth(i).textContent();
        console.log('Error message:', errorText);
      }
    } else {
      console.log('✅ Test user frontend login successful');
    }
    
    console.log('=== TEST USER LOGIN TEST COMPLETE ===');
  });
  
  test('should handle wrong credentials', async ({ page, request }) => {
    console.log('=== WRONG CREDENTIALS TEST ===');
    
    // Step 1: Test backend with wrong credentials / Passo 1: Test backend con credenziali sbagliate
    console.log('1. Testing backend with wrong credentials...');
    const loginResponse = await request.post('http://localhost:8080/api/v1/auth/login', {
      data: {
        email: 'demo@example.com',
        password: 'wrongpassword'
      }
    });
    
    expect(loginResponse.status()).toBe(401);
    console.log('✅ Backend correctly rejected wrong credentials');
    
    // Step 2: Test frontend with wrong credentials / Passo 2: Test frontend con credenziali sbagliate
    console.log('2. Testing frontend with wrong credentials...');
    await page.goto('http://localhost:5173/login');
    
    // Fill login form with wrong credentials / Compila form login con credenziali sbagliate
    await page.fill('input[name="email"]', 'demo@example.com');
    await page.fill('input[name="password"]', 'wrongpassword');
    
    // Submit form / Invia form
    await page.click('button[type="submit"]');
    
    // Wait for response / Aspetta risposta
    await page.waitForTimeout(3000);
    
    // Check for error messages / Controlla messaggi errore
    const errorMessages = page.locator('[class*="error"], [class*="alert"], [role="alert"]');
    const errorCount = await errorMessages.count();
    
    if (errorCount > 0) {
      console.log('✅ Frontend correctly showed error for wrong credentials');
      for (let i = 0; i < errorCount; i++) {
        const errorText = await errorMessages.nth(i).textContent();
        console.log('Error message:', errorText);
      }
    } else {
      console.log('❌ Frontend should have shown error for wrong credentials');
    }
    
    console.log('=== WRONG CREDENTIALS TEST COMPLETE ===');
  });
});
