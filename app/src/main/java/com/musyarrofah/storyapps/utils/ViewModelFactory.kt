package com.musyarrofah.storyapps.utils

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.musyarrofah.storyapps.adapter.AddStoryActivity
import com.musyarrofah.storyapps.adapter.ListStoryAdapter
import com.musyarrofah.storyapps.adapter.LoginActivity
import com.musyarrofah.storyapps.adapter.RegisterActivity
import com.musyarrofah.storyapps.di.Injection
import com.musyarrofah.storyapps.maps.MapsViewModel
import com.musyarrofah.storyapps.repository.StoryRepository
import java.lang.IllegalArgumentException

class ViewModelFactory private constructor(private val repository: StoryRepository) :
    ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ListStoryAdapter::class.java)) {
            return ListStoryAdapter(repository) as T
        }
        if (modelClass.isAssignableFrom(LoginActivity::class.java)) {
            return LoginActivity(repository) as T
        }
        if (modelClass.isAssignableFrom(RegisterActivity::class.java)) {
            return RegisterActivity(repository) as T
        }
        if (modelClass.isAssignableFrom(AddStoryActivity::class.java)) {
            return AddStoryActivity(repository) as T
        }
        if (modelClass.isAssignableFrom(MapsViewModel::class.java)) {
            return MapsViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }

    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null
        fun getInstance(context: Context): ViewModelFactory {
            return instance ?: synchronized(this) {
                instance ?: ViewModelFactory(Injection.provideRepository(context))
            }.also { instance = it }
        }
    }
}