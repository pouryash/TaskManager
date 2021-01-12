package com.example.taskmanager

import android.app.Application
import androidContextModule
import androidx.appcompat.app.AppCompatDelegate
import androidx.multidex.MultiDexApplication
import com.example.taskmanager.di.appModule
import com.example.taskmanager.di.netModule
import org.koin.android.ext.koin.androidContext

class TaskApp: MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()

        startKoin()
    }

    fun startKoin() {
        org.koin.core.context.startKoin {
            androidContext(this@TaskApp)

            modules(listOf(netModule, appModule, androidContextModule))
        }

        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)

    }

}