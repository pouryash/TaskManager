package com.example.taskmanager.data.models

import com.google.gson.annotations.SerializedName

data class UserModel(
    @SerializedName("userName")
    val name: String = "",
    @SerializedName("password")
    val password: String = "",
    @SerializedName("email")
    val email: String = "",
    @SerializedName("role")
    val role: String = "",
    @SerializedName("token")
    val token: String = "",
    @SerializedName("id")
    val userId: Long = 0
)
