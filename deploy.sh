#!/bin/bash

# Script de despliegue para VPS
# Uso: bash deploy.sh

set -e

echo "================================"
echo "Order Management API - Despliegue"
echo "================================"

# Colores para output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Función para imprimir con color
print_status() {
    echo -e "${GREEN}[✓]${NC} $1"
}

print_error() {
    echo -e "${RED}[✗]${NC} $1"
}

print_warning() {
    echo -e "${YELLOW}[!]${NC} $1"
}

# Verificar si Docker está instalado
if ! command -v docker &> /dev/null; then
    print_error "Docker no está instalado"
    exit 1
fi
print_status "Docker está instalado"

# Verificar si Docker Compose está instalado
if ! command -v docker-compose &> /dev/null; then
    print_error "Docker Compose no está instalado"
    exit 1
fi
print_status "Docker Compose está instalado"

# Verificar si existe el archivo .env
if [ ! -f .env ]; then
    print_warning ".env no encontrado, creando desde .env.example"
    if [ -f .env.example ]; then
        cp .env.example .env
        print_warning "Por favor, editar .env con tus valores antes de continuar"
        exit 1
    else
        print_error ".env.example tampoco existe"
        exit 1
    fi
fi
print_status "Archivo .env encontrado"

# Cargar variables de entorno
export $(cat .env | xargs)

echo ""
echo "Configuración:"
echo "  - PostgreSQL User: $POSTGRES_USER"
echo "  - Database: $POSTGRES_DB"
echo "  - Spring Profile: $SPRING_PROFILES_ACTIVE"
echo "  - App Port: $APP_PORT"
echo ""

read -p "¿Continuar con el despliegue? (s/n) " -n 1 -r
echo
if [[ ! $REPLY =~ ^[Ss]$ ]]; then
    print_error "Despliegue cancelado"
    exit 1
fi

# Detener contenedores anteriores
print_status "Deteniendo contenedores anteriores..."
docker-compose down || true

# Construir imágenes
print_status "Construyendo imágenes Docker..."
docker-compose build

# Iniciar servicios
print_status "Iniciando servicios..."
docker-compose up -d

# Esperar a que PostgreSQL esté listo
print_status "Esperando a que PostgreSQL esté disponible..."
RETRIES=30
until docker-compose exec -T postgres pg_isready -U $POSTGRES_USER > /dev/null 2>&1 || [ $RETRIES -eq 0 ]; do
    echo "Esperando por PostgreSQL, reintentos restantes: $((RETRIES--))"
    sleep 1
done

if [ $RETRIES -eq 0 ]; then
    print_error "PostgreSQL no respondió a tiempo"
    docker-compose logs postgres
    exit 1
fi

print_status "PostgreSQL está listo"

# Esperar a que la aplicación esté lista
print_status "Esperando a que la aplicación esté lista..."
RETRIES=30
until curl -f http://localhost:$APP_PORT/swagger-ui.html > /dev/null 2>&1 || [ $RETRIES -eq 0 ]; do
    echo "Esperando por la aplicación, reintentos restantes: $((RETRIES--))"
    sleep 2
done

if [ $RETRIES -eq 0 ]; then
    print_warning "La aplicación está tardando en iniciar. Verificar logs:"
    echo "docker-compose logs orderapp"
else
    print_status "Aplicación lista"
fi

# Mostrar resumen
echo ""
echo "================================"
echo "Despliegue completado"
echo "================================"
echo ""
echo "Servicios disponibles:"
echo "  - API: http://localhost:$APP_PORT"
echo "  - Swagger UI: http://localhost:$APP_PORT/swagger-ui.html"
echo "  - Base de datos: localhost:$DB_PORT"
echo ""
echo "Comandos útiles:"
echo "  - Ver logs: docker-compose logs -f orderapp"
echo "  - Ver estado: docker-compose ps"
echo "  - Detener: docker-compose down"
echo ""
print_status "¡Despliegue exitoso!"
