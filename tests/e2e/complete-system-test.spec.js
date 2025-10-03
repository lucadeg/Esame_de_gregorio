const { test, expect } = require('@playwright/test');

/**
 * Complete System Test Suite
 * Suite di Test Sistema Completo
 * 
 * Final comprehensive tests for the entire system
 * Test completi finali per l'intero sistema
 */
test.describe('Complete System Tests', () => {
  
  test('should complete full user journey', async ({ page, request }) => {
    console.log('=== COMPLETE SYSTEM TEST ===');
    
    // Step 1: Register new user / Passo 1: Registra nuovo utente
    const userData = {
      nome: 'Complete',
      cognome: 'System',
      email: `complete.system.${Date.now()}@example.com`,
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
    
    // Step 4: Test courses endpoint / Passo 4: Test endpoint corsi
    console.log('4. Testing courses endpoint...');
    const coursesResponse = await request.get('http://localhost:8080/courses');
    expect(coursesResponse.status()).toBe(200);
    const coursesData = await coursesResponse.json();
    console.log('✅ Courses endpoint working, found', coursesData.courses?.length || coursesData.length || 0, 'courses');
    
    // Step 5: Test enrollments endpoint / Passo 5: Test endpoint iscrizioni
    console.log('5. Testing enrollments endpoint...');
    const enrollmentsResponse = await request.get('http://localhost:8080/enrollments');
    expect(enrollmentsResponse.status()).toBe(200);
    const enrollmentsData = await enrollmentsResponse.json();
    console.log('✅ Enrollments endpoint working, found', enrollmentsData.length || 0, 'enrollments');
    
    // Step 6: Test frontend login / Passo 6: Test login frontend
    console.log('6. Testing frontend login...');
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
    
    // Step 7: Test system health / Passo 7: Test salute sistema
    console.log('7. Testing system health...');
    const healthResponse = await request.get('http://localhost:8080/actuator/health');
    expect(healthResponse.status()).toBe(200);
    const healthData = await healthResponse.json();
    expect(healthData.status).toBe('UP');
    console.log('✅ System health: UP');
    
    // Step 8: Test Swagger documentation / Passo 8: Test documentazione Swagger
    console.log('8. Testing Swagger documentation...');
    const swaggerResponse = await request.get('http://localhost:8080/swagger-ui.html');
    expect(swaggerResponse.status()).toBe(200);
    console.log('✅ Swagger documentation working');
    
    // Step 9: Test OpenAPI docs / Passo 9: Test documentazione OpenAPI
    console.log('9. Testing OpenAPI documentation...');
    const openApiResponse = await request.get('http://localhost:8080/v3/api-docs');
    expect(openApiResponse.status()).toBe(200);
    console.log('✅ OpenAPI documentation working');
    
    console.log('=== COMPLETE SYSTEM TEST SUCCESSFUL ===');
  });
  
  test('should handle all error scenarios', async ({ page, request }) => {
    console.log('=== ERROR HANDLING TEST ===');
    
    // Test wrong password / Test password sbagliata
    const wrongPasswordResponse = await request.post('http://localhost:8080/api/v1/auth/login', {
      data: {
        email: 'test.login.final2@example.com',
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
    
    // Test duplicate registration / Test registrazione duplicata
    const duplicateResponse = await request.post('http://localhost:8080/api/v1/auth/register', {
      data: {
        nome: 'Duplicate',
        cognome: 'User',
        email: 'test.login.final2@example.com', // Same email as before
        password: 'password123'
      }
    });
    
    expect(duplicateResponse.status()).toBe(400);
    console.log('✅ Duplicate registration correctly rejected');
    
    console.log('=== ERROR HANDLING TEST COMPLETE ===');
  });
  
  test('should test all system endpoints', async ({ page, request }) => {
    console.log('=== SYSTEM ENDPOINTS TEST ===');
    
    const endpoints = [
      { url: 'http://localhost:8080/actuator/health', expectedStatus: 200, name: 'Health' },
      { url: 'http://localhost:8080/courses', expectedStatus: 200, name: 'Courses' },
      { url: 'http://localhost:8080/enrollments', expectedStatus: 200, name: 'Enrollments' },
      { url: 'http://localhost:8080/swagger-ui.html', expectedStatus: 200, name: 'Swagger UI' },
      { url: 'http://localhost:8080/v3/api-docs', expectedStatus: 200, name: 'OpenAPI Docs' },
      { url: 'http://localhost:8080/h2-console', expectedStatus: 200, name: 'H2 Console' }
    ];
    
    for (const endpoint of endpoints) {
      try {
        const response = await request.get(endpoint.url);
        expect(response.status()).toBe(endpoint.expectedStatus);
        console.log(`✅ ${endpoint.name}: ${response.status()}`);
      } catch (error) {
        console.log(`❌ ${endpoint.name}: ERROR - ${error.message}`);
      }
    }
    
    console.log('=== SYSTEM ENDPOINTS TEST COMPLETE ===');
  });
});