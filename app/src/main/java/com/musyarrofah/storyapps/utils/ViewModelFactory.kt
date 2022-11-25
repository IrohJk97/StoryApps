package com.musyarrofah.storyapps.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.musyarrofah.storyapps.preferences.SettingPreference
import com.musyarrofah.storyapps.viewmodel.PreferencesViewModel
import java.lang.IllegalArgumentException

class ViewModelFactory (private val pref: SettingPreference) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PreferencesViewModel::class.java)) {
            return PreferencesViewModel(pref) as T
        }
        throw IllegalArgumentException("Unkow ViewModel Class "+ modelClass.name)
    }

}