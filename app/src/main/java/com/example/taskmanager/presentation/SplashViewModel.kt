package com.example.taskmanager.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskmanager.data.models.BaseModel
import com.example.taskmanager.data.models.UserModel
import com.example.taskmanager.data.repository.UserRepository
import com.example.taskmanager.utils.RIM
import com.example.taskmanager.utils.ResultWrapper
import com.example.taskmanager.utils.Status
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class SplashViewModel(private val userRepository: UserRepository) : ViewModel() {

    var tokenLivedata =  MutableLiveData<String>()


    fun readUserInfo(){
        viewModelScope.launch {
           tokenLivedata.value = userRepository.readUserInfo().token
        }
    }


}