package com.facerecon.app.data.repository

import android.graphics.Bitmap
import com.facerecon.app.data.api.ApiService
import com.facerecon.app.data.models.RecognitionResponse
import com.facerecon.app.data.models.User
import com.facerecon.app.data.models.UserRegistrationRequest
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject

class FaceRecognitionRepository @Inject constructor(
    private val apiService: ApiService
) {
    suspend fun checkHealth(): Result<Map<String, String>> {
        return try {
            val response = apiService.checkHealth()
            if (response.isSuccessful) {
                response.body()?.let {
                    Result.success(it)
                } ?: Result.failure(Exception("Empty response body"))
            } else {
                Result.failure(Exception("Health check failed: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun recognizeFace(bitmap: Bitmap): Result<RecognitionResponse> {
        return try {
            val imageFile = bitmapToFile(bitmap)
            val requestBody = imageFile.asRequestBody("image/jpeg".toMediaTypeOrNull())
            val imagePart = MultipartBody.Part.createFormData("face_image", "face.jpg", requestBody)
            
            val response = apiService.recognizeFace(imagePart)
            
            if (response.isSuccessful) {
                response.body()?.let {
                    Result.success(it)
                } ?: Result.failure(Exception("Empty response body"))
            } else {
                Result.failure(Exception("API call failed: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun getAllUsers(): Result<List<User>> {
        return try {
            val response = apiService.getAllUsers()
            if (response.isSuccessful) {
                response.body()?.let {
                    Result.success(it)
                } ?: Result.failure(Exception("Empty response body"))
            } else {
                Result.failure(Exception("Failed to get users: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun getUserById(id: String): Result<User> {
        return try {
            val response = apiService.getUserById(id)
            if (response.isSuccessful) {
                response.body()?.let {
                    Result.success(it)
                } ?: Result.failure(Exception("Empty response body"))
            } else {
                Result.failure(Exception("Failed to get user: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun registerUser(
        nombre: String,
        apellido: String,
        email: String,
        telefono: String,
        requisitoriado: Boolean,
        foto: Bitmap
    ): Result<User> {
        return try {
            val imageFile = bitmapToFile(foto)
            val requestBody = imageFile.asRequestBody("image/jpeg".toMediaTypeOrNull())
            val imagePart = MultipartBody.Part.createFormData("foto", "user_photo.jpg", requestBody)
            
            val nombreBody = nombre.toRequestBody("text/plain".toMediaTypeOrNull())
            val apellidoBody = apellido.toRequestBody("text/plain".toMediaTypeOrNull())
            val emailBody = email.toRequestBody("text/plain".toMediaTypeOrNull())
            val telefonoBody = telefono.toRequestBody("text/plain".toMediaTypeOrNull())
            val requisitoriadoBody = requisitoriado.toString().toRequestBody("text/plain".toMediaTypeOrNull())
            
            val response = apiService.registerUser(
                nombreBody, apellidoBody, emailBody, telefonoBody, requisitoriadoBody, imagePart
            )
            
            if (response.isSuccessful) {
                response.body()?.let {
                    Result.success(it)
                } ?: Result.failure(Exception("Empty response body"))
            } else {
                Result.failure(Exception("Failed to register user: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun updateUser(id: String, user: UserRegistrationRequest): Result<User> {
        return try {
            val response = apiService.updateUser(id, user)
            if (response.isSuccessful) {
                response.body()?.let {
                    Result.success(it)
                } ?: Result.failure(Exception("Empty response body"))
            } else {
                Result.failure(Exception("Failed to update user: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun deleteUser(id: String): Result<Map<String, String>> {
        return try {
            val response = apiService.deleteUser(id)
            if (response.isSuccessful) {
                response.body()?.let {
                    Result.success(it)
                } ?: Result.failure(Exception("Empty response body"))
            } else {
                Result.failure(Exception("Failed to delete user: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    private fun bitmapToFile(bitmap: Bitmap): File {
        val file = File.createTempFile("face", ".jpg")
        val outputStream = FileOutputStream(file)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, outputStream)
        outputStream.close()
        return file
    }
} 