package com.example.taskmanager.data.repository

import com.example.taskmanager.data.models.BaseModel
import com.example.taskmanager.data.models.UserModel
import com.example.taskmanager.utils.ResultWrapper
import io.reactivex.Single

interface UserRepository {

    suspend fun signUp(userModel: UserModel): ResultWrapper<BaseModel<UserModel>>

    suspend fun login(userModel: UserModel): ResultWrapper<BaseModel<UserModel>>

    suspend fun updateUser(userModel: UserModel): ResultWrapper<BaseModel<UserModel>>

    suspend fun getUsers(): ResultWrapper<BaseModel<ArrayList<UserModel>>>

    suspend fun saveUserInfo(userModel: UserModel)

    suspend fun readUserInfo(): UserModel

    suspend fun removeUserInfo()

}