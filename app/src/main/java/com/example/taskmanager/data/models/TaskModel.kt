package com.example.taskmanager.data.models

import com.google.gson.annotations.SerializedName

data class TaskModel(
    @SerializedName("id")
    val id: Long,
    @SerializedName("taskName")
    val taskName: String = "",
    @SerializedName("description")
    val description: String = "",
    @SerializedName("reporter")
    val reporter: String = "",
    @SerializedName("priority")
    val priority: String = "",
    @SerializedName("status")
    val status: String = "",
    @SerializedName("loggedTime")
    val loggedTime: String = "",
    @SerializedName("createDate")
    val createDate: String = "",
    @SerializedName("endDate")
    val endDate: String = ""
)
