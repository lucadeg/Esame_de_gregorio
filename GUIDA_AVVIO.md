# 🚀 GUIDA AVVIO SISTEMA - COURSE MANAGEMENT

**Data**: 2025-01-27  
**Versione**: 1.0  
**Sistema**: Course Management System  

---

## 📋 **PREREQUISITI**

### ✅ **Software Richiesto**
- **Java 17+** (verificare con `java -version`)
- **Node.js 18+** (verificare con `node --version`)
- **Maven 3.9+** (verificare con `mvn --version`)

### ✅ **Verifica Prerequisiti**
```powershell
# Verifica Java
java -version

# Verifica Node.js
node --version

# Verifica Maven
mvn --version
```

---

## 🎯 **AVVIO RAPIDO (RACCOMANDATO)**

### **1. Avvia Backend**
```powershell
# Script automatico (pulito e affidabile)
.\scripts\start\start-backend.ps1
```

### **2. Avvia Frontend** (in nuovo terminale)
```powershell
# Script automatico
.\scripts\start\start-frontend.ps1
```

### **3. Testa il Sistema**
- **Frontend**: http://localhost:5173
- **Backend API**: http://localhost:8080/api
- **Swagger UI**: http://localhost:8080/api/swagger-ui.html
- **H2 Console**: http://localhost:8080/api/h2-console

---

## 🔧 **AVVIO MANUALE**

### **Backend (Spring Boot)**
```powershell
# 1. Vai nella cartella backend
cd backend

# 2. Compila il progetto
mvn clean compile

# 3. Avvia il backend
mvn spring-boot:run
```

### **Frontend (React)**
```powershell
# 1. Vai nella cartella frontend
cd frontend

# 2. Installa dipendenze (solo prima volta)
npm install

# 3. Avvia il frontend
npm run dev
```

---

## 🗄️ **CONFIGURAZIONE DATABASE**

### ✅ **H2 Database (Default)**
- **Tipo**: In-memory database
- **Configurazione**: Automatica
- **Console**: http://localhost:8080/api/h2-console
- **JDBC URL**: `jdbc:h2:mem:course_management`
- **Username**: `sa`
- **Password**: (vuoto)

### ✅ **PostgreSQL (Opzionale)**
```powershell
# Setup PostgreSQL locale
.\scripts\setup\setup-local-postgres.ps1

# Avvia con PostgreSQL
$env:DB_PASSWORD = "your_password"
.\scripts\start\start-backend.ps1
```

---

## 🧪 **TESTING**

### **Test Completi**
```powershell
# Esegui tutti i test (script pulito)
.\scripts\test\run-tests.ps1
```

### **Test Specifici**
```powershell
# Test Backend
.\scripts\test\test-backend.ps1

# Test Frontend
.\scripts\test\test-frontend.ps1
```

---

## 🐛 **RISOLUZIONE PROBLEMI**

### **Backend Non Si Avvia**
1. **Verifica Java**: `java -version`
2. **Verifica Maven**: `mvn --version`
3. **Pulisci progetto**: `mvn clean`
4. **Riavvia**: `mvn spring-boot:run`

### **Frontend Non Si Avvia**
1. **Verifica Node.js**: `node --version`
2. **Installa dipendenze**: `npm install`
3. **Pulisci cache**: `npm cache clean --force`
4. **Riavvia**: `npm run dev`

### **Errori di Connessione**
1. **Verifica porte**: 8080 (backend), 5173 (frontend)
2. **Chiudi altri processi**: `taskkill /f /im java.exe`
3. **Riavvia sistema**: Chiudi tutto e riavvia

---

## 📊 **MONITORAGGIO**

### **Health Check**
- **Backend**: http://localhost:8080/api/actuator/health
- **Database**: http://localhost:8080/api/actuator/health/db

### **Logs**
- **Backend**: Console del terminale
- **Frontend**: Console del browser (F12)

---

## 🎯 **URL IMPORTANTI**

| Servizio | URL | Descrizione |
|----------|-----|-------------|
| **Frontend** | http://localhost:5173 | Interfaccia utente |
| **Backend API** | http://localhost:8080/api | API REST |
| **Swagger UI** | http://localhost:8080/api/swagger-ui.html | Documentazione API |
| **H2 Console** | http://localhost:8080/api/h2-console | Database console |
| **Health Check** | http://localhost:8080/api/actuator/health | Stato sistema |

---

## 🔄 **WORKFLOW TIPICO**

### **1. Sviluppo**
```powershell
# Terminale 1: Backend
.\scripts\start\start-backend.ps1

# Terminale 2: Frontend
.\scripts\start\start-frontend.ps1
```

### **2. Testing**
```powershell
# Test completo
.\scripts\test\run-tests.ps1
```

### **3. Debug**
- **Backend**: Logs in console
- **Frontend**: Browser DevTools (F12)
- **Database**: H2 Console

---

## 📝 **NOTE IMPORTANTI**

### ✅ **Configurazione Corretta**
- **Database**: H2 (in-memory) di default
- **Porte**: 8080 (backend), 5173 (frontend)
- **Profili**: `h2` attivo di default

### ✅ **Scripts Disponibili**
- `start-backend.ps1`: Avvia backend
- `start-frontend.ps1`: Avvia frontend
- `run-tests.ps1`: Esegue test
- `setup-local-postgres.ps1`: Setup PostgreSQL

### ✅ **Troubleshooting**
- Se backend non si avvia: verifica Java e Maven
- Se frontend non si avvia: verifica Node.js e npm install
- Se errori di connessione: verifica porte libere

---

## 🏆 **STATO SISTEMA**

### ✅ **Funzionalità Complete**
- ✅ Backend Spring Boot con H2
- ✅ Frontend React con Vite
- ✅ API REST complete
- ✅ Database schema
- ✅ Test suite completa
- ✅ Documentazione Swagger

### ✅ **Pronto per**
- ✅ Sviluppo locale
- ✅ Testing
- ✅ Presentazione
- ✅ Demo

---

**🎯 Il sistema è COMPLETO e PRONTO per l'uso!**

*Ultima modifica: 2025-01-27*
