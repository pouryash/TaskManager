//package com.example.taskmanager.di
//
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import com.example.taskmanager.AppPreference
import com.example.taskmanager.data.PreferencesKeys
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import okhttp3.Interceptor
import okhttp3.Response
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import java.io.IOException
import java.net.HttpURLConnection
import java.net.InetSocketAddress
import java.net.Socket
import java.net.SocketTimeoutException
import kotlin.coroutines.CoroutineContext

private const val AUTHORIZATION_TAG = "Authorization"

val androidContextModule = module {

    single {
        AppPreference(androidContext())
    }

}

class HeaderCheckInterceptor(val appPreference: AppPreference) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val response = chain.proceed(chain.request())
        if (response.code() == HttpURLConnection.HTTP_UNAUTHORIZED) {
            CoroutineScope(Dispatchers.IO + Job()).launch {
                appPreference.save(PreferencesKeys.AUTHORIZATION, "")
            }
        }
        return response
    }
}

class AuthHeaderInterceptor(val appPreference: AppPreference) : Interceptor {
    lateinit var token: String
    val job = CoroutineScope(Dispatchers.IO + Job()).launch {
        token = appPreference.read(PreferencesKeys.AUTHORIZATION).first()
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        return if (this::token.isInitialized && token.isNotEmpty()) {
            val newReq = chain.request().newBuilder().addHeader(AUTHORIZATION_TAG, "Bearer $token")
                .build()
            Log.d("BearerJWT", "$token")
            chain.proceed(newReq)
        } else
            chain.proceed(chain.request())
    }
}


class ConnectivityInterceptor (private val context: Context) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        return if (!isConnectionOn()) {
            throw NoConnectivityException()
        } else if (!isInternetAvailable()) {
            throw NoInternetException()
        } else {
            timeoutIntercept(chain)
            //chain.proceed(chain.request())
        }
    }

    private fun isConnectionOn(): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            val network = connectivityManager.activeNetwork
            val connection = connectivityManager.getNetworkCapabilities(network)
            return connection != null && (
                    connection.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                            connection.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR))

        } else {
            val activeNetwork = connectivityManager.activeNetworkInfo
            if (activeNetwork != null) {
                return (activeNetwork.type == ConnectivityManager.TYPE_WIFI ||
                        activeNetwork.type == ConnectivityManager.TYPE_MOBILE)
            }
            return false
        }
    }

    private fun isInternetAvailable(): Boolean {
        return try {
            val timeoutMs = 1500
            val sock = Socket()
            val sockaddr = InetSocketAddress("8.8.8.8", 53)
            sock.connect(sockaddr, timeoutMs)
            sock.close()
            true
        } catch (e: IOException) {
            false
        }
    }

    class NoConnectivityException : IOException() {
        override val message: String
            get() = "اتصال به اینترنت را بررسی کنید و مجدد تلاش کنید"
    }

    class NoInternetException() : IOException() {
        override val message: String
            get() = "اتصال به اینترنت را بررسی کنید و مجدد تلاش کنید"
    }

    class TimeoutException() : SocketTimeoutException() {
        override val message: String
            get() = "پاسخی از سرور دریافت نشد"
    }

    @Throws(IOException::class)
    private fun timeoutIntercept(chain: Interceptor.Chain): Response {
        try {
            return chain.proceed(chain.request())
        } catch (exception: SocketTimeoutException) {
            throw TimeoutException()
        }
    }
}
