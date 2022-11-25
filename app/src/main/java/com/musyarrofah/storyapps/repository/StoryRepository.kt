package com.musyarrofah.storyapps.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.liveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.musyarrofah.storyapps.addstory.AddStoryResponse
import com.musyarrofah.storyapps.api.ApiService
import com.musyarrofah.storyapps.liststory.StoryResponse
import com.musyarrofah.storyapps.liststory.UserModel
import com.musyarrofah.storyapps.login.LoginRequest
import com.musyarrofah.storyapps.login.LoginResponse
import com.musyarrofah.storyapps.paging.StoryPagingSource
import com.musyarrofah.storyapps.register.RegisterRequest
import com.musyarrofah.storyapps.register.RegisterResponse
import com.musyarrofah.storyapps.viewmodel.PreferencesViewModel
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.lang.Exception

class StoryRepository(private val pref: PreferencesViewModel, private val apiService: ApiService) {
    fun getStory(): LiveData<PagingData<StoryResponse.Story>> {
        return Pager(
            config = PagingConfig(pageSize = 5),
            pagingSourceFactory = {
                StoryPagingSource(apiService, pref)
            }
        ).liveData
    }

    fun userLogin(email: String, password: String): LiveData<ResultProcess<LoginResponse>> = liveData {
        emit(ResultProcess.Loading)
        try {
            val response = apiService.login(LoginRequest(email, password))
            emit(ResultProcess.Success(response))
        } catch (e: Exception) {
            Log.d("Login", e.message.toString())
            emit(ResultProcess.Error(e.message.toString()))
        }
    }

    fun userRegister(name: String, email: String, password: String)
            : LiveData<ResultProcess<RegisterResponse>> =
        liveData {
            emit(ResultProcess.Loading)
            try {
                val response = apiService.register(
                    RegisterRequest(name, email, password)
                )
                emit(ResultProcess.Success(response))
            } catch (e: Exception) {
                Log.d("Signup", e.message.toString())
                emit(ResultProcess.Error(e.message.toString()))
            }
        }

    fun addStory(
        token: String,
        file: MultipartBody.Part,
        description: RequestBody, lat: Double?, lon: Double?
    ): LiveData<ResultProcess<AddStoryResponse>> = liveData {
        emit(ResultProcess.Loading)
        try {
            val response = apiService.addStory(token, file, description,lat, lon)
            emit(ResultProcess.Success(response))
        } catch (e: Exception) {
            Log.d("Signup", e.message.toString())
            emit(ResultProcess.Error(e.message.toString()))
        }
    }

    fun getStoryLocation(token: String): LiveData<ResultProcess<StoryResponse>> = liveData {
        emit(ResultProcess.Loading)
        try {
            val response = apiService.getStoryLocation(token, 1)
            emit(ResultProcess.Success(response))
        } catch (e: Exception) {
            Log.d("Signup", e.message.toString())
            emit(ResultProcess.Error(e.message.toString()))
        }
    }

    fun getUserData(): LiveData<UserModel> {
        return pref.getUserData().asLiveData()
    }

    suspend fun saveUserData(user: UserModel) {
        pref.saveUserData(user)
    }

    suspend fun login() {
        pref.login()
    }

    suspend fun logout() {
        pref.logout()
    }

    companion object {
        @Volatile
        private var instance: StoryRepository? = null
        fun getInstance(
            preferences: PreferencesViewModel,
            apiService: ApiService
        ): StoryRepository =
            instance ?: synchronized(this) {
                instance ?: StoryRepository(preferences, apiService)
            }.also { instance = it }
    }
}
