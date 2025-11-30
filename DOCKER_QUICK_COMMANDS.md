# ⚡ Comandos Rápidos - Docker Rebuild Optimizados

## 🎯 Tl;dr - Comandos Esenciales

### Después de cambios en código Java

```bash
# Opción 1: Rápido (30 segundos)
docker-compose build
docker-compose restart orderapp

# Opción 2: Más agresivo
docker-compose up -d --build
```

### Después de cambios en Dockerfile

```bash
# Sin cache (3 minutos)
docker-compose build --no-cache
docker-compose down
docker-compose up -d
```

### Scripts Listos para Usar

**Windows PowerShell:**

```powershell
.\rebuild.ps1 -Type fast   # Rápido
.\rebuild.ps1 -Type full   # Completo
```

**Linux/MacOS/VPS:**

```bash
chmod +x rebuild.sh
./rebuild.sh fast   # Rápido
./rebuild.sh full   # Completo
```

---

## 📋 Matriz: Qué Comando Usar

```
CAMBIO                              COMANDO
────────────────────────────────────────────────────────────────
Código Java (.java)                 docker-compose build && restart
Configuración (.yml, .env)          docker-compose restart
Dockerfile                          docker-compose build --no-cache && up
Dependencias (pom.xml)              docker-compose build --no-cache && up
```

---

## 🚀 Comandos por Escenario

### Escenario 1: Desarrollo Rápido

```bash
# Hiciste cambio en Java, necesitas ver resultado ASAP
docker-compose build && docker-compose restart orderapp && docker-compose logs -f
```

### Escenario 2: Cambio en Dependencias

```bash
# Actualizaste pom.xml, necesitas reconstruir todo
docker-compose build --no-cache && docker-compose down && docker-compose up -d
```

### Escenario 3: Algo No Funciona

```bash
# No sabes qué pasó, reconstruye de cero
docker-compose down -v
docker system prune -a
docker-compose build --no-cache
docker-compose up -d
```

### Escenario 4: Verificar Estado

```bash
# Ver qué está corriendo y logs
docker-compose ps
docker-compose logs orderapp | tail -50
```

---

## 🔥 Comandos One-Liner (Copia y Pega)

### Build Rápido

```bash
docker-compose build && docker-compose restart orderapp && sleep 5 && docker-compose ps
```

### Build Completo

```bash
docker-compose build --no-cache && docker-compose down && docker-compose up -d && sleep 40 && docker-compose ps && docker-compose logs orderapp | tail -20
```

### Compilar + Reconstruir + Probar

```bash
./mvnw clean package -DskipTests -q && docker-compose up -d --build && sleep 40 && curl http://localhost:8080/swagger-ui.html
```

### Todo: Limpiar, Compilar, Reconstruir

```bash
docker-compose down -v && docker system prune -a -f && ./mvnw clean package -DskipTests -q && docker-compose up -d --build && sleep 40 && docker-compose ps
```

---

## 📊 Comparativa de Velocidad

| Comando                           | Tiempo | Cuándo usar          |
| --------------------------------- | ------ | -------------------- |
| `docker-compose restart`          | 5s     | Cambios config       |
| `docker-compose build && restart` | 30s    | Cambios Java         |
| `docker-compose up -d --build`    | 30s    | Desarrollo           |
| `build --no-cache && down && up`  | 2-3m   | Cambios importantes  |
| `Completa: limpiar todo`          | 5m+    | Solucionar problemas |

---

## 🛠️ Scripts Personalizados

### PowerShell Alias (Windows)

```powershell
# Agregar al perfil de PowerShell
# $PROFILE (ver ubicación con: $PROFILE)

function rebuild-fast {
    docker-compose build
    docker-compose restart orderapp
}

function rebuild-full {
    docker-compose build --no-cache
    docker-compose down
    docker-compose up -d
}

function rebuild-clean {
    docker-compose down -v
    docker system prune -a -f
    docker-compose build --no-cache
    docker-compose up -d
}

function docker-status {
    docker-compose ps
    docker-compose logs orderapp | tail -20
}

# Uso:
# rebuild-fast
# rebuild-full
# rebuild-clean
# docker-status
```

### Bash Alias (Linux/MacOS)

```bash
# Agregar a ~/.bashrc o ~/.zshrc

alias rebuild-fast='docker-compose build && docker-compose restart orderapp'
alias rebuild-full='docker-compose build --no-cache && docker-compose down && docker-compose up -d'
alias rebuild-clean='docker-compose down -v && docker system prune -a -f && docker-compose build --no-cache && docker-compose up -d'
alias docker-status='docker-compose ps && docker-compose logs orderapp | tail -20'

# Luego ejecutar:
# source ~/.bashrc
# o
# source ~/.zshrc

# Uso:
# rebuild-fast
# rebuild-full
# rebuild-clean
# docker-status
```

---

## 🎓 Explicación: Qué Pasa Internamente

### Paso 1: `docker-compose build`

```
1. Lee docker-compose.yml
2. Revisa Dockerfile
3. Ejecuta cada comando en el Dockerfile
4. Usa cache si está disponible (--no-cache ignora)
5. Crea imagen final: orderapp-orderapp:latest
```

### Paso 2: `docker-compose restart`

```
1. Detiene contenedor orderapp
2. Lo inicia nuevamente con la nueva imagen
3. Preserva volúmenes y red
```

### Paso 3: `docker-compose down && up`

```
1. DETIENE todo (contenedores + red)
2. ELIMINA contenedores
3. INICIA todo de nuevo
4. Crea nuevos contenedores
```

---

## ⚙️ Optimización: BuildKit

### Activar BuildKit (más rápido)

```bash
# Windows (en PowerShell o CMD)
set DOCKER_BUILDKIT=1

# Linux/MacOS
export DOCKER_BUILDKIT=1

# Luego, todos los builds usarán BuildKit
docker-compose build
```

**Beneficio:** 30-50% más rápido en builds

---

## 📈 Monitorizar Durante Rebuild

```bash
# En otra terminal, mientras se reconstruye:

# Ver construcción en tiempo real
docker-compose build --progress=plain

# Ver logs de la app mientras inicia
docker-compose logs -f orderapp

# Monitorizar uso de memoria/CPU
docker stats
```

---

## ✅ Checklist: Después de Rebuild

```bash
# 1. Ver estado
docker-compose ps
# Debería ver: "Up" para todos los servicios

# 2. Revisar logs de inicio
docker-compose logs orderapp | tail -30
# Buscar: "Started OrderappApplication"

# 3. Probar conectividad
curl http://localhost:8080/swagger-ui.html
# Debería devolver HTML

# 4. Probar API
curl http://localhost:8080/api/orders
# Debería devolver JSON

# 5. Entrar a Swagger
# Abre navegador: http://localhost:8080/swagger-ui.html
```

---

## 🚨 Troubleshooting

### Problema: "Cannot connect to Docker daemon"

```bash
# Solución: Reinicia Docker Desktop
# Windows/Mac: Reinicia la aplicación
# Linux: systemctl restart docker
```

### Problema: "Port 8080 already in use"

```bash
docker-compose down
docker-compose up -d
```

### Problema: "Build failed"

```bash
# Ver error detallado
docker-compose build --progress=plain

# Limpiar y reintentar
docker system prune -a -f
docker-compose build --no-cache
```

### Problema: "Out of space"

```bash
# Ver uso
docker system df

# Limpiar
docker system prune -a -f

# Reintentar build
docker-compose build --no-cache
```

---

## 📚 Recursos

- [DOCKER_REBUILD_GUIDE.md](DOCKER_REBUILD_GUIDE.md) - Guía completa
- [rebuild.ps1](rebuild.ps1) - Script PowerShell
- [rebuild.sh](rebuild.sh) - Script Bash
- [docker-compose.yml](docker-compose.yml) - Configuración

---

## 🎯 Resumen Final

**Rápido (30s):**

```bash
docker-compose build && docker-compose restart orderapp
```

**Completo (3m):**

```bash
docker-compose build --no-cache && docker-compose up -d
```

**Usar scripts:**

```bash
# Windows
.\rebuild.ps1 -Type fast

# Linux/VPS
./rebuild.sh fast
```

---

**Última actualización:** Noviembre 2024
