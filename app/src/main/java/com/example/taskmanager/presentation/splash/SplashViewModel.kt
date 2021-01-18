package com.example.taskmanager.presentation.splash

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskmanager.data.repository.impl.UserRepository
import kotlinx.coroutines.launch

class SplashViewModel(private val userRepository: UserRepository) : ViewModel() {

    var tokenLivedata =  MutableLiveData<String>()


    fun readUserInfo(){
        viewModelScope.launch {
           tokenLivedata.value = userRepository.readUserInfo().token
        }
    }


}