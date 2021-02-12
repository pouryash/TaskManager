package com.example.taskmanager.data.models

import com.google.gson.annotations.SerializedName

data class BaseModel<T>(
    @SerializedName("status")
    val status: Int? = null,
    @SerializedName("message")
    val message: String,
    @SerializedName("data")
    val data: T?
)
