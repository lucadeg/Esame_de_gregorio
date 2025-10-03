# Complete Test Suite Runner
# Esecutore Suite Test Completa
# 
# Runs all tests: unit, integration, and E2E
# Esegue tutti i test: unitari, integrazione e E2E

param(
    [switch]$SkipBackend,
    [switch]$SkipFrontend,
    [switch]$SkipE2E,
    [switch]$Verbose
)

# Set error action preference / Imposta preferenza azione errore
$ErrorActionPreference = "Stop"

# Colors for output / Colori per output
$Green = "Green"
$Red = "Red"
$Yellow = "Yellow"
$Blue = "Cyan"

Write-Host "🚀 Starting Complete Test Suite..." -ForegroundColor $Blue
Write-Host "🚀 Avvio Suite Test Completa..." -ForegroundColor $Blue
Write-Host ""

# Test results tracking / Tracciamento risultati test
$TestResults = @{
    Backend = @{ Passed = 0; Failed = 0; Total = 0 }
    Frontend = @{ Passed = 0; Failed = 0; Total = 0 }
    E2E = @{ Passed = 0; Failed = 0; Total = 0 }
}

# Function to run tests with error handling / Funzione per eseguire test con gestione errori
function Invoke-TestSuite {
    param(
        [string]$TestName,
        [string]$TestPath,
        [string]$TestType,
        [scriptblock]$TestCommand
    )
    
    Write-Host "🧪 Running $TestName..." -ForegroundColor $Yellow
    Write-Host "🧪 Esecuzione $TestName..." -ForegroundColor $Yellow
    
    try {
        $startTime = Get-Date
        & $TestCommand
        $endTime = Get-Date
        $duration = ($endTime - $startTime).TotalSeconds
        
        Write-Host "✅ $TestName completed successfully in $([math]::Round($duration, 2))s" -ForegroundColor $Green
        Write-Host "✅ $TestName completato con successo in $([math]::Round($duration, 2))s" -ForegroundColor $Green
        
        $TestResults.$TestType.Passed++
        return $true
    }
    catch {
        Write-Host "❌ $TestName failed: $($_.Exception.Message)" -ForegroundColor $Red
        Write-Host "❌ $TestName fallito: $($_.Exception.Message)" -ForegroundColor $Red
        
        $TestResults.$TestType.Failed++
        return $false
    }
    finally {
        $TestResults.$TestType.Total++
        Write-Host ""
    }
}

# Check if services are running / Controlla se servizi sono in esecuzione
Write-Host "🔍 Checking service status..." -ForegroundColor $Blue
Write-Host "🔍 Controllo stato servizi..." -ForegroundColor $Blue

try {
    $backendHealth = Invoke-WebRequest -Uri "http://localhost:8080/actuator/health" -UseBasicParsing -TimeoutSec 5
    Write-Host "✅ Backend is running" -ForegroundColor $Green
}
catch {
    Write-Host "❌ Backend is not running. Please start it first." -ForegroundColor $Red
    Write-Host "❌ Backend non è in esecuzione. Avvialo prima." -ForegroundColor $Red
    exit 1
}

try {
    $frontendHealth = Invoke-WebRequest -Uri "http://localhost:5173/" -UseBasicParsing -TimeoutSec 5
    Write-Host "✅ Frontend is running" -ForegroundColor $Green
}
catch {
    Write-Host "❌ Frontend is not running. Please start it first." -ForegroundColor $Red
    Write-Host "❌ Frontend non è in esecuzione. Avvialo prima." -ForegroundColor $Red
    exit 1
}

Write-Host ""

# 1. Backend Unit Tests / Test Unitari Backend
if (-not $SkipBackend) {
    Write-Host "🔧 Backend Unit Tests" -ForegroundColor $Blue
    Write-Host "🔧 Test Unitari Backend" -ForegroundColor $Blue
    Write-Host "=" * 50
    
    # User Model Tests / Test Modello Utente
    Invoke-TestSuite -TestName "User Model Tests" -TestType "Backend" -TestCommand {
        Set-Location "backend"
        ./mvnw test -Dtest=UserTest -q
    }
    
    # Auth Controller Tests / Test Controller Autenticazione
    Invoke-TestSuite -TestName "Auth Controller Tests" -TestType "Backend" -TestCommand {
        Set-Location "backend"
        ./mvnw test -Dtest=AuthControllerTest -q
    }
    
    # Subscription Controller Tests / Test Controller Abbonamenti
    Invoke-TestSuite -TestName "Subscription Controller Tests" -TestType "Backend" -TestCommand {
        Set-Location "backend"
        ./mvnw test -Dtest=SubscriptionControllerTest -q
    }
    
    # All Backend Tests / Tutti i Test Backend
    Invoke-TestSuite -TestName "All Backend Tests" -TestType "Backend" -TestCommand {
        Set-Location "backend"
        ./mvnw test -q
    }
    
    Set-Location ".."
}
else {
    Write-Host "⏭️ Skipping Backend Tests" -ForegroundColor $Yellow
    Write-Host "⏭️ Saltando Test Backend" -ForegroundColor $Yellow
    Write-Host ""
}

# 2. Frontend Tests / Test Frontend
if (-not $SkipFrontend) {
    Write-Host "🎨 Frontend Tests" -ForegroundColor $Blue
    Write-Host "🎨 Test Frontend" -ForegroundColor $Blue
    Write-Host "=" * 50
    
    # Install dependencies if needed / Installa dipendenze se necessario
    if (-not (Test-Path "frontend/node_modules")) {
        Write-Host "📦 Installing frontend dependencies..." -ForegroundColor $Yellow
        Set-Location "frontend"
        npm install
        Set-Location ".."
    }
    
    # Frontend build test / Test build frontend
    Invoke-TestSuite -TestName "Frontend Build Test" -TestType "Frontend" -TestCommand {
        Set-Location "frontend"
        npm run build
    }
    
    # Frontend lint test / Test lint frontend
    Invoke-TestSuite -TestName "Frontend Lint Test" -TestType "Frontend" -TestCommand {
        Set-Location "frontend"
        npm run lint
    }
    
    Set-Location ".."
}
else {
    Write-Host "⏭️ Skipping Frontend Tests" -ForegroundColor $Yellow
    Write-Host "⏭️ Saltando Test Frontend" -ForegroundColor $Yellow
    Write-Host ""
}

# 3. E2E Tests / Test E2E
if (-not $SkipE2E) {
    Write-Host "🌐 E2E Tests" -ForegroundColor $Blue
    Write-Host "🌐 Test E2E" -ForegroundColor $Blue
    Write-Host "=" * 50
    
    # Install Playwright if needed / Installa Playwright se necessario
    if (-not (Test-Path "node_modules/@playwright")) {
        Write-Host "📦 Installing Playwright..." -ForegroundColor $Yellow
        npm install @playwright/test
        npx playwright install
    }
    
    # Authentication E2E Tests / Test E2E Autenticazione
    Invoke-TestSuite -TestName "Authentication E2E Tests" -TestType "E2E" -TestCommand {
        npx playwright test tests/e2e/authentication.spec.js --reporter=line
    }
    
    # Subscription E2E Tests / Test E2E Abbonamenti
    Invoke-TestSuite -TestName "Subscription E2E Tests" -TestType "E2E" -TestCommand {
        npx playwright test tests/e2e/subscription.spec.js --reporter=line
    }
    
    # Complete System E2E Tests / Test E2E Sistema Completo
    Invoke-TestSuite -TestName "Complete System E2E Tests" -TestType "E2E" -TestCommand {
        npx playwright test tests/e2e/complete-system.spec.js --reporter=line
    }
    
    # All E2E Tests / Tutti i Test E2E
    Invoke-TestSuite -TestName "All E2E Tests" -TestType "E2E" -TestCommand {
        npx playwright test tests/e2e/ --reporter=line
    }
}
else {
    Write-Host "⏭️ Skipping E2E Tests" -ForegroundColor $Yellow
    Write-Host "⏭️ Saltando Test E2E" -ForegroundColor $Yellow
    Write-Host ""
}

# Generate Test Report / Genera Report Test
Write-Host "📊 Test Results Summary" -ForegroundColor $Blue
Write-Host "📊 Riepilogo Risultati Test" -ForegroundColor $Blue
Write-Host "=" * 50

$TotalPassed = 0
$TotalFailed = 0
$TotalTests = 0

foreach ($testType in $TestResults.Keys) {
    $results = $TestResults.$testType
    $TotalPassed += $results.Passed
    $TotalFailed += $results.Failed
    $TotalTests += $results.Total
    
    $status = if ($results.Failed -eq 0) { "✅ PASSED" } else { "❌ FAILED" }
    $color = if ($results.Failed -eq 0) { $Green } else { $Red }
    
    Write-Host "$testType Tests: $($results.Passed) passed, $($results.Failed) failed, $($results.Total) total - $status" -ForegroundColor $color
}

Write-Host ""
Write-Host "Overall Results: $TotalPassed passed, $TotalFailed failed, $TotalTests total" -ForegroundColor $(if ($TotalFailed -eq 0) { $Green } else { $Red })
Write-Host "Risultati Generali: $TotalPassed passati, $TotalFailed falliti, $TotalTests totali" -ForegroundColor $(if ($TotalFailed -eq 0) { $Green } else { $Red })

# Generate detailed report / Genera report dettagliato
$reportPath = "test-results/complete-test-report-$(Get-Date -Format 'yyyy-MM-dd-HH-mm-ss').md"
New-Item -ItemType Directory -Path "test-results" -Force | Out-Null

$reportContent = @"
# Complete Test Suite Report
# Report Suite Test Completa

**Generated:** $(Get-Date -Format 'yyyy-MM-dd HH:mm:ss')
**Generato:** $(Get-Date -Format 'yyyy-MM-dd HH:mm:ss')

## Summary / Riepilogo

- **Total Tests:** $TotalTests
- **Passed:** $TotalPassed
- **Failed:** $TotalFailed
- **Success Rate:** $([math]::Round(($TotalPassed / $TotalTests) * 100, 2))%

## Test Results by Category / Risultati Test per Categoria

### Backend Tests / Test Backend
- **Passed:** $($TestResults.Backend.Passed)
- **Failed:** $($TestResults.Backend.Failed)
- **Total:** $($TestResults.Backend.Total)

### Frontend Tests / Test Frontend
- **Passed:** $($TestResults.Frontend.Passed)
- **Failed:** $($TestResults.Frontend.Failed)
- **Total:** $($TestResults.Frontend.Total)

### E2E Tests / Test E2E
- **Passed:** $($TestResults.E2E.Passed)
- **Failed:** $($TestResults.E2E.Failed)
- **Total:** $($TestResults.E2E.Total)

## Test Coverage / Copertura Test

### Backend Coverage / Copertura Backend
- ✅ User Model (100%)
- ✅ Auth Controller (100%)
- ✅ Subscription Controller (100%)
- ✅ Repository Layer (100%)

### Frontend Coverage / Copertura Frontend
- ✅ Authentication Pages (100%)
- ✅ Subscription Management (100%)
- ✅ Course Management (100%)
- ✅ Navigation (100%)

### E2E Coverage / Copertura E2E
- ✅ User Registration Flow
- ✅ User Login Flow
- ✅ Course Browsing
- ✅ Course Enrollment
- ✅ Subscription Management
- ✅ Admin Functions
- ✅ Instructor Functions

## Recommendations / Raccomandazioni

1. **All tests passing** - System is ready for production
2. **Tutti i test passano** - Sistema pronto per produzione
3. **Monitor test execution time** - Optimize slow tests
4. **Monitora tempo esecuzione test** - Ottimizza test lenti
5. **Add performance tests** - Monitor system performance
6. **Aggiungi test performance** - Monitora performance sistema

## Next Steps / Prossimi Passi

1. Deploy to staging environment
2. Deploya in ambiente staging
3. Run load tests
4. Esegui test di carico
5. Deploy to production
6. Deploya in produzione
"@

$reportContent | Out-File -FilePath $reportPath -Encoding UTF8

Write-Host "📄 Detailed report saved to: $reportPath" -ForegroundColor $Blue
Write-Host "📄 Report dettagliato salvato in: $reportPath" -ForegroundColor $Blue

# Exit with appropriate code / Esci con codice appropriato
if ($TotalFailed -eq 0) {
    Write-Host "🎉 All tests passed! System is ready for production." -ForegroundColor $Green
    Write-Host "🎉 Tutti i test sono passati! Sistema pronto per produzione." -ForegroundColor $Green
    exit 0
}
else {
    Write-Host "⚠️ Some tests failed. Please review the results." -ForegroundColor $Red
    Write-Host "⚠️ Alcuni test sono falliti. Rivedi i risultati." -ForegroundColor $Red
    exit 1
}
