package com.musyarrofah.storyapps.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.musyarrofah.storyapps.liststory.UserModel
import com.musyarrofah.storyapps.repository.StoryRepository
import okhttp3.MultipartBody
import okhttp3.RequestBody

class CreateStoryViewModel(private val storyRepository: StoryRepository) : ViewModel(){
    fun addStory(token: String, file: MultipartBody.Part, description: RequestBody, lat: Double?,
                 lon: Double?) =
        storyRepository.addStory(token, file, description, lat, lon)

    fun getUser(): LiveData<UserModel> {
        return storyRepository.getUserData()
    }
}