# Run Tests - Clean and Simple Script
# Esegui Test - Script Pulito e Semplice

Write-Host "=== ESECUZIONE TEST ===" -ForegroundColor Green
Write-Host "Data: $(Get-Date -Format 'yyyy-MM-dd HH:mm:ss')" -ForegroundColor Gray

# Check Node.js
Write-Host "`n1. Verifica Node.js..." -ForegroundColor Yellow
try {
    $nodeVersion = node --version 2>&1
    if ($LASTEXITCODE -eq 0) {
        Write-Host "✅ Node.js trovato: $nodeVersion" -ForegroundColor Green
    } else {
        Write-Host "❌ Node.js non trovato! Installa Node.js 18+" -ForegroundColor Red
        exit 1
    }
} catch {
    Write-Host "❌ Errore verifica Node.js: $_" -ForegroundColor Red
    exit 1
}

# Install dependencies if needed
Write-Host "`n2. Verifica dipendenze..." -ForegroundColor Yellow
if (-not (Test-Path "node_modules")) {
    Write-Host "Installing dependencies..." -ForegroundColor Cyan
    npm install
    if ($LASTEXITCODE -ne 0) {
        Write-Host "❌ Installazione dipendenze fallita!" -ForegroundColor Red
        exit 1
    }
} else {
    Write-Host "✅ Dipendenze già installate" -ForegroundColor Green
}

# Install Playwright if needed
Write-Host "`n3. Verifica Playwright..." -ForegroundColor Yellow
if (-not (Test-Path "node_modules/@playwright")) {
    Write-Host "Installing Playwright..." -ForegroundColor Cyan
    npx playwright install
    if ($LASTEXITCODE -ne 0) {
        Write-Host "❌ Installazione Playwright fallita!" -ForegroundColor Red
        exit 1
    }
} else {
    Write-Host "✅ Playwright già installato" -ForegroundColor Green
}

# Run tests
Write-Host "`n4. Esecuzione test..." -ForegroundColor Yellow
Write-Host "Esecuzione: npx playwright test" -ForegroundColor Gray

try {
    npx playwright test
    if ($LASTEXITCODE -eq 0) {
        Write-Host "✅ Test completati con successo" -ForegroundColor Green
    } else {
        Write-Host "⚠️ Alcuni test sono falliti" -ForegroundColor Yellow
    }
} catch {
    Write-Host "❌ Errore durante esecuzione test: $_" -ForegroundColor Red
    exit 1
}

Write-Host "`n📊 Report test disponibile:" -ForegroundColor Cyan
Write-Host "  - HTML Report: npx playwright show-report" -ForegroundColor White
Write-Host "  - Test UI: npx playwright test --ui" -ForegroundColor White

Write-Host "`n✅ Test completati!" -ForegroundColor Green
