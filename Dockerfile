# Multi-stage build para optimizar la imagen
FROM maven:3.9.6-eclipse-temurin-17 AS builder

ENV TZ=America/Mexico_City

WORKDIR /app

# Copiar archivos de configuración de Maven
COPY pom.xml .
COPY .mvn .mvn
COPY mvnw .

# Descargar dependencias
RUN ./mvnw dependency:go-offline -B

# Copiar código fuente
COPY src ./src

# Compilar y empaquetar la aplicación
RUN ./mvnw clean package -DskipTests

# Etapa final - imagen runtime
FROM eclipse-temurin:17-jre-jammy

WORKDIR /app

# Crear usuario no-root para seguridad
RUN useradd -m -u 1000 appuser

# Copiar jar desde la etapa builder
COPY --from=builder /app/target/orderapp-*.jar app.jar

# Cambiar propietario del archivo
RUN chown -R appuser:appuser /app

# Cambiar al usuario no-root
USER appuser

# Exponer puerto
EXPOSE 8080

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=40s --retries=3 \
    CMD java -cp app.jar org.springframework.boot.loader.JarLauncher || exit 1

# Comando de inicio
CMD ["java", "-jar", "app.jar"]
