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
│   ├── screens/
│   │   └── CameraScreen.kt        # Pantalla principal de cámara con vista previa
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
// Para emulador de Android - conecta a localhost en la máquina anfitriona
const val USE_EMULATOR = true  // Cambia a false para dispositivo físico

// Para dispositivo físico - actualiza esto con la IP de tu PC
private const val PHYSICAL_DEVICE_BASE_URL = "http://192.168.18.194:8000/"
```

**Para emulador de Android:**
- Establece `USE_EMULATOR = true`
- Usa `http://10.0.2.2:8000/` (IP especial del emulador para localhost)

**Para dispositivo físico:**
- Establece `USE_EMULATOR = false`
- Actualiza `PHYSICAL_DEVICE_BASE_URL` con la IP real de tu PC
- Asegúrate de que ambos dispositivos estén en la misma red

### 2. **Requisitos del Backend en Python**

Tu backend en Python debe tener estos endpoints:
- `GET /health` - Devuelve `{"status": "healthy", "message": "API funcionando correctamente"}`
- `POST /recognize` - Acepta datos multipart con un archivo de imagen

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

### Formato de Respuesta Esperado de la API:
```json
{
  "success": true,
  "user": {
    "id": "user123",
    "name": "John Doe",
    "confidence": 0.95
  },
  "message": "Rostro reconocido exitosamente"
}
```

## Prueba de la Conexión

1. **Lanza la app** y concede permisos de cámara
2. **Verifica la configuración de red** - La app muestra en qué modo estás
3. **Prueba la conexión al backend** - Toca "Probar conexión con el backend" para verificar la conectividad
4. **Captura y reconoce** - Toma una foto para probar el reconocimiento facial

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

### Problemas con la Cámara:
1. Concede permisos de cámara cuando se soliciten
2. Asegúrate de que el dispositivo tenga cámara frontal (la app usa la cámara frontal por defecto)

### Problemas con el Backend:
1. Usa el botón "Probar conexión con el backend" para verificar la conectividad
2. Revisa los logs del backend para cualquier error
3. Asegúrate de que el backend esté corriendo y accesible
