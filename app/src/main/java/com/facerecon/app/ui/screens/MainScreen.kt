package com.facerecon.app.ui.screens

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.facerecon.app.ui.viewmodel.FaceRecognitionViewModel
import com.facerecon.app.ui.components.UserImageWithFallback

@Composable
fun MainScreen(
    onNavigateToCamera: () -> Unit,
    onNavigateToUserManagement: () -> Unit,
    onNavigateToUserRegistration: () -> Unit,
    viewModel: FaceRecognitionViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    
    // Alert animation for wanted persons
    val alertAnimation by animateColorAsState(
        targetValue = if (uiState.alertTriggered) Color.Red else Color.Transparent,
        animationSpec = tween(500),
        label = "alert"
    )
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(alertAnimation)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // App Title
            Text(
                text = "Aplicación de Reconocimiento Facial",
                style = MaterialTheme.typography.headlineLarge,
                textAlign = TextAlign.Center
            )
            
            // Alert Banner for Wanted Persons
            if (uiState.alertTriggered) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "🚨 ALERTA DE SEGURIDAD 🚨",
                            style = MaterialTheme.typography.titleLarge,
                            color = Color.White
                        )
                        Text(
                            text = "PERSONA REQUISITORIADA DETECTADA",
                            style = MaterialTheme.typography.bodyLarge,
                            color = Color.White
                        )
                        Text(
                            text = "Se requiere acción inmediata. Contacte a las autoridades.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.White
                        )
                        Button(
                            onClick = { viewModel.clearAlert() },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.White,
                                contentColor = MaterialTheme.colorScheme.error
                            )
                        ) {
                            Text("Reconocer alerta")
                        }
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Main Action Buttons
            Button(
                onClick = onNavigateToCamera,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                Text(
                    text = "Capturar y reconocer",
                    style = MaterialTheme.typography.titleMedium
                )
            }
            
            Button(
                onClick = onNavigateToUserManagement,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                Text(
                    text = "Gestión de usuarios",
                    style = MaterialTheme.typography.titleMedium
                )
            }
            
            Button(
                onClick = onNavigateToUserRegistration,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                Text(
                    text = "Registrar nuevo usuario",
                    style = MaterialTheme.typography.titleMedium
                )
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Backend Status Card
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Estado del backend",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    uiState.healthStatus?.let { health ->
                        Text(
                            text = "Estado: ${health["status"] ?: "Desconocido"}",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Text(
                            text = "Mensaje: ${health["message"] ?: "Sin mensaje"}",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    } ?: run {
                        Text(
                            text = "Estado: No verificado",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(
                        onClick = { viewModel.checkBackendHealth() },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Probar conexión con el backend")
                    }
                }
            }
            
            // Enhanced Recognition Result Display
            uiState.recognitionResult?.let { result ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = if (result.alertTriggered || result.user?.isWanted == true) 
                            MaterialTheme.colorScheme.errorContainer 
                        else 
                            MaterialTheme.colorScheme.surface
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "Resultado del reconocimiento",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        if (result.success && result.user != null) {
                            // User Image and Info Row
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                UserImageWithFallback(
                                    imageUrl = result.user.urlFoto,
                                    userName = result.user.fullName,
                                    modifier = Modifier.padding(end = 16.dp),
                                    size = 64
                                )
                                
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "Reconocido: ${result.user.fullName}",
                                        style = MaterialTheme.typography.bodyLarge
                                    )
                                    Text(
                                        text = "Correo: ${result.user.email}",
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                    Text(
                                        text = "Teléfono: ${result.user.telefono}",
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                }
                            }
                            
                            Spacer(modifier = Modifier.height(8.dp))
                            
                            result.user.confidence?.let { confidence ->
                                Text(
                                    text = "Confianza: ${(confidence * 100).toInt()}%",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                            result.distance?.let { distance ->
                                Text(
                                    text = "Distancia: ${String.format("%.4f", distance)}",
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                            if (result.user.isWanted) {
                                Text(
                                    text = "⚠️ PERSONA REQUISITORIADA - ALERTA DE SEGURIDAD",
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = MaterialTheme.colorScheme.error
                                )
                            }
                        } else {
                            Text(
                                text = result.message ?: "No se reconoció ningún rostro",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }
            }
            
            // Error Display
            uiState.error?.let { error ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer
                    )
                ) {
                    Text(
                        text = error,
                        modifier = Modifier.padding(16.dp),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
            
            // Loading indicator
            if (uiState.isLoading) {
                CircularProgressIndicator()
            }
            
            Spacer(modifier = Modifier.weight(1f))
        }
    }
} 