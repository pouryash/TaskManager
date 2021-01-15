package com.example.taskmanager.di

import com.example.taskmanager.presentation.SplashViewModel
import com.example.taskmanager.presentation.authentication.AuthenticationViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module


val appModule = module {

    viewModel { AuthenticationViewModel(get()) }

    viewModel { SplashViewModel(get()) }

}