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
                "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiJ1c2VyLVhOa2gyeWh1MUVUYThXdnQiLCJpYXQiOjE2Njk5NTE5OTN9.80o9iE2PH3Yup4dIDz2YAlOmzwcZ30Ib9kUNGBXYxFM"
            )
        )
    }

    fun provideRegisterResponse() : RegisterResponse = RegisterResponse (false, "Ok")

    fun provideDummyStoryResponse(): List<StoryResponse.Story> {
        val item = arrayListOf<StoryResponse.Story>()

        for (i in 0 until 10) {
            val story = StoryResponse.Story(
                "user-XNkh2yhu1ETa8Wvt",
                "siapa aja",
                "cek lokasi",
                "https://images.pexels.com/photos/3861972/pexels-photo-3861972.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1",
                "2022-11-02",
                -6.1335033,
                106.64356
            )
            item.add(story)
        }
        return item
    }
    fun provideDummyStoryLocation(): StoryResponse {
        val item: MutableList<StoryResponse.Story> = arrayListOf()
        for (i in 0..100) {
            val story = StoryResponse.Story(
                "user-XNkh2yhu1ETa8Wvt",
                "siapa aja",
                "cek lokasi",
                "https://images.pexels.com/photos/3861972/pexels-photo-3861972.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1",
                "2022-11-02",
                -6.1335033,
                106.64356
            )
            item.add(story)
        }
        return StoryResponse(
            false,
            "success",
            item
        )
    }

    fun getUser(): UserModel{
        return UserModel(
            "Bahrul Aqid Zizi",
            "As3jsdhfuhsos",
            true
        )
    }

    fun provideDummyCreateStoryResponse(): CreateStoryResponse {
        return CreateStoryResponse(
            false,
            "success",
        )
    }
}