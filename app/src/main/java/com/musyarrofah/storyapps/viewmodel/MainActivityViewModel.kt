package com.musyarrofah.storyapps.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.musyarrofah.storyapps.liststory.StoryResponse
import com.musyarrofah.storyapps.liststory.UserModel
import com.musyarrofah.storyapps.repository.StoryRepository

class MainActivityViewModel (private val repository: StoryRepository) : ViewModel(){
    fun getStory(): LiveData<PagingData<StoryResponse.Story>> {
        return  repository.getStory().cachedIn(viewModelScope)
    }

//    fun getUser(): LiveData<UserModel> {
//        return repository.getUserData()
//    } belum diterapkan di unit test
}