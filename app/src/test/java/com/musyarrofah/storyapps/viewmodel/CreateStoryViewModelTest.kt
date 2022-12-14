package com.musyarrofah.storyapps.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.musyarrofah.storyapps.addstory.CreateStoryResponse
import com.musyarrofah.storyapps.liststory.UserModel
import com.musyarrofah.storyapps.repository.StoryRepository
import com.musyarrofah.storyapps.utils.AuthDummy
import com.musyarrofah.storyapps.utils.getOrAwaitValue
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner
import java.io.File
import com.musyarrofah.storyapps.utils.Result

@RunWith(MockitoJUnitRunner::class)
class CreateStoryViewModelTest {

    @get: Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var storyRepository: StoryRepository
    private lateinit var addStoryViewModel: CreateStoryViewModel
    private val auth = AuthDummy.provideDummyCreateStoryResponse()
    private val token = "TOKEN"
    private val lat = -6.1335033
    private val lon = 106.64356

    @Before
    fun setUp() {
        addStoryViewModel = CreateStoryViewModel(storyRepository)
    }

    @Suppress("UNUSED_EXPRESSION")
    @Test
    fun `when Add Story is Success`() {
        val description = "description".toRequestBody("text/plain".toMediaType())
        val file = Mockito.mock(File::class.java)
        val requestImageFile = file.asRequestBody("image/jpg".toMediaTypeOrNull())
        val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
            "photo",
            file.name,
            requestImageFile
        )

        val expectedStory = MutableLiveData<Result<CreateStoryResponse>>()
        expectedStory.value = Result.Success(auth)
        Mockito.`when`(storyRepository.addStory(token, imageMultipart, description, lat, lon)).thenReturn(
            expectedStory
        )
        (expectedStory)

        val actualStory = addStoryViewModel.addStory(token, imageMultipart, description, lat, lon)
            .getOrAwaitValue()

        Mockito.verify(storyRepository).addStory(token, imageMultipart, description, lat, lon)
        Assert.assertNotNull(actualStory)
        Assert.assertTrue(actualStory is Result.Success)
    }

    @Test
    fun `When get User is success` () {
        val expectedResponse = MutableLiveData<UserModel>()
        expectedResponse.value = AuthDummy.getUser()
        Mockito.`when`(storyRepository.getUserData()).thenReturn(expectedResponse)
        val viewModel = CreateStoryViewModel(storyRepository)
        val actualResult = viewModel.getUser().getOrAwaitValue()
        Assert.assertEquals(actualResult, expectedResponse.value)
        Assert.assertEquals(actualResult.name, expectedResponse.value?.name)
        Assert.assertEquals(actualResult.token, expectedResponse.value?.token)
        Assert.assertEquals(actualResult.isLogin, expectedResponse.value?.isLogin)

    }
}


