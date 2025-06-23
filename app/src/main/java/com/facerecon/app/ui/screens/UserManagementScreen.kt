package com.facerecon.app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.facerecon.app.ui.viewmodel.FaceRecognitionViewModel
import com.facerecon.app.ui.components.UserImageWithFallback

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserManagementScreen(
    onNavigateToRegister: () -> Unit,
    onNavigateToEdit: (String) -> Unit,
    onBackPressed: () -> Unit,
    viewModel: FaceRecognitionViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    
    LaunchedEffect(Unit) {
        viewModel.loadAllUsers()
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Gestión de usuarios") },
                navigationIcon = {
                    IconButton(onClick = onBackPressed) {
                        Text("←")
                    }
                },
                actions = {
                    IconButton(onClick = onNavigateToRegister) {
                        Text("+")
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
            if (uiState.isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else if (uiState.users.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "No se encontraron usuarios",
                            style = MaterialTheme.typography.headlineSmall,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(onClick = onNavigateToRegister) {
                            Text("Registrar primer usuario")
                        }
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(uiState.users) { user ->
                        UserCard(
                            user = user,
                            onEdit = { onNavigateToEdit(user.id ?: "") },
                            onDelete = { viewModel.deleteUser(user.id ?: "") }
                        )
                    }
                }
            }
            
            // Error Display
            uiState.error?.let { error ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
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
        }
    }
}

@Composable
fun UserCard(
    user: com.facerecon.app.data.models.User,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // User Image
            UserImageWithFallback(
                imageUrl = user.urlFoto,
                userName = user.fullName,
                modifier = Modifier.padding(end = 16.dp),
                size = 56
            )
            
            // User Info
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = user.fullName,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = user.email ?: "Sin correo",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = user.telefono ?: "Sin teléfono",
                    style = MaterialTheme.typography.bodySmall
                )
                if (user.isWanted) {
                    Text(
                        text = "⚠️ PERSONA REQUISITORIADA",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
            
            // Action Buttons
            Row {
                IconButton(onClick = onEdit) {
                    Text("✏️")
                }
                IconButton(onClick = onDelete) {
                    Text("🗑️")
                }
            }
        }
    }
} 