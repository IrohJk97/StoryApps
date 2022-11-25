package com.musyarrofah.storyapps.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.musyarrofah.storyapps.api.ApiConfig
import com.musyarrofah.storyapps.login.LoginRequest
import com.musyarrofah.storyapps.login.LoginResponse
import com.musyarrofah.storyapps.register.RegisterRequest
import com.musyarrofah.storyapps.register.RegisterResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UserViewModel : ViewModel() {

    private val _registerResponse = MutableLiveData<RegisterResponse>()
    val registerResponse: LiveData<RegisterResponse> = _registerResponse

    private val _loginResponse = MutableLiveData<LoginResponse>()
    val loginResponse: LiveData<LoginResponse> = _loginResponse

    fun userRegister(model: RegisterRequest){
        viewModelScope.launch(Dispatchers.IO){
            try {
                val client = ApiConfig.apiInstance.userRegister(model)
                if (client.isSuccessful) {
                    _registerResponse.postValue(client.body())
                    Log.d("Success: ", client.body().toString())
                } else {
                    _registerResponse.postValue(RegisterResponse(error = true))
                }
            } catch (ex: Exception) {
                Log.d("ERROR", ex.toString())
            }
        }
    }

    fun userLogin(model: LoginRequest){
        viewModelScope.launch(Dispatchers.IO){
            try {
                val client = ApiConfig.apiInstance.userLogin(model)
                if (client.isSuccessful){
                    _loginResponse.postValue(client.body())
                    Log.d("Success: ", client.body().toString())
                }
            } catch (ex: Exception) {
                Log.d("ERROR", ex.toString())
            }
        }
    }

}