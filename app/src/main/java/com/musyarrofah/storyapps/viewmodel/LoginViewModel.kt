package com.musyarrofah.storyapps.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.musyarrofah.storyapps.liststory.UserModel
import com.musyarrofah.storyapps.repository.StoryRepository
import kotlinx.coroutines.launch

class LoginViewModel(private val storyRepository: StoryRepository) : ViewModel() {
    fun userLogin(email: String, password: String) = storyRepository.userLogin(email, password)

    fun saveUser(user: UserModel) {
        viewModelScope.launch {
            storyRepository.saveUserData(user)
        }
    }

    fun login() {
        viewModelScope.launch {
            storyRepository.login()
        }
    }

    fun logout() {
        viewModelScope.launch {
            storyRepository.logout()
        }
    }
}