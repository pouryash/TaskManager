package com.example.taskmanager.presentation.dashboard.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskmanager.data.models.UserModel
import com.example.taskmanager.data.repository.impl.DashboardRepository
import com.example.taskmanager.data.repository.impl.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class ProfileViewModel(private val userRepository: UserRepository) : ViewModel() {

    val userInfoFlow = MutableStateFlow<UserModel>(UserModel())

    fun getUserInfo(){
        viewModelScope.launch {
           userInfoFlow.value = userRepository.readUserInfo()
        }
    }
}