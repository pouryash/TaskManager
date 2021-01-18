package com.example.taskmanager.data.repository.impl

import com.example.taskmanager.data.AppPreference
import com.example.taskmanager.data.PreferencesKeys
import com.example.taskmanager.data.api.WebServices
import com.example.taskmanager.data.models.BaseModel
import com.example.taskmanager.data.models.TaskModel
import com.example.taskmanager.data.models.UserModel
import com.example.taskmanager.data.repository.DashboardInterface
import com.example.taskmanager.data.repository.RepositoryInterface
import com.example.taskmanager.utils.ResultWrapper
import com.example.taskmanager.utils.SafeApiCaller
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.first
import java.lang.Exception


class DashboardRepository (private val api: WebServices, private val dispatcher: CoroutineDispatcher,
                           private val apiCaller: SafeApiCaller, private val appPreference: AppPreference
): DashboardInterface {

    override suspend fun getUserTasks(): ResultWrapper<BaseModel<ArrayList<TaskModel>>> {
        return apiCaller.safeApiCall(dispatcher){
            api.getUserTasks()
        }
    }


}