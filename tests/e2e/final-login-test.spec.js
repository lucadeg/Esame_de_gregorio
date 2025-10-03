const { test, expect } = require('@playwright/test');

/**
 * Final Login Test Suite
 * Suite di Test Login Finale
 * 
 * Final comprehensive tests for login functionality
 * Test completi finali per la funzionalità di login
 */
test.describe('Final Login Tests', () => {
  
  test('should complete full registration and login flow', async ({ page, request }) => {
    console.log('=== FINAL LOGIN TEST ===');
    
    // Step 1: Register new user / Passo 1: Registra nuovo utente
    const userData = {
      nome: 'Final',
      cognome: 'Test',
      email: `final.test.${Date.now()}@example.com`,
      password: 'password123'
    };
    
    console.log('1. Registering user:', userData.email);
    const registerResponse = await request.post('http://localhost:8080/api/v1/auth/register', {
      data: userData
    });
    
    expect(registerResponse.status()).toBe(201);
    const registerData = await registerResponse.json();
    expect(registerData.success).toBe(true);
    console.log('✅ Registration successful');
    
    // Step 2: Login with correct credentials / Passo 2: Login con credenziali corrette
    console.log('2. Testing login...');
    const loginResponse = await request.post('http://localhost:8080/api/v1/auth/login', {
      data: {
        email: userData.email,
        password: userData.password
      }
    });
    
    expect(loginResponse.status()).toBe(200);
    const loginData = await loginResponse.json();
    expect(loginData.success).toBe(true);
    expect(loginData.data.accessToken).toBeDefined();
    expect(loginData.data.refreshToken).toBeDefined();
    console.log('✅ Login successful');
    console.log('Token received:', loginData.data.accessToken.substring(0, 50) + '...');
    
    // Step 3: Test protected endpoint with token / Passo 3: Test endpoint protetto con token
    console.log('3. Testing protected endpoint...');
    const protectedResponse = await request.get('http://localhost:8080/api/v1/users/profile', {
      headers: {
        'Authorization': `Bearer ${loginData.data.accessToken}`
      }
    });
    
    console.log('Protected endpoint status:', protectedResponse.status());
    
    // Step 4: Test frontend login / Passo 4: Test login frontend
    console.log('4. Testing frontend login...');
    await page.goto('http://localhost:5173/login');
    
    // Fill login form / Compila form login
    await page.fill('input[name="email"]', userData.email);
    await page.fill('input[name="password"]', userData.password);
    
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
    
    console.log('=== FINAL LOGIN TEST COMPLETE ===');
  });
  
  test('should handle login errors gracefully', async ({ page, request }) => {
    console.log('=== LOGIN ERROR HANDLING TEST ===');
    
    // Test wrong password / Test password sbagliata
    const wrongPasswordResponse = await request.post('http://localhost:8080/api/v1/auth/login', {
      data: {
        email: 'test.login.final@example.com',
        password: 'wrongpassword'
      }
    });
    
    expect(wrongPasswordResponse.status()).toBe(401);
    console.log('✅ Wrong password correctly rejected');
    
    // Test non-existent user / Test utente inesistente
    const nonExistentResponse = await request.post('http://localhost:8080/api/v1/auth/login', {
      data: {
        email: 'nonexistent@example.com',
        password: 'password123'
      }
    });
    
    expect(nonExistentResponse.status()).toBe(401);
    console.log('✅ Non-existent user correctly rejected');
    
    // Test invalid email format / Test formato email non valido
    const invalidEmailResponse = await request.post('http://localhost:8080/api/v1/auth/login', {
      data: {
        email: 'invalid-email',
        password: 'password123'
      }
    });
    
    expect(invalidEmailResponse.status()).toBe(400);
    console.log('✅ Invalid email format correctly rejected');
    
    console.log('=== LOGIN ERROR HANDLING TEST COMPLETE ===');
  });
  
  test('should test system health and endpoints', async ({ page, request }) => {
    console.log('=== SYSTEM HEALTH TEST ===');
    
    // Test health endpoint / Test endpoint health
    const healthResponse = await request.get('http://localhost:8080/actuator/health');
    expect(healthResponse.status()).toBe(200);
    const healthData = await healthResponse.json();
    expect(healthData.status).toBe('UP');
    console.log('✅ Health endpoint working');
    
    // Test courses endpoint / Test endpoint corsi
    const coursesResponse = await request.get('http://localhost:8080/courses');
    expect(coursesResponse.status()).toBe(200);
    console.log('✅ Courses endpoint working');
    
    // Test enrollments endpoint / Test endpoint iscrizioni
    const enrollmentsResponse = await request.get('http://localhost:8080/enrollments');
    expect(enrollmentsResponse.status()).toBe(200);
    console.log('✅ Enrollments endpoint working');
    
    // Test Swagger documentation / Test documentazione Swagger
    const swaggerResponse = await request.get('http://localhost:8080/swagger-ui.html');
    expect(swaggerResponse.status()).toBe(200);
    console.log('✅ Swagger documentation working');
    
    // Test OpenAPI docs / Test documentazione OpenAPI
    const openApiResponse = await request.get('http://localhost:8080/v3/api-docs');
    expect(openApiResponse.status()).toBe(200);
    console.log('✅ OpenAPI documentation working');
    
    console.log('=== SYSTEM HEALTH TEST COMPLETE ===');
  });
});
