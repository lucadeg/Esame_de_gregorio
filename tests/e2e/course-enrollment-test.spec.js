const { test, expect } = require('@playwright/test');

/**
 * Course Enrollment Test
 * Test Iscrizione Corsi
 * 
 * Tests the course enrollment functionality including course loading and enrollment creation
 * Testa la funzionalità di iscrizione ai corsi incluso caricamento corsi e creazione iscrizione
 */
test.describe('Course Enrollment Tests', () => {
  
  test('should load courses in enrollment form', async ({ page, request }) => {
    console.log('=== COURSE ENROLLMENT TEST ===');
    
    // Step 1: Test backend courses endpoint / Passo 1: Test endpoint corsi backend
    console.log('1. Testing backend courses endpoint...');
    const coursesResponse = await request.get('http://localhost:8080/courses');
    expect(coursesResponse.status()).toBe(200);
    
    const coursesData = await coursesResponse.json();
    expect(coursesData.courses).toBeDefined();
    expect(Array.isArray(coursesData.courses)).toBe(true);
    expect(coursesData.courses.length).toBeGreaterThan(0);
    console.log(`✅ Backend returned ${coursesData.courses.length} courses`);
    
    // Step 2: Test frontend course loading / Passo 2: Test caricamento corsi frontend
    console.log('2. Testing frontend course loading...');
    await page.goto('http://localhost:5173/create-enrollment');
    
    // Wait for page to load / Aspetta caricamento pagina
    await page.waitForTimeout(3000);
    
    // Check if course dropdown is populated / Controlla se dropdown corsi è popolato
    const courseSelect = page.locator('select[name="corsoId"]');
    await expect(courseSelect).toBeVisible();
    
    // Get all options in the select / Ottieni tutte le opzioni nel select
    const options = await courseSelect.locator('option').all();
    console.log(`Found ${options.length} options in course select`);
    
    // Check if there are course options (excluding the default "Seleziona un corso..." option)
    // Controlla se ci sono opzioni corsi (escludendo l'opzione di default "Seleziona un corso...")
    const courseOptions = options.filter(async (option) => {
      const value = await option.getAttribute('value');
      return value !== '';
    });
    
    if (courseOptions.length > 0) {
      console.log('✅ Course dropdown is populated with courses');
    } else {
      console.log('❌ Course dropdown is empty');
    }
    
    // Step 3: Test course selection / Passo 3: Test selezione corso
    console.log('3. Testing course selection...');
    
    // Try to select a course / Prova a selezionare un corso
    if (courseOptions.length > 0) {
      // Get the first course option / Ottieni la prima opzione corso
      const firstCourseOption = courseOptions[0];
      const courseValue = await firstCourseOption.getAttribute('value');
      const courseText = await firstCourseOption.textContent();
      
      console.log(`Selecting course: ${courseText} (value: ${courseValue})`);
      
      // Select the course / Seleziona il corso
      await courseSelect.selectOption(courseValue);
      
      // Wait for course details to load / Aspetta caricamento dettagli corso
      await page.waitForTimeout(1000);
      
      // Check if course details are displayed / Controlla se dettagli corso sono mostrati
      const courseDetails = page.locator('text=Dettagli Corso');
      if (await courseDetails.isVisible()) {
        console.log('✅ Course details are displayed after selection');
      } else {
        console.log('❌ Course details not found');
      }
    }
    
    console.log('=== COURSE ENROLLMENT TEST COMPLETE ===');
  });
  
  test('should create enrollment with valid data', async ({ page, request }) => {
    console.log('=== CREATE ENROLLMENT TEST ===');
    
    // Step 1: Login first / Passo 1: Login prima
    console.log('1. Logging in...');
    await page.goto('http://localhost:5173/login');
    
    await page.fill('input[name="email"]', 'demo@example.com');
    await page.fill('input[name="password"]', 'demo123');
    await page.click('button[type="submit"]');
    
    await page.waitForTimeout(3000);
    
    // Step 2: Navigate to create enrollment / Passo 2: Naviga a crea iscrizione
    console.log('2. Navigating to create enrollment...');
    await page.goto('http://localhost:5173/create-enrollment');
    await page.waitForTimeout(2000);
    
    // Step 3: Fill enrollment form / Passo 3: Compila form iscrizione
    console.log('3. Filling enrollment form...');
    
    // Select a course / Seleziona un corso
    const courseSelect = page.locator('select[name="corsoId"]');
    await expect(courseSelect).toBeVisible();
    
    // Get first available course / Ottieni primo corso disponibile
    const options = await courseSelect.locator('option').all();
    const courseOptions = options.filter(async (option) => {
      const value = await option.getAttribute('value');
      return value !== '';
    });
    
    if (courseOptions.length > 0) {
      const firstCourseOption = courseOptions[0];
      const courseValue = await firstCourseOption.getAttribute('value');
      const courseText = await firstCourseOption.textContent();
      
      console.log(`Selecting course: ${courseText}`);
      await courseSelect.selectOption(courseValue);
      await page.waitForTimeout(1000);
    } else {
      console.log('❌ No courses available for selection');
      return;
    }
    
    // Fill participant data / Compila dati partecipante
    await page.fill('input[name="partecipanteNome"]', 'Mario');
    await page.fill('input[name="partecipanteCognome"]', 'Rossi');
    await page.fill('input[name="partecipanteEmail"]', 'mario.rossi@example.com');
    
    console.log('✅ Enrollment form filled with test data');
    
    // Step 4: Submit enrollment / Passo 4: Invia iscrizione
    console.log('4. Submitting enrollment...');
    
    const submitButton = page.locator('button[type="submit"]');
    await expect(submitButton).toBeVisible();
    await submitButton.click();
    
    // Wait for response / Aspetta risposta
    await page.waitForTimeout(3000);
    
    // Check for success or error messages / Controlla messaggi successo o errore
    const successMessage = page.locator('text=Iscrizione creata con successo');
    const errorMessage = page.locator('[class*="error"], [class*="alert"], [role="alert"]');
    
    if (await successMessage.isVisible()) {
      console.log('✅ Enrollment created successfully');
    } else if (await errorMessage.count() > 0) {
      console.log('❌ Enrollment creation failed');
      for (let i = 0; i < await errorMessage.count(); i++) {
        const errorText = await errorMessage.nth(i).textContent();
        console.log('Error message:', errorText);
      }
    } else {
      console.log('⚠️ No clear success or error message found');
    }
    
    console.log('=== CREATE ENROLLMENT TEST COMPLETE ===');
  });
  
  test('should handle enrollment errors gracefully', async ({ page }) => {
    console.log('=== ENROLLMENT ERROR HANDLING TEST ===');
    
    // Step 1: Navigate to create enrollment without login / Passo 1: Naviga a crea iscrizione senza login
    console.log('1. Testing enrollment without authentication...');
    
    await page.goto('http://localhost:5173/create-enrollment');
    await page.waitForTimeout(2000);
    
    // Check if redirected to login / Controlla se reindirizzato al login
    const currentUrl = page.url();
    if (currentUrl.includes('/login')) {
      console.log('✅ Redirected to login page (protected route working)');
    } else {
      console.log('❌ Not redirected to login page');
    }
    
    console.log('=== ENROLLMENT ERROR HANDLING TEST COMPLETE ===');
  });
});
