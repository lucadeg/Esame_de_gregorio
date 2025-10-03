# Start Frontend - Enhanced Script
# Avvia Frontend - Script Migliorato

Write-Host "=== AVVIO FRONTEND ===" -ForegroundColor Green
Write-Host "Data: $(Get-Date -Format 'yyyy-MM-dd HH:mm:ss')" -ForegroundColor Gray

# Check Node.js
Write-Host "`n1. Verifica Node.js..." -ForegroundColor Yellow
try {
    $nodeVersion = node --version 2>&1
    if ($LASTEXITCODE -eq 0) {
        Write-Host "Node.js trovato: $nodeVersion" -ForegroundColor Green
    } else {
        Write-Host "Node.js non trovato! Installa Node.js 18+" -ForegroundColor Red
        exit 1
    }
} catch {
    Write-Host "Errore verifica Node.js: $_" -ForegroundColor Red
    exit 1
}

# Go to frontend directory
Write-Host "`n2. Navigazione cartella frontend..." -ForegroundColor Yellow
if (Test-Path "frontend") {
    Set-Location "frontend"
    Write-Host "Cartella frontend trovata" -ForegroundColor Green
} else {
    Write-Host "Cartella frontend non trovata!" -ForegroundColor Red
    exit 1
}

# Install dependencies if needed
Write-Host "`n3. Verifica dipendenze..." -ForegroundColor Yellow
if (-not (Test-Path "node_modules")) {
    Write-Host "  - Installazione dipendenze..." -ForegroundColor Cyan
    try {
        npm install
        if ($LASTEXITCODE -eq 0) {
            Write-Host "  Dipendenze installate con successo" -ForegroundColor Green
        } else {
            Write-Host "  Installazione dipendenze fallita!" -ForegroundColor Red
            Set-Location ".."
            exit 1
        }
    } catch {
        Write-Host "  Errore installazione dipendenze: $_" -ForegroundColor Red
        Set-Location ".."
        exit 1
    }
} else {
    Write-Host "  Dipendenze già presenti" -ForegroundColor Green
}

# Start frontend
Write-Host "`n4. Avvio frontend..." -ForegroundColor Yellow
Write-Host "Configurazione:" -ForegroundColor Cyan
Write-Host "  - Porta: 5173" -ForegroundColor Gray
Write-Host "  - URL: http://localhost:5173" -ForegroundColor Gray
Write-Host "  - Hot Reload: Abilitato" -ForegroundColor Gray

Write-Host "`nIMPORTANTE:" -ForegroundColor Yellow
Write-Host "  - Il frontend si avvierà in questa finestra" -ForegroundColor White
Write-Host "  - Premi Ctrl+C per fermarlo" -ForegroundColor White
Write-Host "  - Aspetta il messaggio 'Local: http://localhost:5173'" -ForegroundColor White

Write-Host "`nAvvio in corso..." -ForegroundColor Green
Write-Host "=" * 50 -ForegroundColor Gray

# Start frontend
try {
    npm run dev
} catch {
    Write-Host "`nErrore durante avvio: $_" -ForegroundColor Red
} finally {
    Write-Host "`n" + "=" * 50 -ForegroundColor Gray
    Write-Host "Frontend fermato." -ForegroundColor Yellow
    Set-Location ".."
    Write-Host "Tornato alla cartella root." -ForegroundColor Green
}
