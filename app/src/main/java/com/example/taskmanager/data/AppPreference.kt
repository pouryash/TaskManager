package com.example.taskmanager.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.remove
import androidx.datastore.preferences.createDataStore
import com.example.taskmanager.data.models.UserModel
import com.example.taskmanager.utils.CommonUtils
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

class AppPreference(context: Context) {

    private val dataStore: DataStore<Preferences> =
        context.createDataStore(name = "user")

    suspend fun save(key: Preferences.Key<String>, value: String) {
        dataStore.edit { preferences ->
            preferences[key] = value
        }
    }

    suspend fun<T> save(key: Preferences.Key<String>, value: T) {
        dataStore.edit { preferences ->
            preferences[key] = CommonUtils.convertModelToJsonString(value)
        }
    }

    suspend fun remove(key: Preferences.Key<String>) {
        dataStore.edit { preferences ->
            preferences.remove(key)
        }
    }

    suspend fun read(key: Preferences.Key<String>): Flow<String> = dataStore.data
            .catch { exception ->
                if (exception is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }.map { preferences ->
                preferences[key]?: ""
            }

    suspend fun readUserModel(key: Preferences.Key<String>): Flow<Any> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }.map { preferences ->
           CommonUtils.convertJsonStringToModel<UserModel>(preferences[key] ?: "")
        }


}