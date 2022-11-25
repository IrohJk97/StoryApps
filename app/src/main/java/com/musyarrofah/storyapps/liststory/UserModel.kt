package com.musyarrofah.storyapps.liststory

data class UserModel(
    val name: String,
    val token: String,
    val b: Boolean,
) {
    val isLogin: Boolean
        get() {
            TODO()
        }
}