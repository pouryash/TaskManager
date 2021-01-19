package com.example.taskmanager.data.repository.impl

import com.example.taskmanager.data.AppPreference
import com.example.taskmanager.data.PreferencesKeys
import com.example.taskmanager.data.api.WebServices
import com.example.taskmanager.data.models.BaseModel
import com.example.taskmanager.data.models.UserModel
import com.example.taskmanager.data.repository.UserRepository
import com.example.taskmanager.utils.ResultWrapper
import com.example.taskmanager.utils.SafeApiCaller
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.first
import java.lang.Exception


class UserRepository (private val api: WebServices, private val dispatcher: CoroutineDispatcher,
                      private val apiCaller: SafeApiCaller, private val appPreference: AppPreference
) : UserRepository {

    override suspend fun signUp(userModel: UserModel): ResultWrapper<BaseModel<UserModel>> {

       return apiCaller.safeApiCall(dispatcher){
            api.signUp(userModel)
        }
    }

    override suspend fun login(userModel: UserModel): ResultWrapper<BaseModel<UserModel>> {
        return apiCaller.safeApiCall(dispatcher){
            api.login(userModel)
        }
    }

    override suspend fun updateUser(userModel: UserModel): ResultWrapper<BaseModel<UserModel>> {
        return apiCaller.safeApiCall(dispatcher){
            api.updateUser(userModel)
        }
    }

    override suspend fun saveUserInfo(userModel: UserModel) {
        try {
            appPreference.save(PreferencesKeys.USERINFO, userModel)
        }catch (e: Exception){
            val a = 0
        }
    }

    override suspend fun readUserInfo(): UserModel {
        var result= UserModel()
        appPreference.readUserModel(PreferencesKeys.USERINFO).first()?.let {
            result = (it as UserModel)
        }
        return result
    }

    override suspend fun removeUserInfo() {
        appPreference.remove(PreferencesKeys.USERINFO)
    }


}