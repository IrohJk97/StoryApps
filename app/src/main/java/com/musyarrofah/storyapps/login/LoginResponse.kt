package com.musyarrofah.storyapps.login

data class LoginResponse(
    val error: Boolean? = null,
    val message: String? = "",
    val loginResult: LoginResult? = null
)