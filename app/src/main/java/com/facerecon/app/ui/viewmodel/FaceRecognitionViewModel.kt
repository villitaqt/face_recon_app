package com.facerecon.app.ui.viewmodel

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.facerecon.app.data.models.RecognitionResponse
import com.facerecon.app.data.models.User
import com.facerecon.app.data.models.UserRegistrationRequest
import com.facerecon.app.data.repository.FaceRecognitionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FaceRecognitionViewModel @Inject constructor(
    private val repository: FaceRecognitionRepository
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(FaceRecognitionUiState())
    val uiState: StateFlow<FaceRecognitionUiState> = _uiState.asStateFlow()
    
    fun checkBackendHealth() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                error = null
            )
            
            repository.checkHealth()
                .onSuccess { healthResponse ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        healthStatus = healthResponse,
                        error = null
                    )
                }
                .onFailure { exception ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = "Backend connection failed: ${exception.message}"
                    )
                }
        }
    }
    
    fun captureImage(bitmap: Bitmap) {
        _uiState.value = _uiState.value.copy(
            capturedImage = bitmap,
            isLoading = true,
            error = null,
            alertTriggered = false
        )
        
        viewModelScope.launch {
            repository.recognizeFace(bitmap)
                .onSuccess { response ->
                    val alertTriggered = response.alertTriggered || 
                        (response.user?.isWanted == true)
                    
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        recognitionResult = response,
                        alertTriggered = alertTriggered,
                        error = null
                    )
                }
                .onFailure { exception ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = exception.message ?: "Unknown error occurred"
                    )
                }
        }
    }
    
    fun loadAllUsers() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                error = null
            )
            
            repository.getAllUsers()
                .onSuccess { users ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        users = users,
                        error = null
                    )
                }
                .onFailure { exception ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = "Failed to load users: ${exception.message}"
                    )
                }
        }
    }
    
    fun registerUser(
        nombre: String,
        apellido: String,
        email: String,
        telefono: String,
        requisitoriado: Boolean,
        foto: Bitmap
    ) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            
            repository.registerUser(nombre, apellido, email, telefono, requisitoriado, foto)
                .onSuccess { user ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        users = _uiState.value.users + user,
                        alertMessage = "Usuario registrado exitosamente: ${user.nombre} ${user.apellido}",
                        showAlert = true
                    )
                }
                .onFailure { exception ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        alertMessage = "Error al registrar usuario: ${exception.message}",
                        showAlert = true
                    )
                }
        }
    }
    
    fun updateUser(id: String, user: UserRegistrationRequest) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                error = null
            )
            
            repository.updateUser(id, user)
                .onSuccess { updatedUser ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        lastUpdatedUser = updatedUser,
                        error = null
                    )
                    // Reload users list
                    loadAllUsers()
                }
                .onFailure { exception ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = "Failed to update user: ${exception.message}"
                    )
                }
        }
    }
    
    fun deleteUser(id: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                error = null
            )
            
            repository.deleteUser(id)
                .onSuccess {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = null
                    )
                    // Reload users list
                    loadAllUsers()
                }
                .onFailure { exception ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = "Failed to delete user: ${exception.message}"
                    )
                }
        }
    }
    
    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
    
    fun clearResult() {
        _uiState.value = _uiState.value.copy(
            recognitionResult = null,
            capturedImage = null,
            alertTriggered = false
        )
    }
    
    fun clearAlert() {
        _uiState.value = _uiState.value.copy(alertTriggered = false)
    }

    fun hideAlert() {
        _uiState.value = _uiState.value.copy(showAlert = false)
    }
}

data class FaceRecognitionUiState(
    val capturedImage: Bitmap? = null,
    val recognitionResult: RecognitionResponse? = null,
    val healthStatus: Map<String, String>? = null,
    val users: List<User> = emptyList(),
    val lastRegisteredUser: User? = null,
    val lastUpdatedUser: User? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val alertTriggered: Boolean = false,
    val showAlert: Boolean = false,
    val alertMessage: String = ""
) 