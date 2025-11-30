# Guía de Despliegue - Order Management API

## 📋 Tabla de Contenidos

1. [Requisitos](#requisitos)
2. [Despliegue Local con Docker Compose](#despliegue-local-con-docker-compose)
3. [Despliegue en VPS](#despliegue-en-vps)
4. [Seguridad](#seguridad)
5. [Mantenimiento](#mantenimiento)

---

## ⚙️ Requisitos

### Local

- Docker Desktop (incluye Docker y Docker Compose)
- Git
- 2GB de RAM disponible

### VPS

- Ubuntu 20.04 LTS o superior
- Docker y Docker Compose instalados
- Mínimo 2GB de RAM
- Al menos 20GB de almacenamiento
- Acceso SSH al servidor

---

## 🐳 Despliegue Local con Docker Compose

### Paso 1: Clonar el repositorio

```bash
git clone <repository-url>
cd orderapp
```

### Paso 2: Crear archivo .env

```bash
cp .env.example .env
```

Editar `.env` con tus valores:

```env
POSTGRES_USER=postgres
POSTGRES_PASSWORD=tu_contrasena_segura
POSTGRES_DB=online_store_dev
SPRING_PROFILES_ACTIVE=dev
APP_PORT=8080
DB_PORT=5432
```

### Paso 3: Ejecutar con Docker Compose

```bash
# Construir las imágenes
docker-compose build

# Iniciar los servicios
docker-compose up -d

# Ver logs
docker-compose logs -f orderapp
```

### Paso 4: Verificar que está funcionando

```bash
# Esperar 30-40 segundos para que la aplicación inicie
# Acceder a Swagger UI
http://localhost:8080/swagger-ui.html

# Verificar salud de la aplicación
curl http://localhost:8080/swagger-ui.html
```

### Paso 5: Detener los servicios

```bash
docker-compose down

# Detener sin eliminar volúmenes
docker-compose down -v  # Elimina también los datos de la BD
```

---

## 🚀 Despliegue en VPS

### Paso 1: Preparar el VPS

```bash
# Conectarse al VPS
ssh usuario@tu-vps.com

# Actualizar el sistema
sudo apt-get update
sudo apt-get upgrade -y

# Instalar Docker
curl -fsSL https://get.docker.com -o get-docker.sh
sudo sh get-docker.sh

# Instalar Docker Compose
sudo curl -L "https://github.com/docker/compose/releases/latest/download/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose
sudo chmod +x /usr/local/bin/docker-compose

# Agregar usuario actual al grupo docker
sudo usermod -aG docker $USER
newgrp docker

# Verificar instalación
docker --version
docker-compose --version
```

### Paso 2: Clonar el repositorio en el VPS

```bash
cd /home/tu-usuario
git clone <repository-url>
cd orderapp
```

### Paso 3: Configurar variables de entorno

```bash
# Copiar archivo de ejemplo
cp .env.example .env

# Editar con valores seguros (usar contraseñas fuertes)
nano .env
```

**Ejemplo de .env para producción:**

```env
POSTGRES_USER=app_user
POSTGRES_PASSWORD=GeneraUnaSuperContraseña123!@#
POSTGRES_DB=online_store_prod
SPRING_PROFILES_ACTIVE=prod
APP_PORT=8080
DB_PORT=5432
DB_HOST=postgres
DB_NAME=online_store_prod
```

### Paso 4: Desplegar la aplicación

```bash
# Construir las imágenes
docker-compose build

# Iniciar en background
docker-compose up -d

# Ver el estado
docker-compose ps

# Ver logs
docker-compose logs -f orderapp
```

### Paso 5: Configurar Reverse Proxy (Nginx)

```bash
# Instalar Nginx
sudo apt-get install -y nginx

# Crear configuración
sudo nano /etc/nginx/sites-available/orderapp
```

**Contenido del archivo de configuración:**

```nginx
server {
    listen 80;
    server_name tu-dominio.com;

    location / {
        proxy_pass http://localhost:8080;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;

        # Timeouts
        proxy_connect_timeout 60s;
        proxy_send_timeout 60s;
        proxy_read_timeout 60s;
    }
}
```

```bash
# Habilitar el sitio
sudo ln -s /etc/nginx/sites-available/orderapp /etc/nginx/sites-enabled/

# Probar configuración
sudo nginx -t

# Reiniciar Nginx
sudo systemctl restart nginx
```

### Paso 6: Configurar SSL con Let's Encrypt (Recomendado)

```bash
# Instalar Certbot
sudo apt-get install -y certbot python3-certbot-nginx

# Obtener certificado
sudo certbot --nginx -d tu-dominio.com

# Auto-renovación (se configura automáticamente)
sudo systemctl start certbot.timer
sudo systemctl enable certbot.timer
```

### Paso 7: Configurar Firewall

```bash
# Habilitar UFW
sudo ufw enable

# Permitir SSH
sudo ufw allow ssh

# Permitir HTTP
sudo ufw allow http

# Permitir HTTPS
sudo ufw allow https

# Ver estado
sudo ufw status
```

---

## 🔒 Seguridad

### Buenas Prácticas Implementadas

1. **Usuario no-root en Docker**: La aplicación corre como usuario `appuser` (UID 1000)
2. **Variables de entorno**: Credenciales no están hardcodeadas
3. **Health Checks**: Docker verifica periódicamente la salud de los contenedores
4. **Compresión HTTP**: Habilitada para reducir tráfico
5. **Logging estructurado**: Facilita auditoría y debugging

### Recomendaciones Adicionales

#### 1. **Cambiar contraseña de PostgreSQL**

```bash
# Dentro del contenedor o en la BD
ALTER USER postgres WITH PASSWORD 'nueva_contrasena_segura';
```

#### 2. **Limitar acceso a la BD**

En `.env`:

```env
DB_HOST=postgres  # Solo accesible desde el contenedor de app
```

#### 3. **Usar HTTPS en producción**

Implementado con Let's Encrypt (ver paso 6 del despliegue en VPS)

#### 4. **Monitorear logs**

```bash
# Ver logs de la aplicación
docker-compose logs -f orderapp

# Ver logs de PostgreSQL
docker-compose logs -f postgres

# Guardar logs
docker-compose logs > app_logs.txt
```

#### 5. **Backups de la BD**

```bash
# Crear backup
docker-compose exec postgres pg_dump -U postgres online_store_prod > backup_$(date +%Y%m%d).sql

# Restaurar desde backup
docker-compose exec -T postgres psql -U postgres online_store_prod < backup_20231128.sql
```

#### 6. **Actualizar imagen regularmente**

```bash
# Descargar últimas versiones
docker-compose pull

# Reconstruir
docker-compose build --no-cache

# Reiniciar
docker-compose up -d
```

---

## 🛠️ Mantenimiento

### Comandos Útiles

```bash
# Ver contenedores corriendo
docker-compose ps

# Ver uso de recursos
docker stats

# Ejecutar comando en contenedor
docker-compose exec orderapp ls -la

# Ver variables de ambiente
docker-compose exec orderapp env

# Limpiar recursos no usados
docker system prune -a
```

### Troubleshooting

#### La aplicación no inicia

```bash
# Ver logs detallados
docker-compose logs orderapp

# Verificar variables de entorno
cat .env

# Verificar conectividad con BD
docker-compose exec orderapp nc -zv postgres 5432
```

#### Problema de puerto ocupado

```bash
# Ver qué está usando el puerto 8080
lsof -i :8080

# O cambiar puerto en .env
APP_PORT=8081
docker-compose restart
```

#### Problema con permisos de volúmenes

```bash
# Ajustar permisos
sudo chown -R 1000:1000 ./postgres_data
```

---

## 📞 Contacto y Soporte

Para problemas, reportar issues en el repositorio de GitHub.

---

## 📄 Licencia

Apache 2.0
