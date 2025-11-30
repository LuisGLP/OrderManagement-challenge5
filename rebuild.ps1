#!/usr/bin/env powershell

# rebuild.ps1
# Script para reconstruir imagen Docker de forma rápida
# Uso: .\rebuild.ps1 -Type fast
#      .\rebuild.ps1 -Type full

param(
    [Parameter(Mandatory=$false)]
    [ValidateSet("fast", "full")]
    [string]$Type = "fast"
)

function Write-ColorOutput($message, $color = "Green") {
    Write-Host $message -ForegroundColor $color
}

function Get-ElapsedTime($startTime) {
    $elapsed = New-TimeSpan -Start $startTime -End (Get-Date)
    return "{0:mm}m {0:ss}s" -f $elapsed
}

Write-ColorOutput "╔════════════════════════════════════════════════════════════╗" "Cyan"
Write-ColorOutput "║          🐳 Docker Rebuild Script - Order Management       ║" "Cyan"
Write-ColorOutput "╚════════════════════════════════════════════════════════════╝" "Cyan"
Write-ColorOutput ""

$startTime = Get-Date

if ($Type -eq "fast") {
    Write-ColorOutput "⚡ Modo: RÁPIDO (con cache)" "Yellow"
    Write-ColorOutput ""
    
    Write-ColorOutput "1️⃣  Reconstruyendo imagen..." "Cyan"
    & docker-compose build
    
    Write-ColorOutput "2️⃣  Reiniciando aplicación..." "Cyan"
    & docker-compose restart orderapp
    
} else {
    Write-ColorOutput "🔄 Modo: COMPLETO (sin cache)" "Yellow"
    Write-ColorOutput ""
    
    Write-ColorOutput "1️⃣  Reconstruyendo imagen (sin cache)..." "Cyan"
    & docker-compose build --no-cache
    
    Write-ColorOutput "2️⃣  Deteniendo contenedores..." "Cyan"
    & docker-compose down
    
    Write-ColorOutput "3️⃣  Iniciando servicios..." "Cyan"
    & docker-compose up -d
    
    Write-ColorOutput "4️⃣  Esperando 40 segundos para que la aplicación inicie..." "Cyan"
    for ($i = 40; $i -gt 0; $i--) {
        Write-Host "`rEsperando... $i segundos" -NoNewline
        Start-Sleep -Seconds 1
    }
    Write-ColorOutput "`rEsperando... completado!                    " "Green"
}

Write-ColorOutput ""
Write-ColorOutput "════════════════════════════════════════════════════════════" "Cyan"
Write-ColorOutput "📊 Estado de Servicios:" "Green"
Write-ColorOutput "════════════════════════════════════════════════════════════" "Cyan"

& docker-compose ps

Write-ColorOutput ""
Write-ColorOutput "📋 Últimas líneas de logs:" "Green"
Write-ColorOutput "════════════════════════════════════════════════════════════" "Cyan"

& docker-compose logs orderapp | Select-Object -Last 10

$elapsedTime = Get-ElapsedTime $startTime
Write-ColorOutput ""
Write-ColorOutput "════════════════════════════════════════════════════════════" "Cyan"
Write-ColorOutput "✅ Rebuild completado en: $elapsedTime" "Green"
Write-ColorOutput ""
Write-ColorOutput "🌐 Accede a la aplicación en:" "Yellow"
Write-ColorOutput "   http://localhost:8080/swagger-ui.html" "Cyan"
Write-ColorOutput ""
Write-ColorOutput "📝 Para ver logs en tiempo real:" "Yellow"
Write-ColorOutput "   docker-compose logs -f orderapp" "Cyan"
Write-ColorOutput "════════════════════════════════════════════════════════════" "Cyan"
