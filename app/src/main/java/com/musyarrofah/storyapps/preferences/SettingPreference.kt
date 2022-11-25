package com.musyarrofah.storyapps.preferences

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SettingPreference private constructor(private val dataStore: DataStore<Preferences>){

    private val LOGIN_STATE = booleanPreferencesKey("login")
    private val BEARER_KEY = stringPreferencesKey("bearer")

    fun getLoginState(): Flow<Boolean> {
        return dataStore.data.map { preferences ->
            preferences[LOGIN_STATE] ?: false
        }
    }

    fun getBearerToken(): Flow<String> {
        return dataStore.data.map { preferences ->
            preferences[BEARER_KEY] ?: ""
        }
    }

    suspend fun saveLoginState(loginState: Boolean){
        dataStore.edit { preferences ->
            preferences[LOGIN_STATE] = loginState
        }
    }

    suspend fun saveBearerToken(bearerKey: String){
        dataStore.edit { preferences ->
            preferences[BEARER_KEY] = bearerKey
        }
    }


    companion object {
        @Volatile
        private var INSTANCE : SettingPreference? = null

        fun getInstance(dataStore: DataStore<Preferences>) : SettingPreference {
            return INSTANCE ?: synchronized(this) {
                val instance = SettingPreference(dataStore)
                INSTANCE = instance
                instance
            }
        }

    }


}