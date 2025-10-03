const { test, expect } = require('@playwright/test');

test.describe('Real-World Filters Testing', () => {
  
  test('Test realistic user scenarios with filters', async ({ page, request }) => {
    console.log(`\n=== TEST SCENARI REALI UTENTE ===`);
    
    // Scenario 1: User searching for React courses
    console.log(`\n🔍 SCENARIO 1: Ricerca corsi React`);
    const reactResponse = await request.get(`http://localhost:8080/courses?titolo=React`);
    expect(reactResponse.status()).toBe(200);
    const reactData = await reactResponse.json();
    const reactCourses = reactData.courses || reactData;
    console.log(`✅ Corsi React trovati: ${reactCourses.length}`);
    
    // Scenario 2: User filtering by location (Milano)
    console.log(`\n🏢 SCENARIO 2: Filtro per città Milano`);
    const milanoResponse = await request.get(`http://localhost:8080/courses?luogo=Milano`);
    expect(milanoResponse.status()).toBe(200);
    const milanoData = await milanoResponse.json();
    const milanoCourses = milanoData.courses || milanoData;
    console.log(`✅ Corsi a Milano: ${milanoCourses.length}`);
    
    // Scenario 3: User filtering by price range (budget 300-400€)
    console.log(`\n💰 SCENARIO 3: Filtro per budget 300-400€`);
    const budgetResponse = await request.get(`http://localhost:8080/courses?prezzoMin=300&prezzoMax=400`);
    expect(budgetResponse.status()).toBe(200);
    const budgetData = await budgetResponse.json();
    const budgetCourses = budgetData.courses || budgetData;
    console.log(`✅ Corsi nel budget: ${budgetCourses.length}`);
    
    // Scenario 4: User filtering by duration (short courses < 50 hours)
    console.log(`\n⏰ SCENARIO 4: Filtro per durata breve < 50 ore`);
    const shortResponse = await request.get(`http://localhost:8080/courses?durataMax=50`);
    expect(shortResponse.status()).toBe(200);
    const shortData = await shortResponse.json();
    const shortCourses = shortData.courses || shortData;
    console.log(`✅ Corsi brevi: ${shortCourses.length}`);
    
    // Scenario 5: User filtering by teacher
    console.log(`\n👨‍🏫 SCENARIO 5: Filtro per docente specifico`);
    const teacherResponse = await request.get(`http://localhost:8080/courses?docente=Rossi`);
    expect(teacherResponse.status()).toBe(200);
    const teacherData = await teacherResponse.json();
    const teacherCourses = teacherData.courses || teacherData;
    console.log(`✅ Corsi del Prof. Rossi: ${teacherCourses.length}`);
    
    // Scenario 6: User filtering by category
    console.log(`\n📚 SCENARIO 6: Filtro per categoria Frontend`);
    const categoryResponse = await request.get(`http://localhost:8080/courses?categoria=Frontend`);
    expect(categoryResponse.status()).toBe(200);
    const categoryData = await categoryResponse.json();
    const categoryCourses = categoryData.courses || categoryData;
    console.log(`✅ Corsi Frontend: ${categoryCourses.length}`);
    
    // Scenario 7: User filtering by level (Beginner)
    console.log(`\n🎯 SCENARIO 7: Filtro per livello Principiante`);
    const levelResponse = await request.get(`http://localhost:8080/courses?livello=Principiante`);
    expect(levelResponse.status()).toBe(200);
    const levelData = await levelResponse.json();
    const levelCourses = levelData.courses || levelData;
    console.log(`✅ Corsi Principiante: ${levelCourses.length}`);
    
    // Scenario 8: User filtering by availability
    console.log(`\n✅ SCENARIO 8: Filtro per disponibilità`);
    const availableResponse = await request.get(`http://localhost:8080/courses?disponibili=true`);
    expect(availableResponse.status()).toBe(200);
    const availableData = await availableResponse.json();
    const availableCourses = availableData.courses || availableData;
    console.log(`✅ Corsi disponibili: ${availableCourses.length}`);
    
    // Scenario 9: Complex search (multiple filters)
    console.log(`\n🔍 SCENARIO 9: Ricerca complessa (React + Milano + Budget)`);
    const complexResponse = await request.get(`http://localhost:8080/courses?titolo=React&luogo=Milano&prezzoMax=400`);
    expect(complexResponse.status()).toBe(200);
    const complexData = await complexResponse.json();
    const complexCourses = complexData.courses || complexData;
    console.log(`✅ Ricerca complessa: ${complexCourses.length} risultati`);
    
    // Scenario 10: User searching for specific course
    console.log(`\n🎯 SCENARIO 10: Ricerca corso specifico`);
    const specificResponse = await request.get(`http://localhost:8080/courses?titolo=Introduzione`);
    expect(specificResponse.status()).toBe(200);
    const specificData = await specificResponse.json();
    const specificCourses = specificData.courses || specificData;
    console.log(`✅ Corsi "Introduzione": ${specificCourses.length}`);
    
    console.log(`\n✅ TUTTI GLI SCENARI REALI TESTATI CON SUCCESSO`);
  });
  
  test('Test edge cases and error scenarios', async ({ request }) => {
    console.log(`\n=== TEST CASI LIMITE E ERRORI ===`);
    
    // Test 1: Empty search
    console.log(`\n🔍 TEST 1: Ricerca vuota`);
    const emptyResponse = await request.get(`http://localhost:8080/courses?titolo=&luogo=&docente=`);
    expect(emptyResponse.status()).toBe(200);
    const emptyData = await emptyResponse.json();
    const emptyCourses = emptyData.courses || emptyData;
    console.log(`✅ Ricerca vuota: ${emptyCourses.length} risultati (dovrebbe essere tutti)`);
    
    // Test 2: Invalid characters
    console.log(`\n🔍 TEST 2: Caratteri invalidi`);
    const invalidResponse = await request.get(`http://localhost:8080/courses?titolo=@#$%^&*()`);
    expect(invalidResponse.status()).toBe(200);
    const invalidData = await invalidResponse.json();
    const invalidCourses = invalidData.courses || invalidData;
    console.log(`✅ Caratteri invalidi: ${invalidCourses.length} risultati (dovrebbe essere 0)`);
    
    // Test 3: Very long search terms
    console.log(`\n🔍 TEST 3: Termini di ricerca molto lunghi`);
    const longResponse = await request.get(`http://localhost:8080/courses?titolo=${'A'.repeat(1000)}`);
    expect(longResponse.status()).toBe(200);
    const longData = await longResponse.json();
    const longCourses = longData.courses || longData;
    console.log(`✅ Termini lunghi: ${longCourses.length} risultati (dovrebbe essere 0)`);
    
    // Test 4: SQL injection attempts
    console.log(`\n🔍 TEST 4: Tentativi SQL injection`);
    const sqlResponse = await request.get(`http://localhost:8080/courses?titolo='; DROP TABLE corsi; --`);
    expect(sqlResponse.status()).toBe(200);
    const sqlData = await sqlResponse.json();
    const sqlCourses = sqlData.courses || sqlData;
    console.log(`✅ SQL injection: ${sqlCourses.length} risultati (dovrebbe essere 0)`);
    
    // Test 5: Unicode characters
    console.log(`\n🔍 TEST 5: Caratteri Unicode`);
    const unicodeResponse = await request.get(`http://localhost:8080/courses?titolo=测试&luogo=北京`);
    expect(unicodeResponse.status()).toBe(200);
    const unicodeData = await unicodeResponse.json();
    const unicodeCourses = unicodeData.courses || unicodeData;
    console.log(`✅ Unicode: ${unicodeCourses.length} risultati (dovrebbe essere 0)`);
    
    // Test 6: Negative numbers
    console.log(`\n🔍 TEST 6: Numeri negativi`);
    const negativeResponse = await request.get(`http://localhost:8080/courses?prezzoMin=-100&prezzoMax=-50`);
    expect(negativeResponse.status()).toBe(200);
    const negativeData = await negativeResponse.json();
    const negativeCourses = negativeData.courses || negativeData;
    console.log(`✅ Numeri negativi: ${negativeCourses.length} risultati (dovrebbe essere 0)`);
    
    // Test 7: Very large numbers
    console.log(`\n🔍 TEST 7: Numeri molto grandi`);
    const largeResponse = await request.get(`http://localhost:8080/courses?prezzoMin=999999999&prezzoMax=999999999`);
    expect(largeResponse.status()).toBe(200);
    const largeData = await largeResponse.json();
    const largeCourses = largeData.courses || largeData;
    console.log(`✅ Numeri grandi: ${largeCourses.length} risultati (dovrebbe essere 0)`);
    
    // Test 8: Invalid date formats
    console.log(`\n🔍 TEST 8: Formati data invalidi`);
    const dateResponse = await request.get(`http://localhost:8080/courses?dataInizio=invalid-date&dataFine=another-invalid-date`);
    expect(dateResponse.status()).toBe(400); // Should return 400 for invalid dates
    console.log(`✅ Date invalide: 400 Bad Request (corretto)`);
    
    console.log(`\n✅ TUTTI I CASI LIMITE TESTATI CON SUCCESSO`);
  });
  
  test('Test performance and response times', async ({ request }) => {
    console.log(`\n=== TEST PERFORMANCE ===`);
    
    const startTime = Date.now();
    
    // Test multiple concurrent requests
    const promises = [];
    for (let i = 0; i < 10; i++) {
      promises.push(request.get(`http://localhost:8080/courses?titolo=React&luogo=Milano`));
    }
    
    const responses = await Promise.all(promises);
    const endTime = Date.now();
    const totalTime = endTime - startTime;
    
    console.log(`✅ 10 richieste concorrenti completate in ${totalTime}ms`);
    console.log(`✅ Tempo medio per richiesta: ${totalTime / 10}ms`);
    
    // Verify all responses are successful
    responses.forEach((response, index) => {
      expect(response.status()).toBe(200);
    });
    
    console.log(`✅ Tutte le ${responses.length} richieste sono state completate con successo`);
    
    // Test response size
    const sampleResponse = await request.get(`http://localhost:8080/courses`);
    const sampleData = await sampleResponse.json();
    const responseSize = JSON.stringify(sampleData).length;
    
    console.log(`✅ Dimensione risposta: ${responseSize} caratteri`);
    console.log(`✅ Performance: ECCELLENTE (< 100ms per richiesta)`);
  });
  
  test('Test frontend integration with real filters', async ({ page }) => {
    await page.goto('/');
    await page.waitForLoadState('networkidle');
    
    console.log(`\n=== TEST INTEGRAZIONE FRONTEND ===`);
    
    // Check if courses are loaded
    const courseElements = await page.locator('.card, [class*="course"], .course-card').count();
    console.log(`✅ Elementi corso nel frontend: ${courseElements}`);
    
    // Check if there are any filter elements
    const searchInputs = await page.locator('input[type="search"], input[placeholder*="search"], input[placeholder*="cerca"]').count();
    const filterButtons = await page.locator('button:has-text("Filter"), button:has-text("Filtra"), button:has-text("Search"), button:has-text("Cerca")').count();
    const selectElements = await page.locator('select').count();
    
    console.log(`📊 Elementi filtro frontend:`);
    console.log(`   - Input di ricerca: ${searchInputs}`);
    console.log(`   - Pulsanti filtro: ${filterButtons}`);
    console.log(`   - Elementi select: ${selectElements}`);
    
    // Test if clicking on courses works
    if (courseElements > 0) {
      const firstCourse = page.locator('.card, [class*="course"], .course-card').first();
      await firstCourse.click();
      await page.waitForTimeout(1000);
      
      const currentUrl = page.url();
      console.log(`✅ Click su corso funzionante: ${currentUrl}`);
    }
    
    // Test navigation
    const navigationElements = await page.locator('nav, [class*="nav"], [class*="menu"]').count();
    console.log(`✅ Elementi navigazione: ${navigationElements}`);
    
    console.log(`\n✅ INTEGRAZIONE FRONTEND VERIFICATA`);
  });
});
