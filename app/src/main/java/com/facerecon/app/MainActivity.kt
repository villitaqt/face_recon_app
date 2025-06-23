package com.facerecon.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
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
        setContent {
            FaceRecognitionAppTheme {
                AppContent()
            }
        }
    }
}

@Composable
fun AppContent() {
    var currentScreen by remember { mutableStateOf<Screen>(Screen.Main) }
    var selectedUserId by remember { mutableStateOf<String?>(null) }
    
    when (currentScreen) {
        Screen.Main -> {
            MainScreen(
                onNavigateToCamera = {
                    currentScreen = Screen.Camera
                },
                onNavigateToUserManagement = {
                    currentScreen = Screen.UserManagement
                },
                onNavigateToUserRegistration = {
                    currentScreen = Screen.UserRegistration
                }
            )
        }
        Screen.Camera -> {
            CameraScreen(
                onBackPressed = {
                    currentScreen = Screen.Main
                }
            )
        }
        Screen.UserManagement -> {
            UserManagementScreen(
                onNavigateToRegister = {
                    currentScreen = Screen.UserRegistration
                },
                onNavigateToEdit = { userId ->
                    selectedUserId = userId
                    currentScreen = Screen.UserRegistration
                },
                onBackPressed = {
                    currentScreen = Screen.Main
                }
            )
        }
        Screen.UserRegistration -> {
            UserRegistrationScreen(
                onNavigateBack = {
                    currentScreen = Screen.Main
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