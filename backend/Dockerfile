# Multi-stage build for Spring Boot application
# Build multi-stage per applicazione Spring Boot
# Generated at: 2025-10-02 10:11:35

# Build stage / Fase di build
FROM maven:3.9.5-openjdk-17-slim AS build

# Set working directory / Imposta directory di lavoro
WORKDIR /app

# Copy pom.xml and download dependencies / Copia pom.xml e scarica dipendenze
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy source code / Copia codice sorgente
COPY src ./src

# Build application / Costruisci applicazione
RUN mvn clean package -DskipTests

# Runtime stage / Fase di runtime
FROM openjdk:17-jre-slim

# Set working directory / Imposta directory di lavoro
WORKDIR /app

# Copy built jar / Copia jar costruito
COPY --from=build /app/target/*.jar app.jar

# Expose port / Esponi porta
EXPOSE 8080

# Health check / Controllo salute
HEALTHCHECK --interval=30s --timeout=10s --start-period=60s --retries=3 \
  CMD curl -f http://localhost:8080/api/actuator/health || exit 1

# Run application / Esegui applicazione
ENTRYPOINT ["java", "-jar", "app.jar"]
