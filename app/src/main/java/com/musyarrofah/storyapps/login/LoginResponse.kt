package com.musyarrofah.storyapps.login

data class LoginResponse(
    val loginResult: LoginResult,
    val error: Boolean ,
    val message: String

)