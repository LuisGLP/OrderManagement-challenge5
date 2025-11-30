#!/bin/bash

# rebuild.sh
# Script para reconstruir imagen Docker de forma rápida
# Uso: bash rebuild.sh fast
#      bash rebuild.sh full
#      bash rebuild.sh
#      chmod +x rebuild.sh && ./rebuild.sh

# Colores
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
CYAN='\033[0;36m'
NC='\033[0m' # No Color

# Por defecto "fast"
TYPE=${1:-fast}

# Función para imprimir con color
print_header() {
    echo -e "${CYAN}╔════════════════════════════════════════════════════════════╗${NC}"
    echo -e "${CYAN}║          🐳 Docker Rebuild Script - Order Management       ║${NC}"
    echo -e "${CYAN}╚════════════════════════════════════════════════════════════╝${NC}"
    echo ""
}

print_step() {
    echo -e "${CYAN}$1${NC}"
}

print_success() {
    echo -e "${GREEN}✅ $1${NC}"
}

print_warning() {
    echo -e "${YELLOW}⚠️  $1${NC}"
}

# Capturar tiempo de inicio
start_time=$(date +%s)

print_header

if [ "$TYPE" = "fast" ]; then
    print_warning "Modo: RÁPIDO (con cache)"
    echo ""
    
    print_step "1️⃣  Reconstruyendo imagen..."
    docker-compose build
    
    print_step "2️⃣  Reiniciando aplicación..."
    docker-compose restart orderapp
    
else
    print_warning "Modo: COMPLETO (sin cache)"
    echo ""
    
    print_step "1️⃣  Reconstruyendo imagen (sin cache)..."
    docker-compose build --no-cache
    
    print_step "2️⃣  Deteniendo contenedores..."
    docker-compose down
    
    print_step "3️⃣  Iniciando servicios..."
    docker-compose up -d
    
    print_step "4️⃣  Esperando 40 segundos para que la aplicación inicie..."
    for i in {40..1}; do
        printf "\rEsperando... %d segundos" $i
        sleep 1
    done
    echo ""
fi

echo ""
print_step "════════════════════════════════════════════════════════════"
echo -e "${GREEN}📊 Estado de Servicios:${NC}"
print_step "════════════════════════════════════════════════════════════"

docker-compose ps

echo ""
print_step "════════════════════════════════════════════════════════════"
echo -e "${GREEN}📋 Últimas líneas de logs:${NC}"
print_step "════════════════════════════════════════════════════════════"

docker-compose logs orderapp | tail -10

# Calcular tiempo transcurrido
end_time=$(date +%s)
elapsed=$((end_time - start_time))
minutes=$((elapsed / 60))
seconds=$((elapsed % 60))

echo ""
print_step "════════════════════════════════════════════════════════════"
print_success "Rebuild completado en: ${minutes}m ${seconds}s"
echo ""
echo -e "${YELLOW}🌐 Accede a la aplicación en:${NC}"
echo -e "${CYAN}   http://localhost:8080/swagger-ui.html${NC}"
echo ""
echo -e "${YELLOW}📝 Para ver logs en tiempo real:${NC}"
echo -e "${CYAN}   docker-compose logs -f orderapp${NC}"
print_step "════════════════════════════════════════════════════════════"
