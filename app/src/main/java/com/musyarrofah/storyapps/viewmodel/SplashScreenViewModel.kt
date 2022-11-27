package com.musyarrofah.storyapps.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.musyarrofah.storyapps.liststory.UserModel

class SplashScreenViewModel(private val preferencesViewModel: PreferencesViewModel) {
    fun getUser(): LiveData<UserModel>{
        return preferencesViewModel.getUserData().asLiveData()
    }
}