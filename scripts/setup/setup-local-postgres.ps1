# Script per configurare PostgreSQL locale
# Script to configure local PostgreSQL

Write-Host "=== CONFIGURAZIONE POSTGRESQL LOCALE ===" -ForegroundColor Green

# 1. Verifica servizi PostgreSQL
Write-Host "1. Verifico servizi PostgreSQL..." -ForegroundColor Yellow
$postgresServices = Get-Service -Name "postgresql*" -ErrorAction SilentlyContinue
if ($postgresServices) {
    foreach ($service in $postgresServices) {
        Write-Host "   Servizio: $($service.Name) - Stato: $($service.Status)" -ForegroundColor Green
        if ($service.Status -ne "Running") {
            Start-Service -Name $service.Name
            Write-Host "   Avviato servizio: $($service.Name)" -ForegroundColor Green
        }
    }
} else {
    Write-Host "   ERRORE: Nessun servizio PostgreSQL trovato" -ForegroundColor Red
    exit 1
}

# 2. Aggiungi PostgreSQL al PATH
Write-Host "2. Configuro PATH PostgreSQL..." -ForegroundColor Yellow
$postgresPaths = @(
    "C:\Program Files\PostgreSQL\15\bin",
    "C:\Program Files\PostgreSQL\17\bin",
    "C:\Program Files\PostgreSQL\16\bin"
)

$postgresPath = $null
foreach ($path in $postgresPaths) {
    if (Test-Path $path) {
        $postgresPath = $path
        break
    }
}

if ($postgresPath) {
    $env:PATH += ";$postgresPath"
    Write-Host "   PATH configurato: $postgresPath" -ForegroundColor Green
} else {
    Write-Host "   ERRORE: PostgreSQL bin non trovato nei path standard" -ForegroundColor Red
    Write-Host "   Cerca manualmente il path di PostgreSQL e aggiorna lo script" -ForegroundColor Yellow
    exit 1
}

# 3. Testa connessione PostgreSQL
Write-Host "3. Testo connessione PostgreSQL..." -ForegroundColor Yellow
try {
    $env:PGPASSWORD = $env:DB_PASSWORD
    psql -U postgres -h localhost -p 5432 -c "SELECT version();" 2>$null
    if ($LASTEXITCODE -eq 0) {
        Write-Host "   Connessione PostgreSQL riuscita" -ForegroundColor Green
    } else {
        Write-Host "   ERRORE: Connessione PostgreSQL fallita" -ForegroundColor Red
        Write-Host "   Verifica che PostgreSQL sia in esecuzione e la password sia corretta" -ForegroundColor Yellow
        exit 1
    }
} catch {
    Write-Host "   ERRORE: PostgreSQL non accessibile" -ForegroundColor Red
    exit 1
}

# 4. Crea database course_management
Write-Host "4. Creo database course_management..." -ForegroundColor Yellow
try {
    $env:PGPASSWORD = $env:DB_PASSWORD
    psql -U postgres -h localhost -p 5432 -c "CREATE DATABASE course_management;" 2>$null
    if ($LASTEXITCODE -eq 0) {
        Write-Host "   Database course_management creato" -ForegroundColor Green
    } else {
        Write-Host "   Database potrebbe già esistere" -ForegroundColor Yellow
    }
} catch {
    Write-Host "   ERRORE durante la creazione del database" -ForegroundColor Red
}

# 5. Applica schema database
Write-Host "5. Applico schema database..." -ForegroundColor Yellow
try {
    $env:PGPASSWORD = $env:DB_PASSWORD
    Get-Content "database\schema.sql" | psql -U postgres -h localhost -p 5432 -d course_management
    Write-Host "   Schema applicato con successo" -ForegroundColor Green
} catch {
    Write-Host "   ERRORE durante l'applicazione dello schema" -ForegroundColor Red
}

# 6. Verifica tabelle create
Write-Host "6. Verifico tabelle create..." -ForegroundColor Yellow
try {
    $env:PGPASSWORD = $env:DB_PASSWORD
    psql -U postgres -h localhost -p 5432 -d course_management -c "\dt" 2>$null
    Write-Host "   Tabelle verificate" -ForegroundColor Green
} catch {
    Write-Host "   ERRORE durante la verifica delle tabelle" -ForegroundColor Red
}

Write-Host "=== CONFIGURAZIONE COMPLETATA ===" -ForegroundColor Green
Write-Host "PostgreSQL locale configurato" -ForegroundColor Cyan
Write-Host "Database: course_management" -ForegroundColor Cyan
Write-Host "Porta: 5432" -ForegroundColor Cyan
Write-Host "Username: postgres" -ForegroundColor Cyan
