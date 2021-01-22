package com.example.taskmanager.presentation.dashboard.addTask

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskmanager.data.models.BaseModel
import com.example.taskmanager.data.models.TaskModel
import com.example.taskmanager.data.models.UserModel
import com.example.taskmanager.data.repository.impl.DashboardRepository
import com.example.taskmanager.data.repository.impl.UserRepository
import com.example.taskmanager.utils.RIM
import com.example.taskmanager.utils.ResultWrapper
import com.example.taskmanager.utils.Status
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class AddTaskViewModel(private val userRepository: UserRepository, private val dashboardRepository: DashboardRepository) : ViewModel() {

    val userInfoFlow = MutableStateFlow(UserModel())
    val usersFlow = MutableStateFlow<RIM<BaseModel<ArrayList<UserModel>>>>(RIM(state = Status.EMPTY))
    val taskFlow = MutableStateFlow<RIM<BaseModel<TaskModel>>>(RIM(state = Status.EMPTY))

    init {
        getUser()
    }

    fun getUserInfo(){
        viewModelScope.launch {
            userInfoFlow.value = userRepository.readUserInfo()
        }
    }

    fun getUser() {
        viewModelScope.launch {
            usersFlow.value = RIM(state = Status.LOADING)
            when (val result = userRepository.getUsers()) {
                is ResultWrapper.Success -> usersFlow.value = RIM(state = Status.SUCCESSFUL, data = result.value)

                is ResultWrapper.NetworkError -> usersFlow.value = RIM(state = Status.ERROR, error = result.error?.message)

                is ResultWrapper.GenericError -> usersFlow.value = RIM(state = Status.ERROR, error = result.error)
            }
        }
    }

    fun createTask(taskModel: TaskModel) {
        viewModelScope.launch {
            taskFlow.value = RIM(state = Status.LOADING)
            when (val result = dashboardRepository.createTask(taskModel)) {
                is ResultWrapper.Success -> taskFlow.value = RIM(state = Status.SUCCESSFUL, data = result.value)

                is ResultWrapper.NetworkError -> taskFlow.value = RIM(state = Status.ERROR, error = result.error?.message)

                is ResultWrapper.GenericError -> taskFlow.value = RIM(state = Status.ERROR, error = result.error)
            }
        }
    }

}