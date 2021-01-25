package com.example.taskmanager.presentation.dashboard.home

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

class DashboardViewModel(private val userRepository: UserRepository, private val dashboardRepository: DashboardRepository) : ViewModel() {

    val userInfoFlow = MutableStateFlow(UserModel())
    val userTasksFlow = MutableStateFlow<RIM<BaseModel<ArrayList<TaskModel>>>>(RIM(state = Status.EMPTY))

    fun getUserTasks() {
        viewModelScope.launch {
            userTasksFlow.value = RIM(state = Status.LOADING)
            when (val result = dashboardRepository.getUserTasks()) {
                is ResultWrapper.Success -> userTasksFlow.value = RIM(state = Status.SUCCESSFUL, data = result.value)

                is ResultWrapper.NetworkError -> userTasksFlow.value = RIM(state = Status.ERROR, error = result.error?.message)

                is ResultWrapper.GenericError -> userTasksFlow.value = RIM(state = Status.ERROR, error = result.error)
            }
        }
    }

    fun searchTask(taskModel: TaskModel) {
        viewModelScope.launch {
            userTasksFlow.value = RIM(state = Status.LOADING)
            when (val result = dashboardRepository.searchTask(taskModel)) {
                is ResultWrapper.Success -> userTasksFlow.value = RIM(state = Status.SUCCESSFUL, data = result.value)

                is ResultWrapper.NetworkError -> userTasksFlow.value = RIM(state = Status.ERROR, error = result.error?.message)

                is ResultWrapper.GenericError -> userTasksFlow.value = RIM(state = Status.ERROR, error = result.error)
            }
        }
    }

    fun getUserInfo(){
        viewModelScope.launch {
           userInfoFlow.value = userRepository.readUserInfo()
        }
    }
}