package com.example.taskmanager.data.api

import com.example.taskmanager.data.models.BaseModel
import com.example.taskmanager.data.models.UserModel
import retrofit2.http.Body
import retrofit2.http.POST


interface WebServices {

    @POST("/api/createUsers")
    suspend fun signUp(@Body userModel: UserModel): BaseModel<UserModel>

    @POST("/api/userLogin")
    suspend fun login(@Body userModel: UserModel): BaseModel<UserModel>

}