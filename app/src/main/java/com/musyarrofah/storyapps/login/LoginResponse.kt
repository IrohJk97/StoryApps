package com.musyarrofah.storyapps.login

data class LoginResponse(
    val message: String,
    val error: Boolean ,
    val loginResult: LoginResult,

)