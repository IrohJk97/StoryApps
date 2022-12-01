package com.musyarrofah.storyapps.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.musyarrofah.storyapps.liststory.UserModel
import com.musyarrofah.storyapps.repository.StoryRepository

class MapsViewModel(private val repository: StoryRepository) : ViewModel() {
    fun getStoryLocation(token: String) =
        repository.getStoryLocation(token)

    fun getUser(): LiveData<UserModel> {
        return repository.getUserData()
    }
}