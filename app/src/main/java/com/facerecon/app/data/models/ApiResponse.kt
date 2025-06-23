package com.facerecon.app.data.models

import com.google.gson.annotations.SerializedName

data class RecognitionResponse(
    @SerializedName("success")
    val success: Boolean,
    @SerializedName("user")
    val user: User? = null,
    @SerializedName("message")
    val message: String? = null,
    @SerializedName("distance")
    val distance: Double? = null,
    @SerializedName("threshold")
    val threshold: Double? = null,
    @SerializedName("alert_triggered")
    val alertTriggered: Boolean = false
)

data class ErrorResponse(
    @SerializedName("error")
    val error: String,
    @SerializedName("message")
    val message: String
) 