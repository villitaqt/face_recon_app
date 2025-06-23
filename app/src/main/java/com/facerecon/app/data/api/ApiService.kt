package com.facerecon.app.data.api

import com.facerecon.app.data.models.RecognitionResponse
import com.facerecon.app.data.models.User
import com.facerecon.app.data.models.UserRegistrationRequest
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface ApiService {
    @GET("health")
    suspend fun checkHealth(): Response<Map<String, String>>
    
    @Multipart
    @POST("recognize")
    suspend fun recognizeFace(
        @Part face_image: MultipartBody.Part
    ): Response<RecognitionResponse>
    
    // User Management CRUD endpoints
    @GET("usuarios/")
    suspend fun getAllUsers(): Response<List<User>>
    
    @GET("usuarios/{id}")
    suspend fun getUserById(@Path("id") id: String): Response<User>
    
    @Multipart
    @POST("usuarios/")
    suspend fun registerUser(
        @Part("nombre") nombre: RequestBody,
        @Part("apellido") apellido: RequestBody,
        @Part("email") email: RequestBody,
        @Part("telefono") telefono: RequestBody,
        @Part("requisitoriado") requisitoriado: RequestBody,
        @Part foto: MultipartBody.Part
    ): Response<User>
    
    @PUT("usuarios/{id}")
    suspend fun updateUser(
        @Path("id") id: String,
        @Body user: UserRegistrationRequest
    ): Response<User>
    
    @DELETE("usuarios/{id}")
    suspend fun deleteUser(@Path("id") id: String): Response<Map<String, String>>
} 