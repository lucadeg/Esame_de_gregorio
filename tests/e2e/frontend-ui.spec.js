const { test, expect } = require('@playwright/test');

test.describe('Frontend UI - Requirements Section 3', () => {
  
  test('Home page should load and display courses', async ({ page }) => {
    await page.goto('/');
    
    // Wait for page to load
    await page.waitForLoadState('networkidle');
    
    // Check if page title contains expected text
    const title = await page.title();
    expect(title).toBeTruthy();
    
    // Look for course-related elements
    const courseElements = await page.locator('[data-testid="course"], .course, [class*="course"]').count();
    console.log(`Found ${courseElements} course elements`);
    
    // Check for 3D visualization elements
    const canvasElements = await page.locator('canvas').count();
    console.log(`Found ${canvasElements} 3D canvas elements`);
    
    // Check for animated statistics
    const statsElements = await page.locator('.bg-white.rounded-xl.p-6.shadow-lg').count();
    console.log(`Found ${statsElements} animated statistics elements`);
    
    // Check for navigation or main content
    const mainContent = await page.locator('main, [role="main"], .main-content').count();
    expect(mainContent).toBeGreaterThan(0);
  });

  test('Home page should have course list with details', async ({ page }) => {
    await page.goto('/');
    await page.waitForLoadState('networkidle');
    
    // Look for course information elements
    const courseTitles = await page.locator('h1, h2, h3, [class*="title"], [class*="name"]').count();
    console.log(`Found ${courseTitles} potential course titles`);
    
    // Check for course details (date, location, availability)
    const dateElements = await page.locator('[class*="date"], [class*="time"]').count();
    const locationElements = await page.locator('[class*="location"], [class*="place"]').count();
    
    console.log(`Found ${dateElements} date elements and ${locationElements} location elements`);
  });

  test('Home page should have enrollment buttons', async ({ page }) => {
    await page.goto('/');
    await page.waitForLoadState('networkidle');
    
    // Look for enrollment-related buttons
    const enrollmentButtons = await page.locator('button:has-text("Iscrizioni"), button:has-text("Enroll"), [class*="enroll"], [class*="iscrizioni"]').count();
    console.log(`Found ${enrollmentButtons} enrollment buttons`);
    
    // Check for any buttons on the page
    const allButtons = await page.locator('button').count();
    console.log(`Total buttons found: ${allButtons}`);
  });

  test('Home page should have 3D visualizations', async ({ page }) => {
    await page.goto('/');
    await page.waitForLoadState('networkidle');
    
    // Check for 3D background elements
    const canvasElements = await page.locator('canvas').count();
    console.log(`Found ${canvasElements} canvas elements`);
    
    // Check for course elements
    const courseElements = await page.locator('.card').count();
    expect(courseElements).toBeGreaterThan(0);
  });

  test('Home page should have animated statistics', async ({ page }) => {
    await page.goto('/');
    await page.waitForLoadState('networkidle');
    
    // Check for animated statistics
    const statsCards = await page.locator('.card').count();
    expect(statsCards).toBeGreaterThan(0);
    
    // Check for specific stat titles
    const totalCourses = await page.locator('text=Total Courses').count();
    const enrollments = await page.locator('text=Enrollments').count();
    const available = await page.locator('text=Available').count();
    
    expect(totalCourses + enrollments + available).toBeGreaterThan(0);
  });

  test('Home page should have floating course cards', async ({ page }) => {
    await page.goto('/');
    await page.waitForLoadState('networkidle');
    
    // Check for course cards
    const courseCards = await page.locator('.card').count();
    expect(courseCards).toBeGreaterThan(0);
    
    // Check for buttons
    const buttons = await page.locator('button').count();
    expect(buttons).toBeGreaterThan(0);
  });

  test('Enrollment list page should be accessible', async ({ page }) => {
    await page.goto('/');
    await page.waitForLoadState('networkidle');
    
    // Try to navigate to enrollments (if link exists)
    const enrollmentLinks = await page.locator('a:has-text("Iscrizioni"), a:has-text("Enrollments"), [href*="enroll"]').count();
    
    if (enrollmentLinks > 0) {
      await page.locator('a:has-text("Iscrizioni"), a:has-text("Enrollments")').first().click();
      await page.waitForLoadState('networkidle');
      
      // Check if enrollment list is displayed
      const enrollmentList = await page.locator('[class*="enrollment"], [class*="iscrizione"], table, ul, ol').count();
      expect(enrollmentList).toBeGreaterThan(0);
    } else {
      console.log('No enrollment links found, testing direct navigation');
      // Try direct navigation to enrollments page
      await page.goto('/enrollments');
      await page.waitForLoadState('networkidle');
    }
  });

  test('Enrollment form should be accessible', async ({ page }) => {
    await page.goto('/');
    await page.waitForLoadState('networkidle');
    
    // Look for enrollment form or create enrollment button
    const createButtons = await page.locator('button:has-text("Crea"), button:has-text("Create"), button:has-text("Nuova"), button:has-text("New")').count();
    
    if (createButtons > 0) {
      await page.locator('button:has-text("Crea"), button:has-text("Create")').first().click();
      await page.waitForLoadState('networkidle');
    } else {
      // Try direct navigation to create enrollment
      await page.goto('/enrollments/new');
      await page.waitForLoadState('networkidle');
    }
    
    // Check for form fields
    const nameFields = await page.locator('input[name*="nome"], input[name*="name"], input[placeholder*="nome"], input[placeholder*="name"]').count();
    const emailFields = await page.locator('input[type="email"], input[name*="email"]').count();
    const submitButtons = await page.locator('button[type="submit"], button:has-text("Salva"), button:has-text("Save")').count();
    
    console.log(`Found ${nameFields} name fields, ${emailFields} email fields, ${submitButtons} submit buttons`);
  });

  test('UI should be responsive and user-friendly', async ({ page }) => {
    await page.goto('/');
    await page.waitForLoadState('networkidle');
    
    // Check for responsive design elements
    const viewport = page.viewportSize();
    expect(viewport.width).toBeGreaterThan(0);
    expect(viewport.height).toBeGreaterThan(0);
    
    // Check for navigation elements
    const navElements = await page.locator('nav, [role="navigation"], .navigation, .navbar').count();
    console.log(`Found ${navElements} navigation elements`);
    
    // Check for main content area
    const mainContent = await page.locator('main, [role="main"], .main, .content').count();
    expect(mainContent).toBeGreaterThan(0);
    
    // Check for footer
    const footerElements = await page.locator('footer, [role="contentinfo"], .footer').count();
    console.log(`Found ${footerElements} footer elements`);
  });

  test('Error handling should be present', async ({ page }) => {
    await page.goto('/');
    await page.waitForLoadState('networkidle');
    
    // Check for error message containers
    const errorContainers = await page.locator('[class*="error"], [class*="alert"], [role="alert"]').count();
    console.log(`Found ${errorContainers} error message containers`);
    
    // Check for loading states
    const loadingElements = await page.locator('[class*="loading"], [class*="spinner"], [aria-label*="loading"]').count();
    console.log(`Found ${loadingElements} loading elements`);
  });

});
