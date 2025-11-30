#!/bin/bash

# Script de backup para PostgreSQL
# Uso: bash backup.sh

BACKUP_DIR="./backups"
TIMESTAMP=$(date +%Y%m%d_%H%M%S)
BACKUP_FILE="$BACKUP_DIR/backup_$TIMESTAMP.sql"

# Crear directorio de backups si no existe
mkdir -p $BACKUP_DIR

# Obtener variables de entorno
export $(cat .env | xargs)

echo "Iniciando backup de la base de datos..."

# Crear backup
docker-compose exec -T postgres pg_dump -U $POSTGRES_USER $POSTGRES_DB > $BACKUP_FILE

if [ $? -eq 0 ]; then
    echo "✓ Backup creado exitosamente: $BACKUP_FILE"
    echo "  Tamaño: $(du -h $BACKUP_FILE | cut -f1)"
    
    # Comprimir el backup
    gzip $BACKUP_FILE
    echo "✓ Backup comprimido: ${BACKUP_FILE}.gz"
    
    # Mantener solo los últimos 7 backups
    find $BACKUP_DIR -name "backup_*.sql.gz" -mtime +7 -delete
    echo "✓ Backups antiguos eliminados"
else
    echo "✗ Error al crear el backup"
    exit 1
fi
