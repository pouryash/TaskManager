package com.example.taskmanager.data.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class FilterTaskModel(
    @SerializedName("userName")
    var user: String = "",
    @SerializedName("priority")
    var priority: String = "",
    @SerializedName("status")
    var status: String = "",
    @SerializedName("fromDate")
    var fromDate: String = "",
    @SerializedName("toDate")
    var toDate: String = "",

): Parcelable
