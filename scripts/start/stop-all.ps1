# Stop All Services
# Ferma Tutti i Servizi

Write-Host "=== FERMATA SERVIZI ===" -ForegroundColor Red
Write-Host "Data: $(Get-Date -Format 'yyyy-MM-dd HH:mm:ss')" -ForegroundColor Gray

Write-Host "`n1. Fermata processi Node.js (Frontend)..." -ForegroundColor Yellow
try {
    $nodeProcesses = Get-Process -Name "node" -ErrorAction SilentlyContinue
    if ($nodeProcesses) {
        $nodeProcesses | Stop-Process -Force
        Write-Host "Processi Node.js fermati" -ForegroundColor Green
    } else {
        Write-Host "Nessun processo Node.js trovato" -ForegroundColor Cyan
    }
} catch {
    Write-Host "Errore fermata processi Node.js: $_" -ForegroundColor Yellow
}

Write-Host "`n2. Fermata processi Java (Backend)..." -ForegroundColor Yellow
try {
    $javaProcesses = Get-Process -Name "java" -ErrorAction SilentlyContinue
    if ($javaProcesses) {
        $javaProcesses | Stop-Process -Force
        Write-Host "Processi Java fermati" -ForegroundColor Green
    } else {
        Write-Host "Nessun processo Java trovato" -ForegroundColor Cyan
    }
} catch {
    Write-Host "Errore fermata processi Java: $_" -ForegroundColor Yellow
}

Write-Host "`n3. Fermata processi Maven..." -ForegroundColor Yellow
try {
    $mavenProcesses = Get-Process -Name "mvn" -ErrorAction SilentlyContinue
    if ($mavenProcesses) {
        $mavenProcesses | Stop-Process -Force
        Write-Host "Processi Maven fermati" -ForegroundColor Green
    } else {
        Write-Host "Nessun processo Maven trovato" -ForegroundColor Cyan
    }
} catch {
    Write-Host "Errore fermata processi Maven: $_" -ForegroundColor Yellow
}

Write-Host "`n4. Verifica porte..." -ForegroundColor Yellow
try {
    $port8080 = Get-NetTCPConnection -LocalPort 8080 -ErrorAction SilentlyContinue
    $port5173 = Get-NetTCPConnection -LocalPort 5173 -ErrorAction SilentlyContinue
    
    if ($port8080) {
        Write-Host "Porta 8080 ancora in uso (Backend)" -ForegroundColor Yellow
    } else {
        Write-Host "Porta 8080 libera" -ForegroundColor Green
    }
    
    if ($port5173) {
        Write-Host "Porta 5173 ancora in uso (Frontend)" -ForegroundColor Yellow
    } else {
        Write-Host "Porta 5173 libera" -ForegroundColor Green
    }
} catch {
    Write-Host "Impossibile verificare porte: $_" -ForegroundColor Cyan
}

Write-Host "`nFERMATA COMPLETATA!" -ForegroundColor Green
Write-Host "=" * 40 -ForegroundColor Gray
Write-Host "Tutti i servizi sono stati fermati." -ForegroundColor White
Write-Host "Per riavviare, usa: scripts\start\start-all.ps1" -ForegroundColor Cyan
