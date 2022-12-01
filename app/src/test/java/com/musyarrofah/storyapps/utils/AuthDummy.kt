package com.musyarrofah.storyapps.utils

import com.musyarrofah.storyapps.addstory.CreateStoryResponse
import com.musyarrofah.storyapps.liststory.StoryResponse
import com.musyarrofah.storyapps.liststory.UserModel
import com.musyarrofah.storyapps.login.LoginResponse
import com.musyarrofah.storyapps.login.LoginResult
import com.musyarrofah.storyapps.register.RegisterResponse

object AuthDummy {
    fun provideLoginResponse() : LoginResponse {
        return LoginResponse(
            "success",
            false,
            LoginResult(
                "user-XNkh2yhu1ETa8Wvt",
                "siapa aja",
                "token"
            )
        )
    }

    fun provideRegisterResponse() : RegisterResponse = RegisterResponse (false, "Ok")

    fun provideDummyStoryResponse(): List<StoryResponse.Story> {
        val item = arrayListOf<StoryResponse.Story>()

        for (i in 0 until 10) {
            val story = StoryResponse.Story(
                "",
                "",
                "",
                "",
                ""
            )
            item.add(story)
        }
        return item
    }

    fun provideGetUser(): UserModel {
        return UserModel(
            "",
            "token",
            true
        )
    }

    fun provideDummyStoryLocation(): StoryResponse {
        val item: MutableList<StoryResponse.Story> = arrayListOf()
        for (i in 0..100) {
            val story = StoryResponse.Story(
                "",
                "",
                "",
                "",
                ""
            )
            item.add(story)
        }
        return StoryResponse(
            false,
            "success",
            item
        )
    }

    fun provideDummyCreateStoryResponse(): CreateStoryResponse {
        return CreateStoryResponse(
            false,
            "success",
        )
    }
}