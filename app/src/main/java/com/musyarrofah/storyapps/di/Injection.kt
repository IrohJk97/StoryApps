package com.musyarrofah.storyapps.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.musyarrofah.storyapps.api.ApiConfig
import com.musyarrofah.storyapps.repository.StoryRepository
import com.musyarrofah.storyapps.viewmodel.PreferencesUser

val Context.dataStore: DataStore<Preferences> by preferencesDataStore("storyapp")
object Injection {
    fun provideRepository(context: Context): StoryRepository {
        val preferences = PreferencesUser.getInstance(context.dataStore)
        val apiService = ApiConfig.getApiService()
        return StoryRepository.getInstance(preferences, apiService)
    }

}