package com.facerecon.app.utils

/**
 * CONFIGURACIÓN DE DESARROLLO
 * 
 * Esta versión usa la API local para desarrollo
 */
object AppConfig {
    // Configuración para desarrollo
    const val CURRENT_ENVIRONMENT = "physical"
    
    // URLs de cada entorno (no tocar)
    const val EMULATOR_URL = "http://10.0.2.2:8000/"
    const val PHYSICAL_URL = "http://192.168.18.194:8000/"
    const val RENDER_URL = "https://facerecon-api.onrender.com/"
    
    fun getBaseUrl(): String {
        return when (CURRENT_ENVIRONMENT) {
            "emulator" -> EMULATOR_URL
            "physical" -> PHYSICAL_URL
            "render" -> RENDER_URL
            else -> PHYSICAL_URL
        }
    }
    
    fun getEnvironmentName(): String {
        return when (CURRENT_ENVIRONMENT) {
            "emulator" -> "Emulador (Local)"
            "physical" -> "Dispositivo Físico"
            "render" -> "Producción (Render)"
            else -> "Dispositivo Físico"
        }
    }
    
    fun getAppName(): String {
        return "FaceRecon DEV"
    }
} 