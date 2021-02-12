package com.example.taskmanager.di


import com.example.taskmanager.data.repository.impl.DashboardRepository
import com.example.taskmanager.data.repository.impl.UserRepository
import com.example.taskmanager.utils.SafeApiCaller
import kotlinx.coroutines.Dispatchers
import org.koin.dsl.module

val repoModule = module {

    single { UserRepository(get(), Dispatchers.IO, SafeApiCaller(), get()) }

    single { DashboardRepository(get(), Dispatchers.IO, SafeApiCaller(), get()) }

}