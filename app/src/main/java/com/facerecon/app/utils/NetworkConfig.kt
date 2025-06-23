package com.facerecon.app.utils

object NetworkConfig {
    // Configuration for different environments
    // Set this to true for emulator, false for physical device, "render" for production
    const val ENVIRONMENT = "render" // Options: "emulator", "physical", "render"
    
    // For Android Emulator - connects to localhost on host machine
    private const val EMULATOR_BASE_URL = "http://10.0.2.2:8000/"
    
    // For Physical Device - connects to your PC's IP address
    // Make sure your PC and phone are on the same network
    private const val PHYSICAL_DEVICE_BASE_URL = "http://192.168.18.194:8000/"
    
    // Production URL - Render deployment
    private const val RENDER_BASE_URL = "https://facerecon-api.onrender.com/"
    
    fun getBaseUrl(): String {
        return when (ENVIRONMENT) {
            "emulator" -> EMULATOR_BASE_URL
            "physical" -> PHYSICAL_DEVICE_BASE_URL
            "render" -> RENDER_BASE_URL
            else -> RENDER_BASE_URL // Default to Render
        }
    }
    
    fun getCurrentConfig(): String {
        return when (ENVIRONMENT) {
            "emulator" -> "Emulator Mode - Connecting to: $EMULATOR_BASE_URL"
            "physical" -> "Physical Device Mode - Connecting to: $PHYSICAL_DEVICE_BASE_URL"
            "render" -> "Production Mode - Connecting to: $RENDER_BASE_URL"
            else -> "Production Mode - Connecting to: $RENDER_BASE_URL"
        }
    }
    
    /**
     * Builds the complete image URL by concatenating base URL with the image path
     * @param imagePath The relative path from the API response (e.g., "uploads/user_123.jpg")
     * @return Complete HTTPS URL for the image
     */
    fun buildImageUrl(imagePath: String?): String? {
        if (imagePath.isNullOrBlank()) return null
        
        val baseUrl = getBaseUrl()
        return if (imagePath.startsWith("http")) {
            imagePath // Already a complete URL
        } else {
            baseUrl + imagePath.removePrefix("/")
        }
    }
} 