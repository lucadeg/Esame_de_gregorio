# Script di Avvio Sistema
# System Launch Scripts

Questa directory contiene gli script PowerShell per gestire l'avvio e la fermata del sistema completo.

## Script Disponibili

### 🚀 Avvio Sistema

#### `start-all.ps1` - Avvia Tutto
Script principale che avvia sia frontend che backend in finestre separate.

```powershell
.\scripts\start\start-all.ps1
```

**Cosa fa:**
- Verifica prerequisiti (Java, Node.js, Maven)
- Compila il backend
- Installa dipendenze frontend se necessario
- Avvia backend in nuova finestra
- Avvia frontend in nuova finestra
- Mostra URL importanti

#### `start-backend-fixed.ps1` - Solo Backend
Avvia solo il backend Spring Boot.

```powershell
.\scripts\start\start-backend-fixed.ps1
```

**Configurazione:**
- Database: H2 (in-memory)
- Porta: 8080
- Profilo: h2

#### `start-frontend.ps1` - Solo Frontend
Avvia solo il frontend React.

```powershell
.\scripts\start\start-frontend.ps1
```

**Configurazione:**
- Porta: 5173
- Hot Reload: Abilitato

### 🛑 Fermata Sistema

#### `stop-all.ps1` - Ferma Tutto
Ferma tutti i processi del sistema.

```powershell
.\scripts\start\stop-all.ps1
```

**Cosa fa:**
- Ferma processi Node.js (Frontend)
- Ferma processi Java (Backend)
- Ferma processi Maven
- Verifica che le porte siano libere

### 🧪 Test Sistema

#### `test-system.ps1` - Test Sistema
Verifica che tutti i servizi funzionino correttamente.

```powershell
.\scripts\start\test-system.ps1
```

**Test eseguiti:**
- Connessione Backend (porta 8080)
- Connessione Frontend (porta 5173)
- API Endpoints (/api/courses, /api/enrollments)
- Swagger Documentation
- H2 Console

## URL Importanti

Dopo l'avvio del sistema, questi sono gli URL disponibili:

### 🌐 Frontend
- **Applicazione**: http://localhost:5173
- **Hot Reload**: Abilitato automaticamente

### 🔧 Backend API
- **API Base**: http://localhost:8080/api
- **Health Check**: http://localhost:8080/api/actuator/health
- **Swagger UI**: http://localhost:8080/api/swagger-ui.html
- **H2 Console**: http://localhost:8080/api/h2-console

## Prerequisiti

### Software Richiesto
- **Java 17+** - Per il backend Spring Boot
- **Node.js 18+** - Per il frontend React
- **Maven 3.6+** - Per la build del backend
- **PowerShell 5.1+** - Per eseguire gli script

### Verifica Prerequisiti
```powershell
# Verifica Java
java -version

# Verifica Node.js
node --version

# Verifica Maven
mvn --version
```

## Risoluzione Problemi

### Backend Non Si Avvia
1. Verifica che Java sia installato e nella PATH
2. Verifica che Maven sia installato e nella PATH
3. Controlla che la porta 8080 sia libera
4. Esegui `mvn clean compile` nella cartella backend

### Frontend Non Si Avvia
1. Verifica che Node.js sia installato e nella PATH
2. Controlla che la porta 5173 sia libera
3. Esegui `npm install` nella cartella frontend

### Porte Occupate
Se le porte sono occupate, usa:
```powershell
# Ferma tutto
.\scripts\start\stop-all.ps1

# Oppure ferma manualmente i processi
Get-Process -Name "java" | Stop-Process -Force
Get-Process -Name "node" | Stop-Process -Force
```

## Workflow Consigliato

### Sviluppo
1. **Avvio completo**: `.\scripts\start\start-all.ps1`
2. **Test sistema**: `.\scripts\start\test-system.ps1`
3. **Sviluppo**: Modifica il codice, hot reload automatico
4. **Fermata**: `.\scripts\start\stop-all.ps1`

### Debug
1. **Solo backend**: `.\scripts\start\start-backend-fixed.ps1`
2. **Solo frontend**: `.\scripts\start\start-frontend.ps1`
3. **Test specifico**: `.\scripts\start\test-system.ps1`

## Note Importanti

- ⚠️ **Non chiudere le finestre** dei servizi durante lo sviluppo
- 🔄 **Hot reload** è abilitato per il frontend
- 📝 **Log** sono visibili nelle finestre dei servizi
- 🛑 **Ctrl+C** per fermare i servizi nelle rispettive finestre
- 🔧 **Configurazione** è in `backend/src/main/resources/application.yml`

## Supporto

Se hai problemi:
1. Controlla i log nelle finestre dei servizi
2. Esegui il test sistema: `.\scripts\start\test-system.ps1`
3. Verifica i prerequisiti
4. Riavvia tutto: `.\scripts\start\stop-all.ps1` → `.\scripts\start\start-all.ps1`
