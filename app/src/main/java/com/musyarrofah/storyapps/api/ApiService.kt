package com.musyarrofah.storyapps.api

import com.musyarrofah.storyapps.addstory.AddStoryResponse
import com.musyarrofah.storyapps.liststory.StoryResponse
import com.musyarrofah.storyapps.login.LoginRequest
import com.musyarrofah.storyapps.login.LoginResponse
import com.musyarrofah.storyapps.register.RegisterRequest
import com.musyarrofah.storyapps.register.RegisterResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    @POST("register")
    suspend fun userRegister(
        @Body body: RegisterRequest
    ): Response<RegisterResponse>

    @POST("login")
    suspend fun userLogin(
        @Body body: LoginRequest
    ): Response<LoginResponse>

    @GET("stories")
    suspend fun getAllStories(
        @Header("Authorization") bearer: String
    ): Response<StoryResponse>

    @Multipart
    @POST("stories")
    suspend fun postStories(
        @Header("Authorization") bearer: String,
        @Part file : MultipartBody.Part,
        @Part("description") description: RequestBody
    ): Response<AddStoryResponse>
}