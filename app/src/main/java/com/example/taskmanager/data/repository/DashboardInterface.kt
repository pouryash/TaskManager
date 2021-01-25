package com.example.taskmanager.data.repository

import com.example.taskmanager.data.models.BaseModel
import com.example.taskmanager.data.models.TaskModel
import com.example.taskmanager.data.models.UserModel
import com.example.taskmanager.utils.ResultWrapper
import io.reactivex.Single

interface DashboardInterface {

    suspend fun getUserTasks(): ResultWrapper<BaseModel<ArrayList<TaskModel>>>

    suspend fun createTask(taskModel: TaskModel): ResultWrapper<BaseModel<TaskModel>>

    suspend fun searchTask(taskModel: TaskModel): ResultWrapper<BaseModel<ArrayList<TaskModel>>>

}