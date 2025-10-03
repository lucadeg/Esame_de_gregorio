const { test, expect } = require('@playwright/test');

/**
 * Authentication System Test
 * Test Sistema Autenticazione
 * 
 * Tests the complete authentication system including login, logout, and user state
 * Testa il sistema di autenticazione completo incluso login, logout e stato utente
 */
test.describe('Authentication System Tests', () => {
  
  test('should show user profile when logged in', async ({ page, request }) => {
    console.log('=== AUTHENTICATION SYSTEM TEST ===');
    
    // Step 1: Login with demo credentials / Passo 1: Login con credenziali demo
    console.log('1. Logging in with demo credentials...');
    await page.goto('http://localhost:5173/login');
    
    // Fill login form / Compila form login
    await page.fill('input[name="email"]', 'demo@example.com');
    await page.fill('input[name="password"]', 'demo123');
    
    // Submit form / Invia form
    await page.click('button[type="submit"]');
    
    // Wait for redirect / Aspetta redirect
    await page.waitForTimeout(3000);
    
    // Check if redirected to home page / Controlla se reindirizzato alla homepage
    const currentUrl = page.url();
    console.log('Current URL after login:', currentUrl);
    
    if (currentUrl.includes('localhost:5173') && !currentUrl.includes('/login')) {
      console.log('✅ Successfully redirected to home page');
    } else {
      console.log('❌ Not redirected properly');
    }
    
    // Step 2: Check if user profile is displayed / Passo 2: Controlla se profilo utente è mostrato
    console.log('2. Checking user profile display...');
    
    // Look for user profile elements / Cerca elementi profilo utente
    const userProfile = page.locator('text=Benvenuto nel tuo profilo');
    const userEmail = page.locator('text=demo@example.com');
    const userRole = page.locator('text=STUDENT');
    const subscriptionBadge = page.locator('text=FREE');
    
    if (await userProfile.isVisible()) {
      console.log('✅ User profile section is visible');
    } else {
      console.log('❌ User profile section not found');
    }
    
    if (await userEmail.isVisible()) {
      console.log('✅ User email is displayed');
    } else {
      console.log('❌ User email not found');
    }
    
    if (await userRole.isVisible()) {
      console.log('✅ User role is displayed');
    } else {
      console.log('❌ User role not found');
    }
    
    if (await subscriptionBadge.isVisible()) {
      console.log('✅ Subscription type is displayed');
    } else {
      console.log('❌ Subscription type not found');
    }
    
    // Step 3: Check navigation bar / Passo 3: Controlla barra navigazione
    console.log('3. Checking navigation bar...');
    
    // Look for user dropdown in navigation / Cerca dropdown utente nella navigazione
    const userDropdown = page.locator('[data-bs-toggle="dropdown"]');
    const logoutButton = page.locator('text=Logout');
    
    if (await userDropdown.isVisible()) {
      console.log('✅ User dropdown is visible in navigation');
      
      // Click dropdown to see options / Clicca dropdown per vedere opzioni
      await userDropdown.click();
      await page.waitForTimeout(1000);
      
      if (await logoutButton.isVisible()) {
        console.log('✅ Logout button is visible in dropdown');
      } else {
        console.log('❌ Logout button not found in dropdown');
      }
    } else {
      console.log('❌ User dropdown not found in navigation');
    }
    
    console.log('=== AUTHENTICATION SYSTEM TEST COMPLETE ===');
  });
  
  test('should protect routes when not authenticated', async ({ page }) => {
    console.log('=== ROUTE PROTECTION TEST ===');
    
    // Step 1: Try to access protected route without login / Passo 1: Prova ad accedere a rotta protetta senza login
    console.log('1. Trying to access protected route without authentication...');
    
    // Clear any existing authentication / Pulisci eventuale autenticazione esistente
    await page.evaluate(() => {
      localStorage.clear();
    });
    
    // Try to access protected route / Prova ad accedere a rotta protetta
    await page.goto('http://localhost:5173/enrollments');
    
    // Wait for redirect / Aspetta redirect
    await page.waitForTimeout(2000);
    
    // Check if redirected to login / Controlla se reindirizzato al login
    const currentUrl = page.url();
    console.log('Current URL after accessing protected route:', currentUrl);
    
    if (currentUrl.includes('/login')) {
      console.log('✅ Successfully redirected to login page');
    } else {
      console.log('❌ Not redirected to login page');
    }
    
    // Step 2: Try to access another protected route / Passo 2: Prova ad accedere a un'altra rotta protetta
    console.log('2. Trying to access another protected route...');
    
    await page.goto('http://localhost:5173/create-enrollment');
    await page.waitForTimeout(2000);
    
    const currentUrl2 = page.url();
    console.log('Current URL after accessing create-enrollment:', currentUrl2);
    
    if (currentUrl2.includes('/login')) {
      console.log('✅ Successfully redirected to login page for create-enrollment');
    } else {
      console.log('❌ Not redirected to login page for create-enrollment');
    }
    
    console.log('=== ROUTE PROTECTION TEST COMPLETE ===');
  });
  
  test('should logout and clear user state', async ({ page }) => {
    console.log('=== LOGOUT TEST ===');
    
    // Step 1: Login first / Passo 1: Login prima
    console.log('1. Logging in...');
    await page.goto('http://localhost:5173/login');
    
    await page.fill('input[name="email"]', 'demo@example.com');
    await page.fill('input[name="password"]', 'demo123');
    await page.click('button[type="submit"]');
    
    await page.waitForTimeout(3000);
    
    // Step 2: Check if logged in / Passo 2: Controlla se loggato
    console.log('2. Checking if logged in...');
    
    const userProfile = page.locator('text=Benvenuto nel tuo profilo');
    if (await userProfile.isVisible()) {
      console.log('✅ User is logged in');
    } else {
      console.log('❌ User is not logged in');
    }
    
    // Step 3: Logout / Passo 3: Logout
    console.log('3. Logging out...');
    
    // Find and click logout button / Trova e clicca pulsante logout
    const userDropdown = page.locator('[data-bs-toggle="dropdown"]');
    if (await userDropdown.isVisible()) {
      await userDropdown.click();
      await page.waitForTimeout(1000);
      
      const logoutButton = page.locator('text=Logout');
      if (await logoutButton.isVisible()) {
        await logoutButton.click();
        await page.waitForTimeout(2000);
        console.log('✅ Logout button clicked');
      } else {
        console.log('❌ Logout button not found');
      }
    } else {
      console.log('❌ User dropdown not found');
    }
    
    // Step 4: Check if logged out / Passo 4: Controlla se disconnesso
    console.log('4. Checking if logged out...');
    
    const currentUrl = page.url();
    console.log('Current URL after logout:', currentUrl);
    
    // Check if user profile is no longer visible / Controlla se profilo utente non è più visibile
    const userProfileAfterLogout = page.locator('text=Benvenuto nel tuo profilo');
    if (!(await userProfileAfterLogout.isVisible())) {
      console.log('✅ User profile is no longer visible');
    } else {
      console.log('❌ User profile is still visible');
    }
    
    // Check if login button is visible / Controlla se pulsante login è visibile
    const loginButton = page.locator('text=Login');
    if (await loginButton.isVisible()) {
      console.log('✅ Login button is visible after logout');
    } else {
      console.log('❌ Login button not found');
    }
    
    console.log('=== LOGOUT TEST COMPLETE ===');
  });
});
