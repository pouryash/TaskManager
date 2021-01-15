package com.example.taskmanager.utils

import com.example.taskmanager.data.models.ErrorResponse
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.withContext
import org.json.JSONObject
import retrofit2.HttpException
import retrofit2.adapter.rxjava2.Result.response
import java.io.IOException


class SafeApiCaller {

    suspend fun <T> safeApiCall(
        dispatcher: CoroutineDispatcher,
        apiCall: suspend () -> T
    ): ResultWrapper<T> {

        return withContext(dispatcher) {
            try {
                ResultWrapper.Success(apiCall.invoke())
            } catch (throwable: Throwable) {
                when (throwable) {
                    is IOException -> ResultWrapper.GenericError("خطای نامشخص رخ داده است.")
                    is HttpException -> {
                        val code = throwable.code()
                        val errorResponse = convertErrorBody(throwable)
                        ResultWrapper.NetworkError(code, errorResponse)
                    }
                    is IllegalArgumentException -> ResultWrapper.GenericError("لطفا از کاراکترهای مجاز استفاده کنید.")
                    else -> {
                        ResultWrapper.NetworkError(
                            -1, ErrorResponse(
                                -1,
                                "خطای نامشخص اتفاق افتاده است."
                            )
                        )
                    }
                }
            }
        }
    }

    private val handler = CoroutineExceptionHandler { context, throwable ->

    }

    private fun convertErrorBody(throwable: HttpException): ErrorResponse? {
        return try {
            val message: ErrorResponse = Gson().fromJson(
                throwable.response()?.errorBody()?.charStream(),
                ErrorResponse::class.java
            )
            return message

        } catch (exception: Exception) {
            null
        }
    }

}