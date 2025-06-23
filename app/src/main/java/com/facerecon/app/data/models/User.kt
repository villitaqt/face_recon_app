package com.facerecon.app.data.models

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("id")
    val id: String? = null,
    @SerializedName("nombre")
    val nombre: String,
    @SerializedName("apellido")
    val apellido: String,
    @SerializedName("email")
    val email: String?,
    @SerializedName("telefono")
    val telefono: String?,
    @SerializedName("requisitoriado")
    val requisitoriado: Boolean = false,
    @SerializedName("url_foto")
    val urlFoto: String? = null,
    @SerializedName("confidence")
    val confidence: Double? = null,
    @SerializedName("distance")
    val distance: Double? = null
) {
    val fullName: String
        get() = "$nombre $apellido"
    
    val isWanted: Boolean
        get() = requisitoriado
}

data class UserRegistrationRequest(
    val nombre: String,
    val apellido: String,
    val email: String,
    val telefono: String,
    val requisitoriado: Boolean = false
) 