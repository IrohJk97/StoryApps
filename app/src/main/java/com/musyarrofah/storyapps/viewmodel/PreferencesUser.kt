package com.musyarrofah.storyapps.viewmodel

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.musyarrofah.storyapps.liststory.UserModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PreferencesUser private constructor(private val dataStore: DataStore<Preferences>) {

    companion object {
        @Volatile
        private var INSTANCE: PreferencesUser? = null

        private val NAME = stringPreferencesKey("name")
        private val TOKEN = stringPreferencesKey("token")
        private val STATE = booleanPreferencesKey("state")

        fun getInstance(dataStore: DataStore<Preferences>): com.musyarrofah.storyapps.viewmodel.PreferencesUser {
            return INSTANCE ?: synchronized(this) {
                val instance = PreferencesUser(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }

    fun getUserData(): Flow<UserModel> {
        return dataStore.data.map { preferences ->
            UserModel(
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

}