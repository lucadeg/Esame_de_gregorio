# Start All - Frontend and Backend
# Avvia Tutto - Frontend e Backend

Write-Host "=== AVVIO COMPLETO SISTEMA ===" -ForegroundColor Green
Write-Host "Data: $(Get-Date -Format 'yyyy-MM-dd HH:mm:ss')" -ForegroundColor Gray

# Check prerequisites
Write-Host "`n1. Verifica prerequisiti..." -ForegroundColor Yellow

# Check Java
Write-Host "  - Verifica Java..." -ForegroundColor Cyan
try {
    $javaVersion = java -version 2>&1
    if ($LASTEXITCODE -eq 0) {
        Write-Host "    Java trovato: $($javaVersion[0])" -ForegroundColor Green
    } else {
        Write-Host "    Java non trovato! Installa Java 17+" -ForegroundColor Red
        exit 1
    }
} catch {
    Write-Host "    Errore verifica Java: $_" -ForegroundColor Red
    exit 1
}

# Check Node.js
Write-Host "  - Verifica Node.js..." -ForegroundColor Cyan
try {
    $nodeVersion = node --version 2>&1
    if ($LASTEXITCODE -eq 0) {
        Write-Host "    Node.js trovato: $nodeVersion" -ForegroundColor Green
    } else {
        Write-Host "    Node.js non trovato! Installa Node.js 18+" -ForegroundColor Red
        exit 1
    }
} catch {
    Write-Host "    Errore verifica Node.js: $_" -ForegroundColor Red
    exit 1
}

# Check Maven
Write-Host "  - Verifica Maven..." -ForegroundColor Cyan
$mavenCmd = "mvn.cmd"
if (Test-Path "C:\maven\apache-maven-3.9.5\bin\mvn.cmd") {
    $mavenCmd = "C:\maven\apache-maven-3.9.5\bin\mvn.cmd"
        Write-Host "    Maven trovato: $mavenCmd" -ForegroundColor Green
} else {
    Write-Host "    Maven non trovato in percorso standard, uso: $mavenCmd" -ForegroundColor Yellow
}

try {
    $mavenVersion = & $mavenCmd --version 2>&1
    if ($LASTEXITCODE -eq 0) {
        Write-Host "    Maven funzionante: $($mavenVersion[0])" -ForegroundColor Green
    } else {
        Write-Host "    Maven non funzionante!" -ForegroundColor Red
        exit 1
    }
} catch {
    Write-Host "    Errore Maven: $_" -ForegroundColor Red
    exit 1
}

Write-Host "`n2. Preparazione backend..." -ForegroundColor Yellow
if (Test-Path "backend") {
    Set-Location "backend"
    Write-Host "  Cartella backend trovata" -ForegroundColor Green
    
    # Clean and compile backend
    Write-Host "  - Compilazione backend..." -ForegroundColor Cyan
    try {
        & $mavenCmd clean compile -q
        if ($LASTEXITCODE -eq 0) {
            Write-Host "    Backend compilato con successo" -ForegroundColor Green
        } else {
            Write-Host "    Compilazione backend fallita!" -ForegroundColor Red
            Set-Location ".."
            exit 1
        }
    } catch {
        Write-Host "    Errore compilazione backend: $_" -ForegroundColor Red
        Set-Location ".."
        exit 1
    }
} else {
    Write-Host "  Cartella backend non trovata!" -ForegroundColor Red
    exit 1
}

Write-Host "`n3. Preparazione frontend..." -ForegroundColor Yellow
Set-Location ".."
if (Test-Path "frontend") {
    Set-Location "frontend"
    Write-Host "  Cartella frontend trovata" -ForegroundColor Green
    
    # Install dependencies if needed
    if (-not (Test-Path "node_modules")) {
        Write-Host "  - Installazione dipendenze frontend..." -ForegroundColor Cyan
        try {
            npm install
            if ($LASTEXITCODE -eq 0) {
                Write-Host "    Dipendenze frontend installate" -ForegroundColor Green
            } else {
                Write-Host "    Installazione dipendenze fallita!" -ForegroundColor Red
                Set-Location ".."
                exit 1
            }
        } catch {
            Write-Host "    Errore installazione dipendenze: $_" -ForegroundColor Red
            Set-Location ".."
            exit 1
        }
    } else {
        Write-Host "  Dipendenze frontend già presenti" -ForegroundColor Green
    }
} else {
    Write-Host "  Cartella frontend non trovata!" -ForegroundColor Red
    exit 1
}

Write-Host "`n4. Avvio servizi..." -ForegroundColor Yellow
Set-Location ".."

# Start backend in new window
Write-Host "  - Avvio backend in nuova finestra..." -ForegroundColor Cyan
$backendScript = "scripts\start\start-backend-fixed.ps1"
if (Test-Path $backendScript) {
    Start-Process powershell -ArgumentList "-NoExit", "-Command", "& '$PWD\$backendScript'"
    Write-Host "    Backend avviato in nuova finestra" -ForegroundColor Green
} else {
    Write-Host "    Script backend non trovato: $backendScript" -ForegroundColor Red
    exit 1
}

# Wait a moment for backend to start
Write-Host "  - Attesa avvio backend (5 secondi)..." -ForegroundColor Cyan
Start-Sleep -Seconds 5

# Start frontend in new window
Write-Host "  - Avvio frontend in nuova finestra..." -ForegroundColor Cyan
$frontendScript = "scripts\start\start-frontend.ps1"
if (Test-Path $frontendScript) {
    Start-Process powershell -ArgumentList "-NoExit", "-Command", "& '$PWD\$frontendScript'"
    Write-Host "    Frontend avviato in nuova finestra" -ForegroundColor Green
} else {
    Write-Host "    Script frontend non trovato: $frontendScript" -ForegroundColor Red
    exit 1
}

Write-Host "`nSISTEMA AVVIATO CON SUCCESSO!" -ForegroundColor Green
Write-Host "=" * 60 -ForegroundColor Gray
Write-Host "URL IMPORTANTI:" -ForegroundColor Cyan
Write-Host "  Frontend: http://localhost:5173" -ForegroundColor White
Write-Host "  Backend API: http://localhost:8080/api" -ForegroundColor White
Write-Host "  Swagger Docs: http://localhost:8080/api/swagger-ui.html" -ForegroundColor White
Write-Host "  H2 Console: http://localhost:8080/api/h2-console" -ForegroundColor White
Write-Host "  Health Check: http://localhost:8080/api/actuator/health" -ForegroundColor White
Write-Host "=" * 60 -ForegroundColor Gray
Write-Host "`nNOTA:" -ForegroundColor Yellow
Write-Host "  - Backend e Frontend sono in finestre separate" -ForegroundColor White
Write-Host "  - Premi Ctrl+C nelle rispettive finestre per fermarli" -ForegroundColor White
Write-Host "  - Aspetta che entrambi siano completamente avviati prima di usare l'app" -ForegroundColor White
Write-Host "`nSistema pronto per l'uso!" -ForegroundColor Green
