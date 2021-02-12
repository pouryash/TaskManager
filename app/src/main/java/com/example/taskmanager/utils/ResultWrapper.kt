package com.example.taskmanager.utils

import android.app.Application
import android.view.View
import com.example.taskmanager.data.models.ErrorResponse
import io.reactivex.internal.util.ErrorMode

sealed class ResultWrapper<out T> {
    data class Success<out T>(val value: T) : ResultWrapper<T>()
    data class NetworkError(val code: Int? = null, val error: ErrorResponse? = null) :
        ResultWrapper<Nothing>()

    data class GenericError(val error: String? = "") : ResultWrapper<Nothing>()
}
