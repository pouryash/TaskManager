package com.example.taskmanager.data.models

import com.google.gson.annotations.SerializedName

data class ErrorResponse(

    @SerializedName("status")
    val status: Int? = null,
    @SerializedName("message")
    val message: String? = ""
)