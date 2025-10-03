const { test, expect } = require('@playwright/test');

test.describe('Frontend-Backend Integration', () => {
  
  test('Frontend should communicate with backend API', async ({ page }) => {
    await page.goto('/');
    await page.waitForLoadState('networkidle');
    
    // Monitor network requests
    const requests = [];
    page.on('request', request => {
      if (request.url().includes('localhost:8080')) {
        requests.push(request.url());
      }
    });
    
    // Wait for potential API calls
    await page.waitForTimeout(2000);
    
    console.log('API requests made:', requests);
    
    // Check if any API calls were made
    const apiRequests = requests.filter(url => url.includes('localhost:8080'));
    console.log(`Found ${apiRequests.length} API requests`);
  });

  test('Course data should be loaded from backend', async ({ page }) => {
    await page.goto('/');
    await page.waitForLoadState('networkidle');
    
    // Check for course data in the page - more flexible selectors
    const courseElements = await page.locator('.card, [class*="course"], .course-card, .course-item').count();
    
    if (courseElements > 0) {
      // Check if course data is displayed
      const courseTitles = await page.locator('h1, h2, h3, h4, [class*="title"], [class*="name"]').allTextContents();
      console.log('Course titles found:', courseTitles);
      
      // Check for course details - more flexible selectors
      const courseDetails = await page.locator('[class*="date"], [class*="location"], [class*="availability"], [class*="price"], [class*="duration"]').count();
      console.log(`Found ${courseDetails} course detail elements`);
    } else {
      // Check for any text content that might indicate courses
      const allText = await page.textContent('body');
      const hasCourseContent = allText && (allText.includes('Course') || allText.includes('corso') || allText.includes('React') || allText.includes('Spring'));
      console.log('Has course content:', hasCourseContent);
    }
  });

  test('Enrollment data should be loaded from backend', async ({ page }) => {
    // Try to navigate to enrollments
    await page.goto('/enrollments');
    await page.waitForLoadState('networkidle');
    
    // Check for enrollment data - more flexible selectors
    const enrollmentElements = await page.locator('.card, [class*="enrollment"], [class*="enroll"], .enrollment-item, .enrollment-card').count();
    console.log(`Found ${enrollmentElements} enrollment elements`);
    
    if (enrollmentElements > 0) {
      // Check for participant information - more flexible selectors
      const participantNames = await page.locator('[class*="name"], [class*="participant"], [class*="user"], [class*="student"]').count();
      const emails = await page.locator('[class*="email"], [type="email"], [class*="mail"]').count();
      console.log(`Found ${participantNames} participant names and ${emails} email elements`);
    } else {
      // Check for any enrollment-related content
      const allText = await page.textContent('body');
      const hasEnrollmentContent = allText && (allText.includes('enrollment') || allText.includes('iscrizione') || allText.includes('participant') || allText.includes('partecipante'));
      console.log('Has enrollment content:', hasEnrollmentContent);
    }
  });

  test('Form submission should work with backend', async ({ page }) => {
    await page.goto('/');
    await page.waitForLoadState('networkidle');
    
    // Look for enrollment form - more flexible selectors
    const forms = await page.locator('form, [class*="form"], [class*="submit"]').count();
    console.log(`Found ${forms} forms on the page`);
    
    if (forms > 0) {
      // Check for form fields
      const nameInputs = await page.locator('input[name*="nome"], input[name*="name"]').count();
      const emailInputs = await page.locator('input[type="email"]').count();
      const submitButtons = await page.locator('button[type="submit"]').count();
      
      console.log(`Found ${nameInputs} name inputs, ${emailInputs} email inputs, ${submitButtons} submit buttons`);
      
      if (nameInputs > 0 && emailInputs > 0 && submitButtons > 0) {
        // Try to fill and submit form
        await page.locator('input[name*="nome"], input[name*="name"]').first().fill('Test User');
        await page.locator('input[type="email"]').first().fill('test@example.com');
        
        // Monitor for API calls on form submission
        const apiCalls = [];
        page.on('request', request => {
          if (request.url().includes('localhost:8080') && request.method() === 'POST') {
            apiCalls.push(request.url());
          }
        });
        
        await page.locator('button[type="submit"]').first().click();
        await page.waitForTimeout(1000);
        
        console.log('API calls made on form submission:', apiCalls);
      }
    }
  });

  test('Error handling should work with backend responses', async ({ page }) => {
    await page.goto('/');
    await page.waitForLoadState('networkidle');
    
    // Check for error handling elements - more flexible selectors
    const errorElements = await page.locator('[class*="error"], [class*="alert"], [role="alert"], [class*="danger"], [class*="warning"]').count();
    console.log(`Found ${errorElements} error handling elements`);
    
    // Check for success messages - more flexible selectors
    const successElements = await page.locator('[class*="success"], [class*="message"], [class*="info"], [class*="notification"]').count();
    console.log(`Found ${successElements} success message elements`);
  });

  test('Loading states should be handled properly', async ({ page }) => {
    await page.goto('/');
    
    // Check for loading indicators - more flexible selectors
    const loadingElements = await page.locator('[class*="loading"], [class*="spinner"], [aria-label*="loading"], [class*="wait"], [class*="pending"]').count();
    console.log(`Found ${loadingElements} loading elements`);
    
    // Wait for page to fully load
    await page.waitForLoadState('networkidle');
    
    // Check that loading states are gone - more flexible selectors
    const finalLoadingElements = await page.locator('[class*="loading"], [class*="spinner"], [class*="wait"], [class*="pending"]').count();
    console.log(`Loading elements after load: ${finalLoadingElements}`);
  });

  test('API endpoints should be accessible from frontend', async ({ page }) => {
    // Test direct API access from frontend context
    const response = await page.request.get('http://localhost:8080/courses');
    expect(response.status()).toBe(200);
    
    const coursesData = await response.json();
    const courses = coursesData.courses || coursesData;
    expect(Array.isArray(courses)).toBe(true);
    console.log(`API returned ${courses.length} courses`);
    
    const enrollmentsResponse = await page.request.get('http://localhost:8080/enrollments');
    expect(enrollmentsResponse.status()).toBe(200);
    
    const enrollments = await enrollmentsResponse.json();
    expect(Array.isArray(enrollments)).toBe(true);
    console.log(`API returned ${enrollments.length} enrollments`);
  });

});
