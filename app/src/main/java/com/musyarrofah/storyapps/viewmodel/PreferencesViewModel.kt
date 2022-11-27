package com.musyarrofah.storyapps.viewmodel

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.musyarrofah.storyapps.liststory.UserModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PreferencesViewModel private constructor(private val dataStore: DataStore<Preferences>) {

    companion object {
        @Volatile
        private var INSTANCE: PreferencesViewModel? = null

        private val USER_ID = stringPreferencesKey("userid")
        private val NAME = stringPreferencesKey("name")
        private val TOKEN = stringPreferencesKey("token")
        private val STATE = booleanPreferencesKey("state")

        fun getInstance(dataStore: DataStore<Preferences>): PreferencesViewModel {
            return INSTANCE ?: synchronized(this) {
                val instance = PreferencesViewModel(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }

    fun getUserData(): Flow<UserModel> {
        return dataStore.data.map { preferences ->
            UserModel(
                preferences[USER_ID]?:"",
                preferences[NAME] ?: "",
                preferences[TOKEN] ?: "",
                preferences[STATE] ?: false
            )
        }
    }

    suspend fun saveUserData(user: UserModel) {
        dataStore.edit { preferences ->
            preferences[NAME] = user.name
            preferences[TOKEN] = user.token
            preferences[STATE] = user.isLogin
        }
    }

    suspend fun login() {
        dataStore.edit { preferences ->
            preferences[STATE] = true
        }
    }

    suspend fun logout() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }

    fun saveBearerToken(token: String) {
        TODO("Not yet implemented")
    }

    fun saveLoginState(b: Boolean) {

    }

    fun getBearerToken(): Any {
        TODO("Not yet implemented")
    }

    fun getLoginState(): Any {
        TODO("Not yet implemented")
    }

}