# Start Backend - Fixed and Reliable Script
# Avvia Backend - Script Corretto e Affidabile

Write-Host "=== AVVIO BACKEND CORRETTO ===" -ForegroundColor Green
Write-Host "Data: $(Get-Date -Format 'yyyy-MM-dd HH:mm:ss')" -ForegroundColor Gray

# Check Java
Write-Host "`n1. Verifica Java..." -ForegroundColor Yellow
try {
    $javaVersion = java -version 2>&1
    if ($LASTEXITCODE -eq 0) {
        Write-Host "Java trovato: $($javaVersion[0])" -ForegroundColor Green
    } else {
        Write-Host "Java non trovato! Installa Java 17+" -ForegroundColor Red
        exit 1
    }
} catch {
    Write-Host "Errore verifica Java: $_" -ForegroundColor Red
    exit 1
}

# Find Maven
Write-Host "`n2. Verifica Maven..." -ForegroundColor Yellow
$mavenCmd = "mvn.cmd"
if (Test-Path "C:\maven\apache-maven-3.9.5\bin\mvn.cmd") {
    $mavenCmd = "C:\maven\apache-maven-3.9.5\bin\mvn.cmd"
        Write-Host "Maven trovato: $mavenCmd" -ForegroundColor Green
} else {
    Write-Host "Maven non trovato in percorso standard, uso: $mavenCmd" -ForegroundColor Yellow
}

# Test Maven
try {
    $mavenVersion = & $mavenCmd --version 2>&1
    if ($LASTEXITCODE -eq 0) {
        Write-Host "Maven funzionante: $($mavenVersion[0])" -ForegroundColor Green
    } else {
        Write-Host "Maven non funzionante!" -ForegroundColor Red
        exit 1
    }
} catch {
    Write-Host "Errore Maven: $_" -ForegroundColor Red
    exit 1
}

# Go to backend directory
Write-Host "`n3. Navigazione cartella backend..." -ForegroundColor Yellow
if (Test-Path "backend") {
    Set-Location "backend"
    Write-Host "Cartella backend trovata" -ForegroundColor Green
} else {
    Write-Host "Cartella backend non trovata!" -ForegroundColor Red
    exit 1
}

# Clean and compile
Write-Host "`n4. Pulizia e compilazione..." -ForegroundColor Yellow
Write-Host "Esecuzione: $mavenCmd clean compile" -ForegroundColor Gray

try {
    & $mavenCmd clean compile
    if ($LASTEXITCODE -eq 0) {
        Write-Host "Compilazione completata con successo" -ForegroundColor Green
    } else {
        Write-Host "Compilazione fallita!" -ForegroundColor Red
        Write-Host "Controlla gli errori sopra e riprova" -ForegroundColor Yellow
        Set-Location ".."
        exit 1
    }
} catch {
    Write-Host "Errore durante compilazione: $_" -ForegroundColor Red
    Set-Location ".."
    exit 1
}

# Start backend
Write-Host "`n5. Avvio backend..." -ForegroundColor Yellow
Write-Host "Configurazione:" -ForegroundColor Cyan
Write-Host "  - Database: H2 (in-memory)" -ForegroundColor Gray
Write-Host "  - Porta: 8080" -ForegroundColor Gray
Write-Host "  - Profilo: h2" -ForegroundColor Gray
Write-Host "`nURL importanti:" -ForegroundColor Cyan
Write-Host "  - API: http://localhost:8080/api" -ForegroundColor White
Write-Host "  - Swagger: http://localhost:8080/api/swagger-ui.html" -ForegroundColor White
Write-Host "  - H2 Console: http://localhost:8080/api/h2-console" -ForegroundColor White
Write-Host "  - Health: http://localhost:8080/api/actuator/health" -ForegroundColor White

Write-Host "`nIMPORTANTE:" -ForegroundColor Yellow
Write-Host "  - Il backend si avvierà in questa finestra" -ForegroundColor White
Write-Host "  - Premi Ctrl+C per fermarlo" -ForegroundColor White
Write-Host "  - Aspetta il messaggio 'Started CourseManagementApplication'" -ForegroundColor White

Write-Host "`nAvvio in corso..." -ForegroundColor Green
Write-Host "=" * 50 -ForegroundColor Gray

# Start Maven Spring Boot
try {
    & $mavenCmd spring-boot:run
} catch {
    Write-Host "`nErrore durante avvio: $_" -ForegroundColor Red
} finally {
    Write-Host "`n" + "=" * 50 -ForegroundColor Gray
    Write-Host "Backend fermato." -ForegroundColor Yellow
    Set-Location ".."
    Write-Host "Tornato alla cartella root." -ForegroundColor Green
}
