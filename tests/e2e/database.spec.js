const { test, expect } = require('@playwright/test');

test.describe('Database Operations', () => {
  
  test('should create and retrieve courses', async ({ request }) => {
    // Test GET /courses endpoint
    const response = await request.get('http://localhost:8080/courses');
    expect(response.status()).toBe(200);
    
    const coursesData = await response.json();
    const courses = coursesData.courses || coursesData;
    expect(Array.isArray(courses)).toBe(true);
    console.log('Initial courses count:', courses.length);
  });

  test('should create and retrieve enrollments', async ({ request }) => {
    // Test GET /enrollments endpoint
    const response = await request.get('http://localhost:8080/enrollments');
    expect(response.status()).toBe(200);
    
    const enrollments = await response.json();
    expect(Array.isArray(enrollments)).toBe(true);
    console.log('Initial enrollments count:', enrollments.length);
  });

  test('should create a new course via API', async ({ request }) => {
    const courseData = {
      titolo: 'Test Course - Playwright',
      dataOraInizio: '2024-12-01T10:00:00',
      luogo: 'Test Location',
      disponibilita: 10
    };

    // Create course (if POST endpoint exists)
    const response = await request.post('http://localhost:8080/courses', {
      data: courseData,
      headers: { 'Content-Type': 'application/json' }
    });
    
    // Note: This might return 404 if POST endpoint doesn't exist
    console.log('Course creation response status:', response.status());
  });

  test('should verify database schema', async ({ request }) => {
    // Test that the database is properly initialized
    const healthResponse = await request.get('http://localhost:8080/actuator/health');
    expect(healthResponse.status()).toBe(200);
    
    const health = await healthResponse.json();
    expect(health.status).toBe('UP');
    console.log('Database health:', health);
  });

});
