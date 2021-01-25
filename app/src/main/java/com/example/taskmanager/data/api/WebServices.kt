package com.example.taskmanager.data.api

import com.example.taskmanager.data.models.BaseModel
import com.example.taskmanager.data.models.TaskModel
import com.example.taskmanager.data.models.UserModel
import retrofit2.http.*


interface WebServices {

    @POST("/api/createUsers")
    suspend fun signUp(@Body userModel: UserModel): BaseModel<UserModel>

    @POST("/api/userLogin")
    suspend fun login(@Body userModel: UserModel): BaseModel<UserModel>

    @GET("/api/userTasks")
    suspend fun getUserTasks(): BaseModel<ArrayList<TaskModel>>

    @GET("/api/users")
    suspend fun getUser(): BaseModel<ArrayList<UserModel>>

    @PUT("/api/updateUser")
    suspend fun updateUser(@Body userModel: UserModel): BaseModel<UserModel>

    @POST("/api/createTask")
    suspend fun createTask(@Body taskModel: TaskModel): BaseModel<TaskModel>

    @POST("/api/searchTask")
    suspend fun searchTask(@Body taskModel: TaskModel): BaseModel<ArrayList<TaskModel>>

}