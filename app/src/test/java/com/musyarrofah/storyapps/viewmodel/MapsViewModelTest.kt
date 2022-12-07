package com.musyarrofah.storyapps.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.musyarrofah.storyapps.liststory.StoryResponse
import com.musyarrofah.storyapps.liststory.UserModel
import com.musyarrofah.storyapps.repository.StoryRepository
import com.musyarrofah.storyapps.utils.AuthDummy
import com.musyarrofah.storyapps.utils.getOrAwaitValue
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner
import com.musyarrofah.storyapps.utils.Result

@RunWith(MockitoJUnitRunner::class)
class MapsViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var storyRepository: StoryRepository
    private lateinit var mapsViewModel: MapsViewModel
    private val auth = AuthDummy.provideDummyStoryLocation()
    private val token = "TOKEN"

    @Before
    fun setUp() {
        mapsViewModel = MapsViewModel(storyRepository)
    }

    @Test
    fun `when get Story Location is success`() {
        val expectedStory = MutableLiveData<Result<StoryResponse>>()
        expectedStory.value = Result.Success(auth)

        Mockito.`when`(storyRepository.getStoryLocation(token)).thenReturn(expectedStory)

        val actualStory = mapsViewModel.getStoryLocation(token).getOrAwaitValue()
        Mockito.verify(storyRepository).getStoryLocation(token)
        Assert.assertNotNull(actualStory)
        Assert.assertTrue(actualStory is Result.Success)
    }

    @Test
    fun `when get Story Location is Error`() {
        val expectedResponse = MutableLiveData<Result<StoryResponse>>()
        expectedResponse.value = Result.Error("error")

        Mockito.`when`(storyRepository.getStoryLocation(token)).thenReturn(expectedResponse)

        val actualResponse = mapsViewModel.getStoryLocation(token).getOrAwaitValue()

        Mockito.verify(storyRepository).getStoryLocation(token)
        Assert.assertNotNull(actualResponse)
        Assert.assertTrue(actualResponse is Result.Error)
    }

    @Test
    fun `get User` () {
        val expectedResponse = MutableLiveData<UserModel>()
        expectedResponse.value = UserModel("name", "token", true)
        Mockito.`when`(storyRepository.getUserData()).thenReturn(expectedResponse)

        // Create a MapsViewModel with the mock repository
        val viewModel = MapsViewModel(storyRepository)

        // Call getUser and verify that the correct LiveData is returned
        Assert.assertEquals(viewModel.getUser(), expectedResponse)
    }

}
