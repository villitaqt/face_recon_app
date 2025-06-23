package com.facerecon.app.ui.screens

import android.Manifest
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.facerecon.app.ui.viewmodel.FaceRecognitionViewModel
import com.facerecon.app.ui.components.UserImageWithFallback
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.rememberPermissionState

@OptIn(ExperimentalPermissionsApi::class, ExperimentalMaterial3Api::class)
@Composable
fun CameraScreen(
    onBackPressed: () -> Unit,
    viewModel: FaceRecognitionViewModel = viewModel()
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val cameraPermissionState = rememberPermissionState(Manifest.permission.CAMERA)
    
    val uiState by viewModel.uiState.collectAsState()
    
    // Alert animation for wanted persons
    val alertAnimation by animateColorAsState(
        targetValue = if (uiState.alertTriggered) Color.Red else Color.Transparent,
        animationSpec = tween(500),
        label = "alert"
    )
    
    LaunchedEffect(Unit) {
        if (cameraPermissionState.status !is PermissionStatus.Granted) {
            cameraPermissionState.launchPermissionRequest()
        }
    }
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = alertAnimation)
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Cámara") },
                    navigationIcon = {
                        IconButton(onClick = onBackPressed) {
                            Text("←")
                        }
                    }
                )
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                // Alert Banner for Wanted Persons
                if (uiState.alertTriggered) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
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
                
                when (cameraPermissionState.status) {
                    is PermissionStatus.Granted -> {
                        // Camera Preview Section
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(400.dp)
                                .padding(16.dp)
                        ) {
                            CameraPreview(
                                onImageCaptured = { bitmap ->
                                    viewModel.captureImage(bitmap)
                                },
                                onError = { /* Handle error */ }
                            )
                        }
                        
                        // Capture Button
                        Button(
                            onClick = {
                                // This will be handled by the CameraPreview
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp)
                                .height(56.dp),
                            enabled = false // Disabled because capture is handled in CameraPreview
                        ) {
                            Text("Toca la vista previa para capturar")
                        }
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        // Enhanced Recognition Result Display
                        uiState.recognitionResult?.let { result ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp),
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
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp),
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
                            Box(
                                modifier = Modifier.fillMaxWidth(),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator(
                                    modifier = Modifier.padding(16.dp)
                                )
                            }
                        }
                    }
                    is PermissionStatus.Denied -> {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "Se requiere permiso de cámara",
                                style = MaterialTheme.typography.headlineSmall,
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Button(
                                onClick = { cameraPermissionState.launchPermissionRequest() }
                            ) {
                                Text("Conceder permiso")
                            }
                        }
                    }
                    else -> {
                        // Permission is being requested or in other states
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CameraPreview(
    onImageCaptured: (android.graphics.Bitmap) -> Unit,
    onError: (ImageCaptureException) -> Unit
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    
    val cameraProviderFuture = remember { ProcessCameraProvider.getInstance(context) }
    val imageCapture = remember { ImageCapture.Builder().build() }
    
    DisposableEffect(lifecycleOwner) {
        onDispose {
            cameraProviderFuture.get().unbindAll()
        }
    }
    
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        AndroidView(
            factory = { ctx ->
                val previewView = PreviewView(ctx).apply {
                    this.scaleType = PreviewView.ScaleType.FILL_CENTER
                }
                
                cameraProviderFuture.addListener({
                    val cameraProvider = cameraProviderFuture.get()
                    val preview = Preview.Builder().build().also {
                        it.setSurfaceProvider(previewView.surfaceProvider)
                    }
                    
                    val cameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA
                    
                    try {
                        cameraProvider.unbindAll()
                        cameraProvider.bindToLifecycle(
                            lifecycleOwner,
                            cameraSelector,
                            preview,
                            imageCapture
                        )
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }, ContextCompat.getMainExecutor(ctx))
                
                previewView
            },
            modifier = Modifier.fillMaxSize()
        )
        
        // Capture button overlay
        FloatingActionButton(
            onClick = {
                val executor = ContextCompat.getMainExecutor(context)
                imageCapture.takePicture(
                    executor,
                    object : ImageCapture.OnImageCapturedCallback() {
                        override fun onCaptureSuccess(image: androidx.camera.core.ImageProxy) {
                            val bitmap = image.toBitmap()
                            bitmap?.let { onImageCaptured(it) }
                            image.close()
                        }
                        
                        override fun onError(exception: ImageCaptureException) {
                            onError(exception)
                        }
                    }
                )
            },
            modifier = Modifier.align(Alignment.BottomCenter)
        ) {
            Text("📷")
        }
    }
} 