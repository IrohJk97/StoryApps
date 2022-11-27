package com.musyarrofah.storyapps.viewmodel

import androidx.lifecycle.ViewModel
import com.musyarrofah.storyapps.repository.StoryRepository

class RegisterViewModel(private val storyRepository: StoryRepository): ViewModel() {
    fun userRegister(name: String, email: String, password: String) =
        storyRepository.userRegister(name, email, password)
}