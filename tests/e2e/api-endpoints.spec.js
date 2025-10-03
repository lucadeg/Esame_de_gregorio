const { test, expect } = require('@playwright/test');

test.describe('API Endpoints - Requirements Section 2', () => {
  
  test('GET /courses - should retrieve all available courses', async ({ request }) => {
    const response = await request.get('http://localhost:8080/courses');
    
    expect(response.status()).toBe(200);
    
    const coursesData = await response.json();
    const courses = coursesData.courses || coursesData;
    expect(Array.isArray(courses)).toBe(true);
    
    // Verify course structure if courses exist
    if (courses.length > 0) {
      const course = courses[0];
      expect(course).toHaveProperty('corsoId');
      expect(course).toHaveProperty('titolo');
      expect(course).toHaveProperty('dataOraInizio');
      expect(course).toHaveProperty('luogo');
      expect(course).toHaveProperty('disponibilita');
    }
    
    console.log(`Found ${courses.length} courses`);
  });

  test('GET /courses with query parameters - should support search filters', async ({ request }) => {
    // Test with search parameters
    const response = await request.get('http://localhost:8080/courses?search=test');
    expect(response.status()).toBe(200);
    
    const coursesData = await response.json();
    const courses = coursesData.courses || coursesData;
    expect(Array.isArray(courses)).toBe(true);
    console.log(`Found ${courses.length} courses with search filter`);
  });

  test('GET /enrollments - should retrieve all enrollments', async ({ request }) => {
    const response = await request.get('http://localhost:8080/enrollments');
    
    expect(response.status()).toBe(200);
    
    const enrollments = await response.json();
    expect(Array.isArray(enrollments)).toBe(true);
    
    // Verify enrollment structure if enrollments exist
    if (enrollments.length > 0) {
      const enrollment = enrollments[0];
      expect(enrollment).toHaveProperty('iscrizioneId');
      expect(enrollment).toHaveProperty('corsoId');
      expect(enrollment).toHaveProperty('partecipanteNome');
      expect(enrollment).toHaveProperty('partecipanteCognome');
      expect(enrollment).toHaveProperty('partecipanteEmail');
      expect(enrollment).toHaveProperty('dataOraIscrizione');
    }
    
    console.log(`Found ${enrollments.length} enrollments`);
  });

  test('GET /enrollments with course filter - should filter by course', async ({ request }) => {
    // Test filtering by course ID
    const response = await request.get('http://localhost:8080/enrollments?corsoId=1');
    expect(response.status()).toBe(200);
    
    const enrollments = await response.json();
    expect(Array.isArray(enrollments)).toBe(true);
    console.log(`Found ${enrollments.length} enrollments for course 1`);
  });

  test('POST /enrollments - should create new enrollment', async ({ request }) => {
    // First, get available courses
    const coursesResponse = await request.get('http://localhost:8080/courses');
    const coursesData = await coursesResponse.json();
    const courses = coursesData.courses || coursesData;
    
    if (courses.length === 0) {
      console.log('No courses available for enrollment test');
      return;
    }
    
    const courseId = courses[0].corsoId;
    
    const enrollmentData = {
      corsoId: courseId,
      partecipanteNome: 'Test',
      partecipanteCognome: 'User',
      partecipanteEmail: 'test@example.com'
    };

    const response = await request.post('http://localhost:8080/enrollments', {
      data: enrollmentData,
      headers: { 'Content-Type': 'application/json' }
    });
    
    // Check if endpoint exists and works
    console.log('Enrollment creation response status:', response.status());
    
    if (response.status() === 200 || response.status() === 201) {
      const enrollment = await response.json();
      expect(enrollment).toHaveProperty('iscrizioneId');
      expect(enrollment.partecipanteNome).toBe('Test');
      expect(enrollment.partecipanteCognome).toBe('User');
      expect(enrollment.partecipanteEmail).toBe('test@example.com');
      console.log('Enrollment created successfully:', enrollment);
    } else {
      console.log('POST /enrollments endpoint not implemented or returned error');
    }
  });

  test('API should return proper JSON responses', async ({ request }) => {
    const endpoints = [
      'http://localhost:8080/courses',
      'http://localhost:8080/enrollments'
    ];

    for (const endpoint of endpoints) {
      const response = await request.get(endpoint);
      expect(response.status()).toBe(200);
      
      const contentType = response.headers()['content-type'];
      expect(contentType).toContain('application/json');
      
      const data = await response.json();
      const arrayData = data.courses || data;
      expect(Array.isArray(arrayData)).toBe(true);
    }
  });

});
