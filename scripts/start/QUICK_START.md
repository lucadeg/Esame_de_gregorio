# 🚀 Quick Start - Avvio Rapido

## Avvio Completo Sistema

```powershell
# Avvia tutto (Backend + Frontend)
.\scripts\start\start-all.ps1
```

## Avvio Singoli Servizi

```powershell
# Solo Backend
.\scripts\start\start-backend-fixed.ps1

# Solo Frontend  
.\scripts\start\start-frontend.ps1
```

## Test Sistema

```powershell
# Verifica che tutto funzioni
.\scripts\start\test-system.ps1
```

## Fermata Sistema

```powershell
# Ferma tutto
.\scripts\start\stop-all.ps1
```

## URL Importanti

- 🌐 **Frontend**: http://localhost:5173
- 🔧 **Backend API**: http://localhost:8080/api
- 📚 **Swagger**: http://localhost:8080/api/swagger-ui.html
- 🗄️ **H2 Console**: http://localhost:8080/api/h2-console
- ❤️ **Health**: http://localhost:8080/api/actuator/health

## Risoluzione Problemi

1. **Porte occupate**: `.\scripts\start\stop-all.ps1`
2. **Test sistema**: `.\scripts\start\test-system.ps1`
3. **Riavvio completo**: `stop-all.ps1` → `start-all.ps1`

## Prerequisiti

- Java 17+
- Node.js 18+
- Maven 3.6+
- PowerShell 5.1+
