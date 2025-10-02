# Multi-stage build for React application with Vite
# Build multi-stage per applicazione React con Vite
# Generated at: 2025-10-02 10:11:35

# Build stage / Fase di build
FROM node:18-alpine AS build

# Set working directory / Imposta directory di lavoro
WORKDIR /app

# Copy package files / Copia file package
COPY package*.json ./

# Install dependencies / Installa dipendenze
RUN npm ci --only=production

# Copy source code / Copia codice sorgente
COPY . .

# Build application / Costruisci applicazione
RUN npm run build

# Production stage / Fase di produzione
FROM nginx:alpine

# Copy built files / Copia file costruiti
COPY --from=build /app/dist /usr/share/nginx/html

# Copy nginx configuration / Copia configurazione nginx
COPY nginx.conf /etc/nginx/nginx.conf

# Expose port / Esponi porta
EXPOSE 5173

# Start nginx / Avvia nginx
CMD ["nginx", "-g", "daemon off;"]
