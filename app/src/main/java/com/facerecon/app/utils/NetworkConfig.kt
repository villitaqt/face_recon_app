package com.facerecon.app.utils

object NetworkConfig {
    // Configuration for different environments
    // Set this to true for emulator, false for physical device, "render" for production
    const val ENVIRONMENT = AppConfig.CURRENT_ENVIRONMENT // Options: "emulator", "physical", "render"
    
    // For Android Emulator - connects to localhost on host machine
    private const val EMULATOR_BASE_URL = "http://10.0.2.2:8000/"
    
    // For Physical Device - connects to your PC's IP address
    // Make sure your PC and phone are on the same network
    private const val PHYSICAL_DEVICE_BASE_URL = "http://192.168.18.194:8000/"
    
    // Production URL - Render deployment
    private const val RENDER_BASE_URL = "https://facerecon-api.onrender.com/"
    
    fun getBaseUrl(): String {
        return AppConfig.getBaseUrl()
    }
    
    fun getCurrentConfig(): String {
        return "${AppConfig.getEnvironmentName()} - Connecting to: ${getBaseUrl()}"
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