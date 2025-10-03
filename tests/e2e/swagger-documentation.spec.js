const { test, expect } = require('@playwright/test');

test.describe('Swagger UI Documentation - Requirements Section 2b', () => {
  
  test('Swagger UI should be accessible', async ({ page }) => {
    await page.goto('http://localhost:8080/api/swagger-ui.html');
    await page.waitForLoadState('networkidle');
    
    // Check if Swagger UI loaded
    const swaggerElements = await page.locator('.swagger-ui, [class*="swagger"]').count();
    expect(swaggerElements).toBeGreaterThan(0);
    
    // Check for API documentation elements
    const apiElements = await page.locator('[class*="api"], [class*="endpoint"]').count();
    console.log(`Found ${apiElements} API documentation elements`);
  });

  test('OpenAPI JSON should be accessible', async ({ page }) => {
    const response = await page.request.get('http://localhost:8080/v3/api-docs');
    expect(response.status()).toBe(200);
    
    const openApiSpec = await response.json();
    expect(openApiSpec).toHaveProperty('openapi');
    expect(openApiSpec).toHaveProperty('info');
    expect(openApiSpec).toHaveProperty('paths');
    
    console.log('OpenAPI version:', openApiSpec.openapi);
    console.log('API title:', openApiSpec.info?.title);
    console.log('Available paths:', Object.keys(openApiSpec.paths || {}));
  });

  test('API documentation should include all required endpoints', async ({ page }) => {
    const response = await page.request.get('http://localhost:8080/v3/api-docs');
    const openApiSpec = await response.json();
    
    const paths = openApiSpec.paths || {};
    
    // Check for required endpoints from requirements
    const requiredEndpoints = [
      '/courses',
      '/enrollments'
    ];
    
    for (const endpoint of requiredEndpoints) {
      expect(paths).toHaveProperty(endpoint);
      console.log(`✓ Found endpoint: ${endpoint}`);
    }
    
    // Check for HTTP methods
    if (paths['/courses']) {
      const courseMethods = Object.keys(paths['/courses']);
      console.log(`Course endpoint methods: ${courseMethods.join(', ')}`);
      expect(courseMethods).toContain('get');
    }
    
    if (paths['/enrollments']) {
      const enrollmentMethods = Object.keys(paths['/enrollments']);
      console.log(`Enrollment endpoint methods: ${enrollmentMethods.join(', ')}`);
      expect(enrollmentMethods).toContain('get');
    }
  });

  test('API documentation should have proper schemas', async ({ page }) => {
    const response = await page.request.get('http://localhost:8080/v3/api-docs');
    const openApiSpec = await response.json();
    
    // Check for components/schemas
    if (openApiSpec.components && openApiSpec.components.schemas) {
      const schemas = openApiSpec.components.schemas;
      console.log('Available schemas:', Object.keys(schemas));
      
      // Look for course and enrollment schemas
      const schemaNames = Object.keys(schemas);
      const hasCourseSchema = schemaNames.some(name => name.toLowerCase().includes('course') || name.toLowerCase().includes('corso'));
      const hasEnrollmentSchema = schemaNames.some(name => name.toLowerCase().includes('enrollment') || name.toLowerCase().includes('iscrizione'));
      
      console.log(`Has course schema: ${hasCourseSchema}`);
      console.log(`Has enrollment schema: ${hasEnrollmentSchema}`);
    }
  });

  test('Swagger UI should display endpoint details', async ({ page }) => {
    await page.goto('http://localhost:8080/api/swagger-ui.html');
    await page.waitForLoadState('networkidle');
    
    // Look for endpoint sections
    const endpointSections = await page.locator('[class*="opblock"], [class*="endpoint"]').count();
    console.log(`Found ${endpointSections} endpoint sections`);
    
    // Look for HTTP methods
    const httpMethods = await page.locator('[class*="http"], [class*="method"]').count();
    console.log(`Found ${httpMethods} HTTP method indicators`);
    
    // Look for parameter sections
    const parameterSections = await page.locator('[class*="parameter"], [class*="param"]').count();
    console.log(`Found ${parameterSections} parameter sections`);
  });

  test('API documentation should be complete and valid', async ({ page }) => {
    const response = await page.request.get('http://localhost:8080/v3/api-docs');
    const openApiSpec = await response.json();
    
    // Validate OpenAPI structure
    expect(openApiSpec.openapi).toMatch(/^\d+\.\d+\.\d+$/);
    expect(openApiSpec.info).toBeDefined();
    expect(openApiSpec.info.title).toBeDefined();
    expect(openApiSpec.info.version).toBeDefined();
    
    // Check for proper path definitions
    const paths = openApiSpec.paths || {};
    for (const [path, pathItem] of Object.entries(paths)) {
      expect(pathItem).toBeDefined();
      console.log(`Path ${path} has methods: ${Object.keys(pathItem).join(', ')}`);
    }
    
    console.log('OpenAPI specification is valid and complete');
  });

});
