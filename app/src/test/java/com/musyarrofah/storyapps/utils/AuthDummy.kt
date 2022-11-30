package com.musyarrofah.storyapps.utils

import com.musyarrofah.storyapps.login.LoginResponse
import com.musyarrofah.storyapps.login.LoginResult
import com.musyarrofah.storyapps.register.RegisterResponse

object AuthDummy {
    fun provideLoginResponse() : LoginResponse = LoginResponse(LoginResult("", "Jungkook", "ONLY"), false,"ok")

    fun provideRegisterResponse() : RegisterResponse = RegisterResponse (false, "Ok")
}