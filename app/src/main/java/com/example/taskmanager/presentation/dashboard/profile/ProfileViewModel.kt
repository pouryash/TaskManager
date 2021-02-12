package com.example.taskmanager.presentation.dashboard.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskmanager.data.models.BaseModel
import com.example.taskmanager.data.models.UserModel
import com.example.taskmanager.data.repository.impl.UserRepository
import com.example.taskmanager.utils.RIM
import com.example.taskmanager.utils.ResultWrapper
import com.example.taskmanager.utils.Status
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class ProfileViewModel(private val userRepository: UserRepository) : ViewModel() {

    val userInfoFlow = MutableStateFlow<UserModel>(UserModel())
    var userFlow = MutableStateFlow<RIM<BaseModel<UserModel>>>(RIM(state = Status.EMPTY))

    fun updateUser(userModel: UserModel) {
        viewModelScope.launch {
            userFlow.value = RIM(state = Status.LOADING)
            when (val result = userRepository.updateUser(userModel)) {
                is ResultWrapper.Success -> userFlow.value =
                    RIM(state = Status.SUCCESSFUL, data = result.value)

                is ResultWrapper.NetworkError -> userFlow.value =
                    RIM(state = Status.ERROR, error = result.error?.message)

                is ResultWrapper.GenericError -> userFlow.value =
                    RIM(state = Status.ERROR, error = result.error)
            }

        }
    }

    fun getUserInfo() {
        viewModelScope.launch {
            userInfoFlow.value = userRepository.readUserInfo()
        }
    }

    fun saveUSerINfo(userModel: UserModel) {
        viewModelScope.launch {
            userRepository.saveUserInfo(userModel)
        }
    }

    fun logout() {
        viewModelScope.launch {
            userRepository.removeUserInfo()
        }
    }
}