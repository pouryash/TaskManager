package com.example.taskmanager.data.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TaskModel(
    @SerializedName("id")
    val id: Long = 0,
    @SerializedName("taskName")
    val taskName: String = "",
    @SerializedName("description")
    val description: String = "",
    @SerializedName("reporter")
    val reporter: String = "",
    @SerializedName("priority")
    var priority: String = "",
    @SerializedName("status")
    var status: String = "",
    @SerializedName("loggedTime")
    var loggedTime: String = "",
    @SerializedName("createDate")
    val createDate: String = "",
    @SerializedName("endDate")
    val endDate: String = "",
    @SerializedName("userId")
    val userId: Long = 0
): Parcelable
