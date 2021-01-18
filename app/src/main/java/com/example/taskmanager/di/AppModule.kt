package com.example.taskmanager.di

import com.example.taskmanager.presentation.splash.SplashViewModel
import com.example.taskmanager.presentation.authentication.AuthenticationViewModel
import com.example.taskmanager.presentation.dashboard.home.DashboardViewModel
import com.example.taskmanager.presentation.dashboard.profile.ProfileViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module


val appModule = module {

    viewModel { AuthenticationViewModel(get()) }

    viewModel { SplashViewModel(get()) }

    viewModel { ProfileViewModel(get()) }

    viewModel { DashboardViewModel(get(), get()) }


}