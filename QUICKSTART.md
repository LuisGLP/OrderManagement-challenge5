# Guía Rápida de Inicio

## 🚀 Inicio Rápido (Local)

```bash
# 1. Clonar y acceder al proyecto
git clone <repository-url>
cd orderapp

# 2. Copiar configuración de ejemplo
cp .env.example .env

# 3. Ejecutar con Docker Compose
docker-compose up -d

# 4. Acceder a Swagger
# http://localhost:8080/swagger-ui.html
```

## 📦 Archivos Creados para Dockerización

| Archivo                | Propósito                                     |
| ---------------------- | --------------------------------------------- |
| `Dockerfile`           | Construcción de imagen multi-stage optimizada |
| `docker-compose.yml`   | Orquestación de servicios (app + BD)          |
| `.env.example`         | Variables de configuración de ejemplo         |
| `application-prod.yml` | Configuración para producción                 |
| `.dockerignore`        | Archivos a excluir de la imagen Docker        |
| `DEPLOYMENT.md`        | Guía completa de despliegue                   |
| `deploy.sh`            | Script automatizado de despliegue             |
| `backup.sh`            | Script para backups de BD                     |
| `nginx.conf.example`   | Configuración de reverse proxy                |

## 🔧 Configuración para Producción

### Cambiar contraseñas (IMPORTANTE)

```bash
nano .env
# Cambiar:
# POSTGRES_PASSWORD=changeme123!  ← Nueva contraseña fuerte
# SPRING_PROFILES_ACTIVE=prod     ← Cambiar a prod
```

### Contraseñas Seguras Recomendadas

- Mínimo 12 caracteres
- Incluir mayúsculas, minúsculas, números y símbolos
- Ejemplo: `Prod@2024#SecureP@ss123`

## 📊 Variables de Entorno Principales

```env
# Base de Datos
POSTGRES_USER=postgres          # Usuario de BD
POSTGRES_PASSWORD=changeme123!  # CAMBIAR EN PRODUCCIÓN
POSTGRES_DB=online_store_prod   # Nombre de la BD

# Aplicación
SPRING_PROFILES_ACTIVE=prod     # dev|prod|test
JPA_DDL_AUTO=update             # validate|update|create|create-drop
APP_PORT=8080                   # Puerto de la aplicación
DB_PORT=5432                    # Puerto de PostgreSQL
```

## 🐳 Comandos Docker Útiles

```bash
# Ver estado de contenedores
docker-compose ps

# Ver logs en tiempo real
docker-compose logs -f orderapp

# Ejecutar comando en contenedor
docker-compose exec orderapp ls -la

# Parar servicios
docker-compose down

# Parar y eliminar volúmenes (¡Cuidado!)
docker-compose down -v
```

## 🌐 URLs de Acceso

- **API**: http://localhost:8080
- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **API Docs JSON**: http://localhost:8080/api-docs

## 🔐 Seguridad Implementada

✅ Usuario no-root en contenedores  
✅ Contraseñas en variables de entorno  
✅ Health checks automáticos  
✅ Compresión HTTP habilitada  
✅ HTTPS listo para SSL  
✅ Firewall configuración incluida

## 🆘 Troubleshooting

### Contenedor no inicia

```bash
docker-compose logs orderapp
```

### Puerto ya en uso

```bash
# Cambiar en .env
APP_PORT=8081
docker-compose restart
```

### BD sin conexión

```bash
# Verificar conectividad
docker-compose exec orderapp nc -zv postgres 5432
```

## 📚 Documentación Completa

Ver `DEPLOYMENT.md` para guía detallada de despliegue en VPS.

## ✉️ Soporte

Para problemas, crear un issue en el repositorio.
