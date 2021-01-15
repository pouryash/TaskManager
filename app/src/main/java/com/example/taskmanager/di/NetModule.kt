package com.example.taskmanager.di

import AuthHeaderInterceptor
import ConnectivityInterceptor
import HeaderCheckInterceptor
import android.content.Context
import com.example.taskmanager.data.AppPreference
import com.example.taskmanager.BuildConfig
import com.example.taskmanager.data.api.WebServices
import com.example.taskmanager.utils.ConstUtils
import com.readystatesoftware.chuck.ChuckInterceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit


const val BASE_URL = "BASE_URL"
private const val DI_TAG = "DI_TAG"


val netModule = module {

    single(named(BASE_URL)) {
        ConstUtils.BASE_URL
    }

    single(named(DI_TAG)) {
        val baseUrl: String = get(named(BASE_URL))
        val client = createOKHttpClientWithAuth(androidContext(), get())
        createRetrofit(
            client,
            baseUrl
        )
    }

    single<WebServices> { createRetrofitService(get(named(DI_TAG))) }

}

private val loggingInterceptor = HttpLoggingInterceptor().apply {
    level = HttpLoggingInterceptor.Level.BODY
}


private const val CONNECT_TIMEOUT = 5L
private const val READ_TIMEOUT = 10L
private const val WRITE_TIMEOUT = 10L

private fun createOKHttpClientNoAuth(context: Context,): OkHttpClient {


    val okhttp = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .addInterceptor(ConnectivityInterceptor(context))
        .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
        .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
        .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)

    if (BuildConfig.DEBUG) {
        return okhttp.addInterceptor(ChuckInterceptor(context))
            .build()
    } else {
        return okhttp.build()
    }
}

fun createOKHttpClientWithAuth(
    context: Context,
    appPreference: AppPreference
): OkHttpClient {


    val okhttp = OkHttpClient.Builder()
        .addInterceptor(HeaderCheckInterceptor(appPreference))
        .addInterceptor(AuthHeaderInterceptor(appPreference))
        .addInterceptor(loggingInterceptor)
        .addInterceptor(ConnectivityInterceptor(context))
        .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
        .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
        .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)

    if (BuildConfig.DEBUG) {
        return okhttp.addInterceptor(ChuckInterceptor(context))
            .build()
    } else {
        return okhttp.build()
    }

}

fun createRetrofit(client: OkHttpClient, baseUrl: String): Retrofit =
    Retrofit.Builder()
        .client(client)
        .addConverterFactory(ScalarsConverterFactory.create())
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(baseUrl)
        .build()

inline fun <reified T : Any> createRetrofitService(retrofit: Retrofit): T =
    retrofit.create(T::class.java)
