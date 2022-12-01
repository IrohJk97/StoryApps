package com.musyarrofah.storyapps.api

import com.musyarrofah.storyapps.addstory.CreateStoryResponse
import com.musyarrofah.storyapps.liststory.StoryResponse
import com.musyarrofah.storyapps.login.LoginRequest
import com.musyarrofah.storyapps.login.LoginResponse
import com.musyarrofah.storyapps.register.RegisterRequest
import com.musyarrofah.storyapps.register.RegisterResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface ApiService {
    @POST("register")
    suspend fun register(
        @Body request: RegisterRequest
    ): RegisterResponse

    @POST("login")
    suspend fun login(
        @Body request: LoginRequest
    ): LoginResponse

    @GET("stories")
    suspend fun getStory(
        @Header("Authorization") token: String,
        @Query("page") page: Int,
        @Query("size") size: Int,
    ): StoryResponse

    @GET("stories")
    suspend fun getStoryLocation(
        @Header("Authorization") token: String,
        @Query("location") location : Int = 1,
    ) : StoryResponse

    @Multipart
    @POST("stories")
    suspend fun addStory(
        @Header("Authorization") token: String,
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody,
        @Part("lat") lat: Double?,
        @Part("lon") lon: Double?
    ): CreateStoryResponse

}