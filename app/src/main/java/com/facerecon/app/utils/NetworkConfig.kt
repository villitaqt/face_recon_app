package com.facerecon.app.utils

object NetworkConfig {
    // Configuration for different environments
    // Set this to true for emulator, false for physical device
    const val USE_EMULATOR = false
    
    // For Android Emulator - connects to localhost on host machine
    private const val EMULATOR_BASE_URL = "http://10.0.2.2:8000/"
    
    // For Physical Device - connects to your PC's IP address
    // Make sure your PC and phone are on the same network
    private const val PHYSICAL_DEVICE_BASE_URL = "http://192.168.18.194:8000/"
    
    fun getBaseUrl(): String {
        return if (USE_EMULATOR) EMULATOR_BASE_URL else PHYSICAL_DEVICE_BASE_URL
    }
    
    fun getCurrentConfig(): String {
        return if (USE_EMULATOR) {
            "Emulator Mode - Connecting to: $EMULATOR_BASE_URL"
        } else {
            "Physical Device Mode - Connecting to: $PHYSICAL_DEVICE_BASE_URL"
        }
    }
} 