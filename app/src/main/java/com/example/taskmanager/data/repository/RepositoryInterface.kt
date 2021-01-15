package com.example.taskmanager.data.repository

import com.example.taskmanager.data.models.BaseModel
import com.example.taskmanager.data.models.UserModel
import com.example.taskmanager.utils.ResultWrapper
import io.reactivex.Single

interface RepositoryInterface {

    suspend fun signUp(userModel: UserModel): ResultWrapper<BaseModel<UserModel>>

    suspend fun login(userModel: UserModel): ResultWrapper<BaseModel<UserModel>>

    suspend fun saveUserInfo(userModel: UserModel)

    suspend fun readUserInfo(): UserModel

}