package com.musyarrofah.storyapps.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.musyarrofah.storyapps.preferences.SettingPreference
import kotlinx.coroutines.launch

class PreferencesViewModel(private val pref: SettingPreference): ViewModel() {

    fun getLoginState(): LiveData<Boolean> {
        return pref.getLoginState().asLiveData()
    }

    fun saveLoginState(loginState: Boolean){
        viewModelScope.launch {
            pref.saveLoginState(loginState)
        }
    }

    fun  getBearerToken(): LiveData<String> {
        return pref.getBearerToken().asLiveData()
    }

    fun saveBearerToken(bearerKey: String) {
        viewModelScope.launch {
            pref.saveBearerToken(bearerKey)
        }
    }

}