# Script para cambiar entre versiones de desarrollo y producción
# Uso: .\switch-env.ps1 [dev|prod]

param(
    [Parameter(Mandatory=$true)]
    [ValidateSet("dev", "prod")]
    [string]$Environment
)

Write-Host "🔄 Cambiando a entorno: $Environment" -ForegroundColor Yellow

if ($Environment -eq "dev") {
    Write-Host "📱 Construyendo versión de DESARROLLO..." -ForegroundColor Green
    Write-Host "   - API: Local (192.168.18.194:8000)" -ForegroundColor Cyan
    Write-Host "   - Nombre: FaceRecon DEV" -ForegroundColor Cyan
    Write-Host "   - ID: com.facerecon.app.dev" -ForegroundColor Cyan
    
    # Construir versión de desarrollo
    ./gradlew assembleDevDebug
    
    Write-Host "✅ Versión de desarrollo construida!" -ForegroundColor Green
    Write-Host "📦 APK: app/build/outputs/apk/dev/debug/app-dev-debug.apk" -ForegroundColor Magenta
    
} elseif ($Environment -eq "prod") {
    Write-Host "🚀 Construyendo versión de PRODUCCIÓN..." -ForegroundColor Green
    Write-Host "   - API: Render (https://facerecon-api.onrender.com/)" -ForegroundColor Cyan
    Write-Host "   - Nombre: FaceRecon" -ForegroundColor Cyan
    Write-Host "   - ID: com.facerecon.app.prod" -ForegroundColor Cyan
    
    # Construir versión de producción
    ./gradlew assembleProdDebug
    
    Write-Host "✅ Versión de producción construida!" -ForegroundColor Green
    Write-Host "📦 APK: app/build/outputs/apk/prod/debug/app-prod-debug.apk" -ForegroundColor Magenta
}

Write-Host ""
Write-Host "📋 Comandos útiles:" -ForegroundColor Yellow
Write-Host "   Instalar dev: adb install app/build/outputs/apk/dev/debug/app-dev-debug.apk" -ForegroundColor Gray
Write-Host "   Instalar prod: adb install app/build/outputs/apk/prod/debug/app-prod-debug.apk" -ForegroundColor Gray
Write-Host "   Limpiar: ./gradlew clean" -ForegroundColor Gray 