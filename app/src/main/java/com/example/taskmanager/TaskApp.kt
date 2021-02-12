package com.example.taskmanager

import android.app.Application
import androidContextModule
import androidx.appcompat.app.AppCompatDelegate
import androidx.multidex.MultiDexApplication
import com.example.taskmanager.di.appModule
import com.example.taskmanager.di.netModule
import com.example.taskmanager.di.repoModule
import org.koin.android.ext.koin.androidContext

class TaskApp: MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()

        init()
        startKoin()
    }

    private fun init(){
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
    }

    private fun startKoin() {
        org.koin.core.context.startKoin {
            androidContext(this@TaskApp)

            modules(listOf(netModule, repoModule, appModule, androidContextModule))
        }
    }

}