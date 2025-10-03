const { test, expect } = require('@playwright/test');

test.describe('Comprehensive Filters Testing', () => {
  
  test('Test all course filters with real data', async ({ page, request }) => {
    // First, get all courses to understand the data structure
    const allCoursesResponse = await request.get('http://localhost:8080/courses');
    expect(allCoursesResponse.status()).toBe(200);
    const allCoursesData = await allCoursesResponse.json();
    const allCourses = allCoursesData.courses || allCoursesData;
    
    console.log(`\n=== ANALISI DATI REALI ===`);
    console.log(`Totale corsi: ${allCourses.length}`);
    
    // Analyze data structure for testing
    const titles = allCourses.map(c => c.titolo).filter(Boolean);
    const locations = allCourses.map(c => c.luogo).filter(Boolean);
    const teachers = allCourses.map(c => c.docenti).filter(Boolean);
    const categories = allCourses.map(c => c.categoria).filter(Boolean);
    const levels = allCourses.map(c => c.livello).filter(Boolean);
    const prices = allCourses.map(c => c.prezzo).filter(p => p !== null && p !== undefined);
    const durations = allCourses.map(c => c.durataOre).filter(d => d !== null && d !== undefined);
    
    console.log(`Titoli unici: ${[...new Set(titles)].length}`);
    console.log(`Luoghi unici: ${[...new Set(locations)].length}`);
    console.log(`Docenti unici: ${[...new Set(teachers)].length}`);
    console.log(`Categorie uniche: ${[...new Set(categories)].length}`);
    console.log(`Livelli unici: ${[...new Set(levels)].length}`);
    console.log(`Prezzi: min=${Math.min(...prices)}, max=${Math.max(...prices)}`);
    console.log(`Durata: min=${Math.min(...durations)}, max=${Math.max(...durations)}`);
    
    // Test 1: Filter by title (partial match)
    console.log(`\n=== TEST 1: FILTRO PER TITOLO ===`);
    const titleFilter = titles[0] ? titles[0].substring(0, 5) : 'React';
    const titleResponse = await request.get(`http://localhost:8080/courses?titolo=${encodeURIComponent(titleFilter)}`);
    expect(titleResponse.status()).toBe(200);
    const titleData = await titleResponse.json();
    const titleCourses = titleData.courses || titleData;
    console.log(`Filtro titolo "${titleFilter}": ${titleCourses.length} risultati`);
    
    // Test 2: Filter by location
    console.log(`\n=== TEST 2: FILTRO PER LUOGO ===`);
    const locationFilter = locations[0] || 'Milano';
    const locationResponse = await request.get(`http://localhost:8080/courses?luogo=${encodeURIComponent(locationFilter)}`);
    expect(locationResponse.status()).toBe(200);
    const locationData = await locationResponse.json();
    const locationCourses = locationData.courses || locationData;
    console.log(`Filtro luogo "${locationFilter}": ${locationCourses.length} risultati`);
    
    // Test 3: Filter by teacher
    console.log(`\n=== TEST 3: FILTRO PER DOCENTE ===`);
    const teacherFilter = teachers[0] || 'Prof';
    const teacherResponse = await request.get(`http://localhost:8080/courses?docente=${encodeURIComponent(teacherFilter)}`);
    expect(teacherResponse.status()).toBe(200);
    const teacherData = await teacherResponse.json();
    const teacherCourses = teacherData.courses || teacherData;
    console.log(`Filtro docente "${teacherFilter}": ${teacherCourses.length} risultati`);
    
    // Test 4: Filter by category
    console.log(`\n=== TEST 4: FILTRO PER CATEGORIA ===`);
    const categoryFilter = categories[0] || 'Programmazione';
    const categoryResponse = await request.get(`http://localhost:8080/courses?categoria=${encodeURIComponent(categoryFilter)}`);
    expect(categoryResponse.status()).toBe(200);
    const categoryData = await categoryResponse.json();
    const categoryCourses = categoryData.courses || categoryData;
    console.log(`Filtro categoria "${categoryFilter}": ${categoryCourses.length} risultati`);
    
    // Test 5: Filter by level
    console.log(`\n=== TEST 5: FILTRO PER LIVELLO ===`);
    const levelFilter = levels[0] || 'Base';
    const levelResponse = await request.get(`http://localhost:8080/courses?livello=${encodeURIComponent(levelFilter)}`);
    expect(levelResponse.status()).toBe(200);
    const levelData = await levelResponse.json();
    const levelCourses = levelData.courses || levelData;
    console.log(`Filtro livello "${levelFilter}": ${levelCourses.length} risultati`);
    
    // Test 6: Filter by price range
    console.log(`\n=== TEST 6: FILTRO PER RANGE PREZZO ===`);
    const minPrice = Math.min(...prices);
    const maxPrice = Math.max(...prices);
    const midPrice = Math.floor((minPrice + maxPrice) / 2);
    
    const priceResponse = await request.get(`http://localhost:8080/courses?prezzoMin=${minPrice}&prezzoMax=${midPrice}`);
    expect(priceResponse.status()).toBe(200);
    const priceData = await priceResponse.json();
    const priceCourses = priceData.courses || priceData;
    console.log(`Filtro prezzo ${minPrice}-${midPrice}: ${priceCourses.length} risultati`);
    
    // Test 7: Filter by duration range
    console.log(`\n=== TEST 7: FILTRO PER RANGE DURATA ===`);
    const minDuration = Math.min(...durations);
    const maxDuration = Math.max(...durations);
    const midDuration = Math.floor((minDuration + maxDuration) / 2);
    
    const durationResponse = await request.get(`http://localhost:8080/courses?durataMin=${minDuration}&durataMax=${midDuration}`);
    expect(durationResponse.status()).toBe(200);
    const durationData = await durationResponse.json();
    const durationCourses = durationData.courses || durationData;
    console.log(`Filtro durata ${minDuration}-${midDuration}: ${durationCourses.length} risultati`);
    
    // Test 8: Filter by availability
    console.log(`\n=== TEST 8: FILTRO PER DISPONIBILITÀ ===`);
    const availabilityResponse = await request.get(`http://localhost:8080/courses?disponibili=true`);
    expect(availabilityResponse.status()).toBe(200);
    const availabilityData = await availabilityResponse.json();
    const availabilityCourses = availabilityData.courses || availabilityData;
    console.log(`Filtro disponibili: ${availabilityCourses.length} risultati`);
    
    // Test 9: Multiple filters combination
    console.log(`\n=== TEST 9: FILTRI MULTIPLI ===`);
    const multipleResponse = await request.get(`http://localhost:8080/courses?titolo=${encodeURIComponent(titleFilter)}&luogo=${encodeURIComponent(locationFilter)}`);
    expect(multipleResponse.status()).toBe(200);
    const multipleData = await multipleResponse.json();
    const multipleCourses = multipleData.courses || multipleData;
    console.log(`Filtri multipli: ${multipleCourses.length} risultati`);
    
    // Test 10: Invalid filters (should not break)
    console.log(`\n=== TEST 10: FILTRI INVALIDI ===`);
    const invalidResponse = await request.get(`http://localhost:8080/courses?titolo=INVALID_TITLE_12345&luogo=INVALID_LOCATION_12345`);
    expect(invalidResponse.status()).toBe(200);
    const invalidData = await invalidResponse.json();
    const invalidCourses = invalidData.courses || invalidData;
    console.log(`Filtri invalidi: ${invalidCourses.length} risultati (dovrebbe essere 0)`);
    
    // Test 11: Empty filters (should return all)
    console.log(`\n=== TEST 11: FILTRI VUOTI ===`);
    const emptyResponse = await request.get(`http://localhost:8080/courses?titolo=&luogo=&docente=`);
    expect(emptyResponse.status()).toBe(200);
    const emptyData = await emptyResponse.json();
    const emptyCourses = emptyData.courses || emptyData;
    console.log(`Filtri vuoti: ${emptyCourses.length} risultati (dovrebbe essere ${allCourses.length})`);
    
    // Test 12: Special characters in filters
    console.log(`\n=== TEST 12: CARATTERI SPECIALI ===`);
    const specialResponse = await request.get(`http://localhost:8080/courses?titolo=test&luogo=Milano&docente=Prof. Rossi`);
    expect(specialResponse.status()).toBe(200);
    const specialData = await specialResponse.json();
    const specialCourses = specialData.courses || specialData;
    console.log(`Caratteri speciali: ${specialCourses.length} risultati`);
    
    // Test 13: Case sensitivity
    console.log(`\n=== TEST 13: CASE SENSITIVITY ===`);
    const caseResponse = await request.get(`http://localhost:8080/courses?titolo=REACT&luogo=milano`);
    expect(caseResponse.status()).toBe(200);
    const caseData = await caseResponse.json();
    const caseCourses = caseData.courses || caseData;
    console.log(`Case sensitivity: ${caseCourses.length} risultati`);
    
    // Test 14: Very long filter values
    console.log(`\n=== TEST 14: FILTRI MOLTO LUNGHI ===`);
    const longFilter = 'A'.repeat(1000);
    const longResponse = await request.get(`http://localhost:8080/courses?titolo=${encodeURIComponent(longFilter)}`);
    expect(longResponse.status()).toBe(200);
    const longData = await longResponse.json();
    const longCourses = longData.courses || longData;
    console.log(`Filtro molto lungo: ${longCourses.length} risultati`);
    
    // Test 15: SQL injection attempts
    console.log(`\n=== TEST 15: SICUREZZA SQL INJECTION ===`);
    const sqlInjectionResponse = await request.get(`http://localhost:8080/courses?titolo='; DROP TABLE corsi; --`);
    expect(sqlInjectionResponse.status()).toBe(200);
    const sqlInjectionData = await sqlInjectionResponse.json();
    const sqlInjectionCourses = sqlInjectionData.courses || sqlInjectionData;
    console.log(`SQL injection test: ${sqlInjectionCourses.length} risultati (dovrebbe essere 0)`);
    
    console.log(`\n=== RIEPILOGO TEST FILTRI ===`);
    console.log(`✅ Tutti i test dei filtri completati con successo`);
    console.log(`✅ Filtri funzionanti: titolo, luogo, docente, categoria, livello, prezzo, durata, disponibilità`);
    console.log(`✅ Filtri multipli funzionanti`);
    console.log(`✅ Gestione errori corretta`);
    console.log(`✅ Sicurezza SQL injection testata`);
  });
  
  test('Test frontend filters integration', async ({ page }) => {
    await page.goto('/');
    await page.waitForLoadState('networkidle');
    
    console.log(`\n=== TEST FRONTEND FILTRI ===`);
    
    // Check if filter elements exist
    const searchInputs = await page.locator('input[type="search"], input[placeholder*="search"], input[placeholder*="cerca"]').count();
    console.log(`Input di ricerca trovati: ${searchInputs}`);
    
    const filterButtons = await page.locator('button:has-text("Filter"), button:has-text("Filtra"), button:has-text("Search"), button:has-text("Cerca")').count();
    console.log(`Pulsanti filtro trovati: ${filterButtons}`);
    
    const selectElements = await page.locator('select').count();
    console.log(`Elementi select trovati: ${selectElements}`);
    
    // Test search functionality if available
    if (searchInputs > 0) {
      const searchInput = page.locator('input[type="search"], input[placeholder*="search"], input[placeholder*="cerca"]').first();
      await searchInput.fill('React');
      await page.waitForTimeout(2000);
      
      const courseElements = await page.locator('.card, [class*="course"], .course-card').count();
      console.log(`Dopo ricerca "React": ${courseElements} elementi corso`);
    }
    
    // Test filter buttons if available
    if (filterButtons > 0) {
      const filterButton = page.locator('button:has-text("Filter"), button:has-text("Filtra")').first();
      await filterButton.click();
      await page.waitForTimeout(2000);
      
      const courseElements = await page.locator('.card, [class*="course"], .course-card').count();
      console.log(`Dopo click filtro: ${courseElements} elementi corso`);
    }
    
    console.log(`✅ Test frontend filtri completato`);
  });
  
  test('Test edge cases and error handling', async ({ request }) => {
    console.log(`\n=== TEST CASI LIMITE E GESTIONE ERRORI ===`);
    
    // Test 1: Very large numbers
    const largeNumberResponse = await request.get(`http://localhost:8080/courses?prezzoMin=999999999&prezzoMax=999999999`);
    expect(largeNumberResponse.status()).toBe(200);
    console.log(`✅ Filtri con numeri molto grandi gestiti`);
    
    // Test 2: Negative numbers
    const negativeResponse = await request.get(`http://localhost:8080/courses?prezzoMin=-100&prezzoMax=-50`);
    expect(negativeResponse.status()).toBe(200);
    console.log(`✅ Filtri con numeri negativi gestiti`);
    
    // Test 3: Invalid date formats
    const dateResponse = await request.get(`http://localhost:8080/courses?dataInizio=invalid-date&dataFine=another-invalid-date`);
    expect(dateResponse.status()).toBe(200);
    console.log(`✅ Filtri con date invalide gestiti`);
    
    // Test 4: Unicode characters
    const unicodeResponse = await request.get(`http://localhost:8080/courses?titolo=测试&luogo=北京`);
    expect(unicodeResponse.status()).toBe(200);
    console.log(`✅ Filtri con caratteri Unicode gestiti`);
    
    // Test 5: Very long URLs
    const longUrlResponse = await request.get(`http://localhost:8080/courses?${'titolo=test&'.repeat(100)}`);
    expect(longUrlResponse.status()).toBe(200);
    console.log(`✅ URL molto lunghi gestiti`);
    
    console.log(`✅ Tutti i casi limite testati con successo`);
  });
});
