package com.facerecon.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.OnBackPressedCallback
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.facerecon.app.ui.screens.CameraScreen
import com.facerecon.app.ui.screens.MainScreen
import com.facerecon.app.ui.screens.UserManagementScreen
import com.facerecon.app.ui.screens.UserRegistrationScreen
import com.facerecon.app.ui.theme.FaceRecognitionAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        // Handle back button press
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // This will be handled by the navigation stack in AppContent
                // The back navigation is managed by each screen's onBackPressed callback
            }
        })
        
        setContent {
            FaceRecognitionAppTheme {
                AppContent()
            }
        }
    }
}

@Composable
fun AppContent() {
    var navigationStack by remember { mutableStateOf(listOf<Screen>(Screen.Main)) }
    var selectedUserId by remember { mutableStateOf<String?>(null) }
    
    // Handle back navigation
    val currentScreen = navigationStack.lastOrNull() ?: Screen.Main
    
    // Back press handling
    LaunchedEffect(Unit) {
        // This will be handled by the individual screens
    }
    
    when (currentScreen) {
        Screen.Main -> {
            MainScreen(
                onNavigateToCamera = {
                    navigationStack = navigationStack + Screen.Camera
                },
                onNavigateToUserManagement = {
                    navigationStack = navigationStack + Screen.UserManagement
                },
                onNavigateToUserRegistration = {
                    navigationStack = navigationStack + Screen.UserRegistration
                }
            )
        }
        Screen.Camera -> {
            CameraScreen(
                onBackPressed = {
                    navigationStack = navigationStack.dropLast(1)
                }
            )
        }
        Screen.UserManagement -> {
            UserManagementScreen(
                onNavigateToRegister = {
                    navigationStack = navigationStack + Screen.UserRegistration
                },
                onNavigateToEdit = { userId ->
                    selectedUserId = userId
                    navigationStack = navigationStack + Screen.UserRegistration
                },
                onBackPressed = {
                    navigationStack = navigationStack.dropLast(1)
                }
            )
        }
        Screen.UserRegistration -> {
            UserRegistrationScreen(
                onNavigateBack = {
                    navigationStack = navigationStack.dropLast(1)
                }
            )
        }
    }
}

sealed class Screen {
    object Main : Screen()
    object Camera : Screen()
    object UserManagement : Screen()
    object UserRegistration : Screen()
}