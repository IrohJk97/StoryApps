package com.musyarrofah.storyapps.register

data class RegisterRequest(
    val name: String? = null,
    val email: String? = null,
    val password: String? = null
)