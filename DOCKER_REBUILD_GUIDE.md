# 🐳 Guía: Reconstruir Imagen Docker Después de Cambios

## 📝 Resumen

Cuando haces cambios en el código Java, necesitas **reconstruir la imagen Docker** para que se reflejen en el contenedor. Esta guía te muestra los mejores comandos y prácticas.

---

## 🔄 Tipos de Cambios y Cómo Manejarlos

### 1️⃣ Cambios en Código Java (**.java files**)

**Qué cambió:** Lógica de negocio, controladores, servicios, repositorios

**Qué hacer:**

```bash
# Opción 1: Reconstruir y reiniciar (Recomendado)
docker-compose build --no-cache
docker-compose restart

# Opción 2: Más agresivo (elimina todo)
docker-compose down
docker-compose build
docker-compose up -d
```

### 2️⃣ Cambios en Configuración (**.yml, .properties**)

**Qué cambió:** Perfiles, variables de entorno, puertos

**Qué hacer:**

```bash
# Basta con reiniciar
docker-compose restart orderapp

# O si cambió el puerto:
docker-compose down
docker-compose up -d
```

### 3️⃣ Cambios en .env

**Qué cambió:** Variables de entorno

**Qué hacer:**

```bash
# Reiniciar y aplica nuevas variables
docker-compose restart
```

### 4️⃣ Cambios en Dockerfile

**Qué cambió:** Imagen base, dependencias del sistema

**Qué hacer:**

```bash
# DEBE reconstruir sin cache
docker-compose build --no-cache
docker-compose up -d
```

---

## 🚀 Comandos Principales

### Opción A: Build Rápido (Con Cache)

```bash
# Mejor rendimiento, usa cache anterior
docker-compose build
docker-compose restart
```

**Ventajas:**

- ⚡ Muy rápido (segundos)
- 💾 Reutiliza capas anteriores

**Desventajas:**

- ❌ Podría no incluir cambios recientes

**Cuándo usar:**

- Cambios menores en código
- Cambios en configuración

---

### Opción B: Build Completo (Sin Cache)

```bash
# Reconstruye desde cero, sin reutilizar nada
docker-compose build --no-cache
docker-compose up -d
```

**Ventajas:**

- ✅ Garantiza inclusión de todos los cambios
- ✅ Limpia capas antiguas

**Desventajas:**

- 🐌 Más lento (2-3 minutos)
- 📦 Descarga todo de nuevo

**Cuándo usar:**

- Cambios importantes en Java
- Después de actualizar dependencias
- Cuando algo no funciona

---

### Opción C: Rebuild y Force Recreate

```bash
# Reconstruye y fuerza recreación de contenedores
docker-compose up -d --build --force-recreate
```

**Ventajas:**

- ✅ Reconstruye si hay cambios
- ✅ Recrea contenedores
- ✅ Todo en un comando

**Cuándo usar:**

- Workflow rápido de desarrollo
- Después de cambios importantes

---

## 📋 Flujo de Trabajo Recomendado

### Para Desarrollo Local

```bash
# 1. Haces cambios en el código
# (editas .java, .yml, etc.)

# 2. Reconstruye rápido con cache
docker-compose build
docker-compose restart

# 3. Revisa los logs
docker-compose logs -f orderapp

# 4. Prueba en Swagger
# http://localhost:8080/swagger-ui.html
```

### Para Cambios Importantes

```bash
# 1. Cambios significativos
# (actualizaste dependencias, cambiaste Dockerfile)

# 2. Reconstruye sin cache
docker-compose build --no-cache
docker-compose down
docker-compose up -d

# 3. Espera a que inicie
sleep 40

# 4. Verifica
docker-compose ps
docker-compose logs orderapp
```

---

## ⚡ Comandos Optimizados por Escenario

### Escenario 1: Cambio Rápido de Código Java

```bash
# RÁPIDO - 30 segundos
./mvnw clean package -DskipTests -q && \
docker-compose build && \
docker-compose restart orderapp && \
docker-compose logs -f orderapp
```

**Desglose:**

- `mvnw clean package` - Compila el JAR
- `docker-compose build` - Reconstruye con cache
- `docker-compose restart orderapp` - Reinicia la app
- `docker-compose logs -f orderapp` - Muestra logs

---

### Escenario 2: Cambio en Dockerfile o Dependencias

```bash
# COMPLETO - 3 minutos
docker-compose build --no-cache && \
docker-compose down && \
docker-compose up -d && \
echo "Esperando 40 segundos..." && \
sleep 40 && \
docker-compose ps && \
docker-compose logs orderapp | tail -20
```

**Desglose:**

- `build --no-cache` - Sin cache
- `down` - Detiene y elimina
- `up -d` - Inicia de nuevo
- `sleep 40` - Espera startup
- `ps` - Muestra estado
- `logs` - Últimas 20 líneas

---

### Escenario 3: Desarrollo Iterativo Rápido

```bash
# ULTRA RÁPIDO - Con alias bash/PowerShell
alias docker-rebuild='docker-compose build && docker-compose restart orderapp'
alias docker-logs='docker-compose logs -f orderapp'
alias docker-status='docker-compose ps'

# Uso:
docker-rebuild
docker-logs
```

---

## 🏗️ Proceso Paso a Paso

### 1. Haces cambios

```bash
# Ejemplo: Cambias OrderRepository.java
nano src/main/java/.../OrderRepository.java
```

### 2. Opción A: Build Rápido

```bash
# Si son cambios menores
docker-compose build
docker-compose restart orderapp
```

**Tiempo:** ~30 segundos

### 3. Opción B: Build Completo

```bash
# Si son cambios importantes
docker-compose build --no-cache
docker-compose up -d
```

**Tiempo:** ~2-3 minutos

### 4. Verifica

```bash
# Esperar a que inicie
sleep 40

# Ver estado
docker-compose ps

# Ver logs
docker-compose logs orderapp

# Prueba en navegador
# http://localhost:8080/swagger-ui.html
```

---

## 🎯 Decisión Rápida: ¿Qué comando usar?

```
¿Qué cambió?
├─ Código Java (.java)
│  └─ docker-compose build && docker-compose restart
├─ Dockerfile
│  └─ docker-compose build --no-cache && docker-compose up -d
├─ Dependencias (pom.xml)
│  └─ docker-compose build --no-cache && docker-compose up -d
├─ Configuración (.yml, .env)
│  └─ docker-compose restart
└─ Deseguro: "No sé"
   └─ docker-compose build --no-cache && docker-compose up -d
```

---

## 📊 Comparativa de Comandos

| Comando            | Cache | Tiempo | Caso de Uso         |
| ------------------ | ----- | ------ | ------------------- |
| `build`            | ✅ Sí | 30s    | Cambios código      |
| `build --no-cache` | ❌ No | 2-3m   | Cambios importantes |
| `up -d --build`    | ✅ Sí | 30s    | Desarrollo          |
| `rebuild` (custom) | ❌ No | 2-3m   | Asegurado           |

---

## 🔍 Verificar que la Nueva Imagen Está Activa

```bash
# Ver imágenes
docker images | grep orderapp

# Ver contenedor corriendo
docker ps | grep orderapp

# Ver logs detallados
docker-compose logs orderapp

# Verificar con curl
curl http://localhost:8080/swagger-ui.html

# Test en Swagger
# http://localhost:8080/swagger-ui.html
```

---

## ⚠️ Problemas Comunes y Soluciones

### Problema 1: "Port already in use"

```bash
# Solución: Forcefully stop and recreate
docker-compose down
docker-compose up -d
```

### Problema 2: "Cache viejo, cambios no reflejados"

```bash
# Solución: Reconstruir sin cache
docker-compose build --no-cache
```

### Problema 3: "Out of memory"

```bash
# Ver uso de espacio
docker system df

# Limpiar recursos no usados
docker system prune -a

# Luego reconstruir
docker-compose build --no-cache
```

### Problema 4: "Application took too long to start"

```bash
# Esperar más y revisar logs
docker-compose logs orderapp

# Si hay error, reconstruir sin cache
docker-compose build --no-cache
docker-compose up -d
sleep 60  # Esperar 60 segundos
docker-compose logs orderapp
```

---

## 🎓 Script Completo Reutilizable

### Para PowerShell (Windows)

```powershell
# rebuild.ps1
param(
    [Parameter(Mandatory=$false)]
    [string]$Type = "fast"  # fast o full
)

Write-Host "🐳 Docker Rebuild Script" -ForegroundColor Green

if ($Type -eq "fast") {
    Write-Host "Mode: RÁPIDO" -ForegroundColor Yellow
    & docker-compose build
    & docker-compose restart orderapp
} else {
    Write-Host "Mode: COMPLETO" -ForegroundColor Yellow
    & docker-compose build --no-cache
    & docker-compose down
    & docker-compose up -d
    Write-Host "Esperando 40 segundos..." -ForegroundColor Cyan
    Start-Sleep -Seconds 40
}

Write-Host "Status:" -ForegroundColor Green
& docker-compose ps

Write-Host "Logs:" -ForegroundColor Green
& docker-compose logs orderapp | Select-Object -Last 20
```

**Uso:**

```powershell
# Rápido
.\rebuild.ps1 -Type fast

# Completo
.\rebuild.ps1 -Type full
```

### Para Bash/Linux (VPS)

```bash
#!/bin/bash
# rebuild.sh

TYPE=${1:-fast}  # Por defecto "fast"

echo "🐳 Docker Rebuild Script"

if [ "$TYPE" = "fast" ]; then
    echo "Mode: RÁPIDO"
    docker-compose build
    docker-compose restart orderapp
else
    echo "Mode: COMPLETO"
    docker-compose build --no-cache
    docker-compose down
    docker-compose up -d
    echo "Esperando 40 segundos..."
    sleep 40
fi

echo "Status:"
docker-compose ps

echo "Logs (últimas 20 líneas):"
docker-compose logs orderapp | tail -20
```

**Uso:**

```bash
chmod +x rebuild.sh

# Rápido
./rebuild.sh fast

# Completo
./rebuild.sh full
```

---

## 📈 Optimización: Parallelizar Procesos

### Build más rápido compilando primero

```bash
# Compilar mientras Docker se inicia
./mvnw clean package -DskipTests -q & \
docker-compose build & \
wait

# Luego reiniciar
docker-compose restart orderapp
```

### Usar BuildKit para builds más rápidos

```bash
# Habilitar BuildKit (Docker 18.09+)
export DOCKER_BUILDKIT=1

# Ahora todos los builds serán más rápidos
docker-compose build
```

---

## 🎯 Resumen de Comandos Esenciales

```bash
# 1. Build rápido (para desarrollo)
docker-compose build
docker-compose restart

# 2. Build completo (cuando algo falla)
docker-compose build --no-cache
docker-compose up -d

# 3. Ver estado
docker-compose ps

# 4. Ver logs
docker-compose logs -f orderapp

# 5. Limpiar todo
docker-compose down -v
docker system prune -a

# 6. Reconstruir y probar
docker-compose up -d --build
curl http://localhost:8080/swagger-ui.html
```

---

## ✅ Checklist: Después de Reconstruir

- [ ] Ejecutar `docker-compose build`
- [ ] Ejecutar `docker-compose restart` o `docker-compose up -d`
- [ ] Esperar 30-40 segundos
- [ ] Verificar `docker-compose ps` (status "Up")
- [ ] Revisar logs: `docker-compose logs orderapp`
- [ ] Probar en navegador: http://localhost:8080/swagger-ui.html
- [ ] Probar un endpoint: `curl http://localhost:8080/api/orders`
- [ ] ✅ Todo bien

---

## 🚀 Comando Final Recomendado

```bash
# Todo en uno: compilar, reconstruir, reiniciar y verificar
./mvnw clean package -DskipTests -q && \
docker-compose up -d --build && \
sleep 40 && \
docker-compose ps && \
echo "✅ Listo. Accede a: http://localhost:8080/swagger-ui.html"
```

---

**Guardado:** Noviembre 2024  
**Status:** ✅ Producción Ready
