package com.facerecon.app.utils

/**
 * CONFIGURACIÓN DE PRODUCCIÓN
 * 
 * Esta versión usa la API de Render para producción
 */
object AppConfig {
    // Configuración para producción
    const val CURRENT_ENVIRONMENT = "render"
    
    // URLs de cada entorno (no tocar)
    const val EMULATOR_URL = "http://10.0.2.2:8000/"
    const val PHYSICAL_URL = "http://192.168.18.194:8000/"
    const val RENDER_URL = "https://facerecon-api.onrender.com/"
    
    fun getBaseUrl(): String {
        return when (CURRENT_ENVIRONMENT) {
            "emulator" -> EMULATOR_URL
            "physical" -> PHYSICAL_URL
            "render" -> RENDER_URL
            else -> RENDER_URL
        }
    }
    
    fun getEnvironmentName(): String {
        return when (CURRENT_ENVIRONMENT) {
            "emulator" -> "Emulador (Local)"
            "physical" -> "Dispositivo Físico"
            "render" -> "Producción (Render)"
            else -> "Producción (Render)"
        }
    }
    
    fun getAppName(): String {
        return "FaceRecon p"
    }
} 