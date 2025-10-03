# Test System - Verify Everything Works
# Test Sistema - Verifica che Tutto Funzioni

Write-Host "=== TEST SISTEMA ===" -ForegroundColor Blue
Write-Host "Data: $(Get-Date -Format 'yyyy-MM-dd HH:mm:ss')" -ForegroundColor Gray

$baseUrl = "http://localhost:8080"
$frontendUrl = "http://localhost:5173"

Write-Host "`n1. Test connessione Backend..." -ForegroundColor Yellow
try {
    $response = Invoke-WebRequest -Uri "$baseUrl/actuator/health" -TimeoutSec 10 -ErrorAction Stop
    if ($response.StatusCode -eq 200) {
        Write-Host "Backend risponde correttamente" -ForegroundColor Green
        Write-Host "  Status: $($response.StatusCode)" -ForegroundColor Gray
    } else {
        Write-Host "Backend risponde ma con status: $($response.StatusCode)" -ForegroundColor Yellow
    }
} catch {
    Write-Host "Backend non raggiungibile: $_" -ForegroundColor Red
    Write-Host "  Assicurati che il backend sia avviato con: scripts\start\start-backend-fixed.ps1" -ForegroundColor Yellow
}

Write-Host "`n2. Test connessione Frontend..." -ForegroundColor Yellow
try {
    $response = Invoke-WebRequest -Uri $frontendUrl -TimeoutSec 10 -ErrorAction Stop
    if ($response.StatusCode -eq 200) {
        Write-Host "Frontend risponde correttamente" -ForegroundColor Green
        Write-Host "  Status: $($response.StatusCode)" -ForegroundColor Gray
    } else {
        Write-Host "Frontend risponde ma con status: $($response.StatusCode)" -ForegroundColor Yellow
    }
} catch {
    Write-Host "Frontend non raggiungibile: $_" -ForegroundColor Red
    Write-Host "  Assicurati che il frontend sia avviato con: scripts\start\start-frontend.ps1" -ForegroundColor Yellow
}

Write-Host "`n3. Test API Endpoints..." -ForegroundColor Yellow

# Test courses endpoint
try {
    $response = Invoke-WebRequest -Uri "$baseUrl/courses" -TimeoutSec 10 -ErrorAction Stop
    if ($response.StatusCode -eq 200) {
        Write-Host "API Courses funzionante" -ForegroundColor Green
    } else {
        Write-Host "API Courses risponde con status: $($response.StatusCode)" -ForegroundColor Yellow
    }
} catch {
    Write-Host "API Courses non raggiungibile: $_" -ForegroundColor Red
}

# Test enrollments endpoint
try {
    $response = Invoke-WebRequest -Uri "$baseUrl/enrollments" -TimeoutSec 10 -ErrorAction Stop
    if ($response.StatusCode -eq 200) {
        Write-Host "API Enrollments funzionante" -ForegroundColor Green
    } else {
        Write-Host "API Enrollments risponde con status: $($response.StatusCode)" -ForegroundColor Yellow
    }
} catch {
    Write-Host "API Enrollments non raggiungibile: $_" -ForegroundColor Red
}

# Test registration endpoint
try {
    $body = @{
        nome = "Test"
        cognome = "User"
        email = "test@example.com"
        password = "password123"
    } | ConvertTo-Json
    
    $response = Invoke-WebRequest -Uri "$baseUrl/api/v1/auth/register" -Method POST -Body $body -ContentType "application/json" -TimeoutSec 10 -ErrorAction Stop
    if ($response.StatusCode -eq 201) {
        Write-Host "API Registration funzionante" -ForegroundColor Green
    } else {
        Write-Host "API Registration risponde con status: $($response.StatusCode)" -ForegroundColor Yellow
    }
} catch {
    Write-Host "API Registration non raggiungibile: $_" -ForegroundColor Red
}

Write-Host "`n4. Test Swagger Documentation..." -ForegroundColor Yellow
try {
    $response = Invoke-WebRequest -Uri "$baseUrl/swagger-ui.html" -TimeoutSec 10 -ErrorAction Stop
    if ($response.StatusCode -eq 200) {
        Write-Host "Swagger Documentation accessibile" -ForegroundColor Green
    } else {
        Write-Host "Swagger risponde con status: $($response.StatusCode)" -ForegroundColor Yellow
    }
} catch {
    Write-Host "Swagger Documentation non raggiungibile: $_" -ForegroundColor Red
}

Write-Host "`n5. Test H2 Console..." -ForegroundColor Yellow
try {
    $response = Invoke-WebRequest -Uri "$baseUrl/h2-console" -TimeoutSec 10 -ErrorAction Stop
    if ($response.StatusCode -eq 200) {
        Write-Host "H2 Console accessibile" -ForegroundColor Green
    } else {
        Write-Host "H2 Console risponde con status: $($response.StatusCode)" -ForegroundColor Yellow
    }
} catch {
    Write-Host "H2 Console non raggiungibile: $_" -ForegroundColor Red
}

Write-Host "`nRISULTATI TEST:" -ForegroundColor Cyan
Write-Host "=" * 50 -ForegroundColor Gray
Write-Host "Frontend: $frontendUrl" -ForegroundColor White
Write-Host "Backend API: $baseUrl" -ForegroundColor White
Write-Host "Courses: $baseUrl/courses" -ForegroundColor White
Write-Host "Registration: $baseUrl/api/v1/auth/register" -ForegroundColor White
Write-Host "Swagger: $baseUrl/swagger-ui.html" -ForegroundColor White
Write-Host "H2 Console: $baseUrl/h2-console" -ForegroundColor White
Write-Host "Health: $baseUrl/actuator/health" -ForegroundColor White
Write-Host "=" * 50 -ForegroundColor Gray

Write-Host "`nSUGGERIMENTI:" -ForegroundColor Yellow
Write-Host "  - Se alcuni test falliscono, riavvia i servizi" -ForegroundColor White
Write-Host "  - Usa 'scripts\start\start-all.ps1' per avviare tutto" -ForegroundColor White
Write-Host "  - Usa 'scripts\start\stop-all.ps1' per fermare tutto" -ForegroundColor White
Write-Host "  - Controlla i log nelle finestre dei servizi per errori" -ForegroundColor White
