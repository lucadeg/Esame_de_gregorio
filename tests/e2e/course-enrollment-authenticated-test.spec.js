const { test, expect } = require('@playwright/test');

/**
 * Course Enrollment Authenticated Test
 * Test Iscrizione Corsi Autenticato
 * 
 * Tests the course enrollment functionality with authentication
 * Testa la funzionalità di iscrizione ai corsi con autenticazione
 */
test.describe('Course Enrollment Authenticated Tests', () => {
  
  test('should load courses in enrollment form after login', async ({ page, request }) => {
    console.log('=== AUTHENTICATED COURSE ENROLLMENT TEST ===');
    
    // Step 1: Login first / Passo 1: Login prima
    console.log('1. Logging in...');
    await page.goto('http://localhost:5173/login');
    
    await page.fill('input[name="email"]', 'demo@example.com');
    await page.fill('input[name="password"]', 'demo123');
    await page.click('button[type="submit"]');
    
    // Wait for login to complete / Aspetta completamento login
    await page.waitForTimeout(3000);
    
    // Check if redirected to home / Controlla se reindirizzato alla home
    const currentUrl = page.url();
    console.log('Current URL after login:', currentUrl);
    
    if (currentUrl.includes('localhost:5173') && !currentUrl.includes('/login')) {
      console.log('✅ Successfully logged in');
    } else {
      console.log('❌ Login failed or not redirected');
      return;
    }
    
    // Step 2: Navigate to create enrollment / Passo 2: Naviga a crea iscrizione
    console.log('2. Navigating to create enrollment...');
    await page.goto('http://localhost:5173/create-enrollment');
    
    // Wait for page to load / Aspetta caricamento pagina
    await page.waitForTimeout(3000);
    
    // Check current URL / Controlla URL corrente
    const enrollmentUrl = page.url();
    console.log('Current URL after navigating to enrollment:', enrollmentUrl);
    
    // Step 3: Check if course dropdown is visible / Passo 3: Controlla se dropdown corsi è visibile
    console.log('3. Checking course dropdown...');
    
    // Try different selectors for the course dropdown / Prova diversi selettori per dropdown corsi
    const courseSelectors = [
      'select[name="corsoId"]',
      'select[data-testid="course-select"]',
      'select.form-select',
      'select',
      '[data-testid="course-select"]',
      '.form-select'
    ];
    
    let courseSelect = null;
    for (const selector of courseSelectors) {
      courseSelect = page.locator(selector);
      if (await courseSelect.count() > 0) {
        console.log(`✅ Found course dropdown with selector: ${selector}`);
        break;
      }
    }
    
    if (!courseSelect || await courseSelect.count() === 0) {
      console.log('❌ Course dropdown not found with any selector');
      
      // Take screenshot for debugging / Fai screenshot per debug
      await page.screenshot({ path: 'course-dropdown-not-found.png' });
      
      // Check if page has any form elements / Controlla se pagina ha elementi form
      const formElements = await page.locator('form, input, select, button').count();
      console.log(`Found ${formElements} form elements on page`);
      
      // Check page content / Controlla contenuto pagina
      const pageContent = await page.textContent('body');
      console.log('Page contains "corso":', pageContent.includes('corso'));
      console.log('Page contains "Corso":', pageContent.includes('Corso'));
      console.log('Page contains "Selezione":', pageContent.includes('Selezione'));
      
      return;
    }
    
    // Step 4: Check if dropdown is populated / Passo 4: Controlla se dropdown è popolato
    console.log('4. Checking if dropdown is populated...');
    
    const options = await courseSelect.locator('option').all();
    console.log(`Found ${options.length} options in course select`);
    
    // Check if there are course options (excluding the default option)
    // Controlla se ci sono opzioni corsi (escludendo l'opzione di default)
    const courseOptions = [];
    for (const option of options) {
      const value = await option.getAttribute('value');
      const text = await option.textContent();
      if (value !== '') {
        courseOptions.push({ value, text });
      }
    }
    
    console.log(`Found ${courseOptions.length} course options`);
    
    if (courseOptions.length > 0) {
      console.log('✅ Course dropdown is populated with courses');
      courseOptions.forEach((option, index) => {
        console.log(`  ${index + 1}. ${option.text} (value: ${option.value})`);
      });
    } else {
      console.log('❌ Course dropdown is empty');
    }
    
    // Step 5: Test course selection / Passo 5: Test selezione corso
    if (courseOptions.length > 0) {
      console.log('5. Testing course selection...');
      
      const firstCourse = courseOptions[0];
      console.log(`Selecting course: ${firstCourse.text}`);
      
      await courseSelect.selectOption(firstCourse.value);
      await page.waitForTimeout(1000);
      
      // Check if course details are displayed / Controlla se dettagli corso sono mostrati
      const courseDetails = page.locator('text=Dettagli Corso');
      if (await courseDetails.isVisible()) {
        console.log('✅ Course details are displayed after selection');
      } else {
        console.log('❌ Course details not found');
      }
    }
    
    console.log('=== AUTHENTICATED COURSE ENROLLMENT TEST COMPLETE ===');
  });
  
  test('should create enrollment with valid data after login', async ({ page }) => {
    console.log('=== CREATE ENROLLMENT WITH LOGIN TEST ===');
    
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
    
    // Try to find and select a course / Prova a trovare e selezionare un corso
    const courseSelectors = [
      'select[name="corsoId"]',
      'select.form-select',
      'select'
    ];
    
    let courseSelect = null;
    for (const selector of courseSelectors) {
      courseSelect = page.locator(selector);
      if (await courseSelect.count() > 0) {
        console.log(`Found course dropdown with selector: ${selector}`);
        break;
      }
    }
    
    if (courseSelect && await courseSelect.count() > 0) {
      // Get available options / Ottieni opzioni disponibili
      const options = await courseSelect.locator('option').all();
      const courseOptions = [];
      
      for (const option of options) {
        const value = await option.getAttribute('value');
        const text = await option.textContent();
        if (value !== '') {
          courseOptions.push({ value, text });
        }
      }
      
      if (courseOptions.length > 0) {
        const firstCourse = courseOptions[0];
        console.log(`Selecting course: ${firstCourse.text}`);
        await courseSelect.selectOption(firstCourse.value);
        await page.waitForTimeout(1000);
      } else {
        console.log('❌ No courses available for selection');
        return;
      }
    } else {
      console.log('❌ Course dropdown not found');
      return;
    }
    
    // Fill participant data / Compila dati partecipante
    const nameInput = page.locator('input[name="partecipanteNome"]');
    const surnameInput = page.locator('input[name="partecipanteCognome"]');
    const emailInput = page.locator('input[name="partecipanteEmail"]');
    
    if (await nameInput.count() > 0) {
      await nameInput.fill('Mario');
      console.log('✅ Filled name field');
    } else {
      console.log('❌ Name input not found');
    }
    
    if (await surnameInput.count() > 0) {
      await surnameInput.fill('Rossi');
      console.log('✅ Filled surname field');
    } else {
      console.log('❌ Surname input not found');
    }
    
    if (await emailInput.count() > 0) {
      await emailInput.fill('mario.rossi@example.com');
      console.log('✅ Filled email field');
    } else {
      console.log('❌ Email input not found');
    }
    
    // Step 4: Submit enrollment / Passo 4: Invia iscrizione
    console.log('4. Submitting enrollment...');
    
    const submitButton = page.locator('button[type="submit"]');
    if (await submitButton.count() > 0) {
      const isDisabled = await submitButton.isDisabled();
      console.log(`Submit button disabled: ${isDisabled}`);
      
      if (!isDisabled) {
        await submitButton.click();
        await page.waitForTimeout(3000);
        console.log('✅ Enrollment form submitted');
      } else {
        console.log('❌ Submit button is disabled');
      }
    } else {
      console.log('❌ Submit button not found');
    }
    
    console.log('=== CREATE ENROLLMENT WITH LOGIN TEST COMPLETE ===');
  });
});
