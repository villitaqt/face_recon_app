# FaceRecon App

Aplicación de reconocimiento facial con soporte para múltiples entornos.

## 🚀 Versiones Disponibles

### 📱 Versión de Desarrollo (DEV)
- **API**: Local (192.168.18.194:8000)
- **Nombre**: FaceRecon DEV
- **ID**: `com.facerecon.app.dev`
- **Uso**: Para desarrollo y pruebas locales

### 🌐 Versión de Producción (PROD)
- **API**: Render (https://facerecon-api.onrender.com/)
- **Nombre**: FaceRecon
- **ID**: `com.facerecon.app.prod`
- **Uso**: Para distribución y uso en producción

## 🛠️ Construcción

### Usando el script automático:
```powershell
# Construir versión de desarrollo
.\switch-env.ps1 dev

# Construir versión de producción
.\switch-env.ps1 prod
```

### Usando Gradle directamente:
```bash
# Versión de desarrollo
./gradlew assembleDevDebug

# Versión de producción
./gradlew assembleProdDebug
```

## 📦 Instalación

### Instalar ambas versiones en el mismo dispositivo:
```bash
# Instalar versión de desarrollo
adb install app/build/outputs/apk/dev/debug/app-dev-debug.apk

# Instalar versión de producción
adb install app/build/outputs/apk/prod/debug/app-prod-debug.apk
```

## 🔧 Configuración

### Estructura de archivos:
```
app/src/
├── main/          # Código común
├── dev/           # Configuración de desarrollo
└── prod/          # Configuración de producción
```

### Diferencias entre versiones:
- **DEV**: Usa API local, nombre "FaceRecon DEV"
- **PROD**: Usa API de Render, nombre "FaceRecon"

## 📱 Características

- ✅ Reconocimiento facial en tiempo real
- ✅ Gestión de usuarios
- ✅ Registro de nuevos usuarios
- ✅ Detección de personas requisitoriadas
- ✅ Interfaz moderna con Material Design 3
- ✅ Soporte para múltiples entornos
- ✅ Navegación intuitiva

## 🎯 Uso

1. **Desarrollo**: Usa la versión DEV para probar con tu API local
2. **Producción**: Usa la versión PROD para distribución final
3. **Coexistencia**: Ambas versiones pueden estar instaladas simultáneamente

## 🔄 Actualización

Para actualizar la configuración de IP en desarrollo:
1. Edita `app/src/dev/java/com/facerecon/app/utils/AppConfig.kt`
2. Cambia `PHYSICAL_URL` a tu nueva IP
3. Reconstruye: `.\switch-env.ps1 dev`

## 📋 Requisitos

- Android Studio Hedgehog | 2023.1.1
- Kotlin 1.9.0+
- Android SDK 34
- Dispositivo Android con cámara

# Aplicación de Reconocimiento Facial

Una aplicación Android construida con Kotlin, Jetpack Compose y librerías modernas de Android para reconocimiento facial usando la cámara y la integración con una API.

## Características

- **Integración con Cámara**: Usa CameraX para la vista previa y captura de imágenes
- **Reconocimiento Facial**: Captura imágenes y las envía a una API backend para reconocimiento facial
- **Interfaz Moderna**: Construida con Jetpack Compose y diseño Material 3
- **Inyección de Dependencias**: Utiliza Hilt para una gestión limpia de dependencias
- **Redes**: Retrofit para la comunicación con la API
- **Manejo de Permisos**: Solicitud de permisos de cámara en tiempo de ejecución usando Accompanist Permissions
- **Verificación de Salud del Backend**: Prueba la conexión con el backend en Python antes de capturar imágenes
- **Visualización de Imágenes**: Carga y muestra imágenes de usuarios desde el servidor usando Coil
- **Gestión de Usuarios**: CRUD completo para usuarios con imágenes de perfil

## Estructura del Proyecto

```
app/src/main/java/com/facerecon/app/
├── data/
│   ├── api/
│   │   └── ApiService.kt          # Interfaz de servicio API con Retrofit
│   ├── models/
│   │   ├── User.kt                # Modelo de datos de usuario
│   │   └── ApiResponse.kt         # Modelos de respuesta de la API
│   └── repository/
│       └── FaceRecognitionRepository.kt  # Repositorio para llamadas a la API
├── di/
│   └── NetworkModule.kt           # Módulo de inyección de dependencias con Hilt
├── ui/
│   ├── components/
│   │   └── UserImage.kt           # Componente reutilizable para imágenes de usuarios
│   ├── screens/
│   │   ├── MainScreen.kt          # Pantalla principal
│   │   ├── CameraScreen.kt        # Pantalla de cámara con vista previa
│   │   ├── UserManagementScreen.kt # Gestión de usuarios
│   │   └── UserRegistrationScreen.kt # Registro de usuarios
│   ├── theme/                     # Definiciones de tema para Compose
│   └── viewmodel/
│       └── FaceRecognitionViewModel.kt  # ViewModel para gestión de estado
├── utils/
│   ├── CameraUtils.kt             # Funciones utilitarias de cámara
│   └── NetworkConfig.kt           # Utilidad de configuración de red
├── FaceRecognitionApplication.kt  # Clase de aplicación con Hilt
└── MainActivity.kt                # Actividad principal con UI Compose
```

## Configuración

### 1. **Configuración de Red**

La app está configurada para conectarse a tu backend en Python. Actualiza la configuración en `NetworkConfig.kt`:

```kotlin
// Configuración para diferentes entornos
const val ENVIRONMENT = "render" // Opciones: "emulator", "physical", "render"

// Para emulador de Android - conecta a localhost en la máquina anfitriona
private const val EMULATOR_BASE_URL = "http://10.0.2.2:8000/"

// Para dispositivo físico - conecta a la IP de tu PC
private const val PHYSICAL_DEVICE_BASE_URL = "http://192.168.18.194:8000/"

// URL de producción - Despliegue en Render
private const val RENDER_BASE_URL = "https://facerecon-api.onrender.com/"
```

**Para emulador de Android:**
- Establece `ENVIRONMENT = "emulator"`
- Usa `http://10.0.2.2:8000/` (IP especial del emulador para localhost)

**Para dispositivo físico:**
- Establece `ENVIRONMENT = "physical"`
- Actualiza `PHYSICAL_DEVICE_BASE_URL` con la IP real de tu PC
- Asegúrate de que ambos dispositivos estén en la misma red

**Para producción (Render):**
- Establece `ENVIRONMENT = "render"`
- Usa `https://facerecon-api.onrender.com/` (URL de producción)

### 2. **Requisitos del Backend en Python**

Tu backend en Python debe tener estos endpoints:
- `GET /health` - Devuelve `{"status": "healthy", "message": "API funcionando correctamente"}`
- `POST /recognize` - Acepta datos multipart con un archivo de imagen
- `GET /usuarios/` - Lista todos los usuarios
- `POST /usuarios/` - Registra un nuevo usuario
- `PUT /usuarios/{id}` - Actualiza un usuario
- `DELETE /usuarios/{id}` - Elimina un usuario

### 3. **Compilar y Ejecutar**

La app está lista para compilarse y ejecutarse en dispositivos/emuladores Android

## Integración con la API

La app espera una API de reconocimiento facial con los siguientes endpoints:

### Endpoint de Verificación de Salud:
- `GET /health` - Devuelve el estado del backend
- Respuesta esperada: `{"status": "healthy", "message": "API funcionando correctamente"}`

### Endpoint de Reconocimiento:
- `POST /recognize` - Acepta datos multipart con un archivo de imagen
- Devuelve una respuesta JSON con los datos de reconocimiento de usuario

### Endpoints de Gestión de Usuarios:
- `GET /usuarios/` - Lista todos los usuarios
- `GET /usuarios/{id}` - Obtiene un usuario específico
- `POST /usuarios/` - Registra un nuevo usuario
- `PUT /usuarios/{id}` - Actualiza un usuario
- `DELETE /usuarios/{id}` - Elimina un usuario

### Formato de Respuesta Esperado de la API:
```json
{
  "success": true,
  "user": {
    "id": "user123",
    "nombre": "John",
    "apellido": "Doe",
    "email": "john@example.com",
    "telefono": "123456789",
    "requisitoriado": false,
    "url_foto": "uploads/user_123.jpg",
    "confidence": 0.95
  },
  "message": "Rostro reconocido exitosamente"
}
```

## Visualización de Imágenes de Usuarios

### Carga de Imágenes
La app utiliza Coil para cargar imágenes desde el servidor. Las imágenes se construyen concatenando la URL base con el campo `url_foto` de la respuesta de la API:

```kotlin
// Ejemplo de construcción de URL de imagen
val completeImageUrl = NetworkConfig.buildImageUrl(user.urlFoto)
// Resultado: https://facerecon-api.onrender.com/uploads/user_123.jpg
```

### Componente de Imagen de Usuario
Se incluye un componente reutilizable `UserImageWithFallback` que:
- Carga la imagen del usuario desde el servidor
- Muestra las iniciales del usuario si no hay imagen
- Maneja errores de carga mostrando un fallback
- Es circular y responsive

### Manejo de Errores
- Si la imagen no existe, se muestran las iniciales del usuario
- Si hay error de red, se muestra un placeholder
- Las imágenes se cachean automáticamente por Coil

## Prueba de la Conexión

1. **Lanza la app** y concede permisos de cámara
2. **Verifica la configuración de red** - La app muestra en qué modo estás
3. **Prueba la conexión al backend** - Toca "Probar conexión con el backend" para verificar la conectividad
4. **Captura y reconoce** - Toma una foto para probar el reconocimiento facial
5. **Gestiona usuarios** - Registra y gestiona usuarios con imágenes de perfil

## Permisos

La app requiere los siguientes permisos:
- `CAMERA` - Para acceso a la cámara
- `INTERNET` - Para comunicación con la API
- `ACCESS_NETWORK_STATE` - Para monitoreo del estado de red

## Pruebas

El proyecto incluye una prueba básica de UI que verifica que la pantalla principal se lance correctamente:
- `MainActivityTest.kt` - Prueba que se muestre la pantalla de permisos de cámara

## Dependencias

- **Jetpack Compose**: Toolkit moderno de UI
- **CameraX**: Funcionalidad de cámara
- **Hilt**: Inyección de dependencias
- **Retrofit**: Comunicación de red
- **Coil**: Carga de imágenes desde la red
- **Accompanist Permissions**: Manejo de permisos en tiempo de ejecución
- **Material 3**: Sistema de diseño

## Requisitos Mínimos

- Android API Nivel 24 (Android 7.0)
- API objetivo Nivel 34 (Android 14)
- Kotlin 1.9+
- Java 17

## Solución de Problemas

### Problemas de Conexión:
1. **Emulador**: Asegúrate de que el backend en Python esté corriendo en `localhost:8000`
2. **Dispositivo físico**: 
   - Verifica que ambos dispositivos estén en la misma red
   - Asegúrate de que la IP de tu PC sea correcta
   - Verifica que el firewall permita conexiones en el puerto 8000
3. **Render**: 
   - Verifica que el servicio esté activo en Render
   - Revisa los logs del servicio para errores

### Problemas con la Cámara:
1. Concede permisos de cámara cuando se soliciten
2. Asegúrate de que el dispositivo tenga cámara frontal (la app usa la cámara frontal por defecto)

### Problemas con Imágenes:
1. Verifica que las URLs de las imágenes sean correctas
2. Asegúrate de que el servidor esté sirviendo las imágenes correctamente
3. Revisa la conectividad de red para la carga de imágenes

### Problemas con el Backend:
1. Usa el botón "Probar conexión con el backend" para verificar la conectividad
2. Revisa los logs del backend para cualquier error
3. Asegúrate de que el backend esté corriendo y accesible
