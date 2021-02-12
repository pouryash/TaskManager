package com.example.taskmanager.presentation.authentication

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskmanager.data.models.BaseModel
import com.example.taskmanager.data.models.UserModel
import com.example.taskmanager.data.repository.impl.UserRepository
import com.example.taskmanager.utils.RIM
import com.example.taskmanager.utils.ResultWrapper
import com.example.taskmanager.utils.Status
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class AuthenticationViewModel(private val userRepository: UserRepository) : ViewModel() {

    var registerFlow =  MutableStateFlow<RIM<BaseModel<UserModel>>>(RIM(state = Status.EMPTY))

    fun registerUser(userModel: UserModel) {
        viewModelScope.launch {
            registerFlow.value = RIM(state = Status.LOADING)
            when (val result = userRepository.signUp(userModel)) {
                is ResultWrapper.Success -> registerFlow.value = RIM(state = Status.SUCCESSFUL, data = result.value)

                is ResultWrapper.NetworkError -> registerFlow.value = RIM(state = Status.ERROR, error = result.error?.message)

                is ResultWrapper.GenericError -> registerFlow.value = RIM(state = Status.ERROR, error = result.error)
            }
        }
    }

    fun loginUser(userModel: UserModel) {
        viewModelScope.launch {
            registerFlow.value = RIM(state = Status.LOADING)
            when (val result = userRepository.login(userModel)) {
                is ResultWrapper.Success -> registerFlow.value = RIM(state = Status.SUCCESSFUL, data = result.value)

                is ResultWrapper.NetworkError -> registerFlow.value = RIM(state = Status.ERROR, error = result.error?.message)

                is ResultWrapper.GenericError -> registerFlow.value = RIM(state = Status.ERROR, error = result.error)
            }
        }
    }

    fun saveUserInfo(userModel: UserModel){
        viewModelScope.launch {
            userRepository.saveUserInfo(userModel)
        }
    }


}