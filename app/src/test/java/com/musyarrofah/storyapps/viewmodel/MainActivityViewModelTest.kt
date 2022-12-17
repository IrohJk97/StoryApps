package com.musyarrofah.storyapps.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import androidx.recyclerview.widget.ListUpdateCallback
import com.musyarrofah.storyapps.liststory.StoryResponse
import com.musyarrofah.storyapps.liststory.UserModel
import com.musyarrofah.storyapps.paging.PagingSourceTest
import com.musyarrofah.storyapps.paging.StoryPagingAdapter
import com.musyarrofah.storyapps.repository.StoryRepository
import com.musyarrofah.storyapps.utils.AuthDummy
import com.musyarrofah.storyapps.utils.DispatcherRule
import com.musyarrofah.storyapps.utils.getOrAwaitValue
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class MainActivityViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @OptIn(ExperimentalCoroutinesApi::class)
    @get:Rule
    val mainDispatcherRules = DispatcherRule()

    @Mock
    private lateinit var storyRepository: StoryRepository

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `when get Story is success`() = runTest {
        val dummyStories = AuthDummy.provideDummyStoryResponse()
        val data: PagingData<StoryResponse.Story> = PagingSourceTest.snapshot(dummyStories)
        val expectedStory = MutableLiveData<PagingData<StoryResponse.Story>>()

        expectedStory.value = data
        Mockito.`when`(storyRepository.getStory()).thenReturn(expectedStory)

        val mainViewModel = MainActivityViewModel(storyRepository)
        val actualStory: PagingData<StoryResponse.Story> =
            mainViewModel.getStory().getOrAwaitValue()

        val differ = AsyncPagingDataDiffer(
            diffCallback = StoryPagingAdapter.DIFF_CALLBACK,
            updateCallback = noopListUpdateCallback,
            workerDispatcher = Dispatchers.Main,
        )
        differ.submitData(actualStory)

        Assert.assertNotNull(differ.snapshot())
        assertEquals(dummyStories, differ.snapshot())
        assertEquals(dummyStories.size, differ.snapshot().size)
        assertEquals(dummyStories[0].name, differ.snapshot()[0]?.name)
    }

    @Test
    fun getUser() {
        val expectedResponse = MutableLiveData<UserModel>()
        expectedResponse.value = AuthDummy.getUser()
        Mockito.`when`(storyRepository.getUserData()).thenReturn(expectedResponse)
        val viewModel = MainActivityViewModel(storyRepository)
        val actualResult = viewModel.getUser().getOrAwaitValue()
        Assert.assertEquals(actualResult, expectedResponse.value)
        Assert.assertEquals(actualResult.name, expectedResponse.value?.name)
        Assert.assertEquals(actualResult.token, expectedResponse.value?.token)
        Assert.assertEquals(actualResult.isLogin, expectedResponse.value?.isLogin)
    }

}

val noopListUpdateCallback = object : ListUpdateCallback {
    override fun onInserted(position: Int, count: Int) {}
    override fun onRemoved(position: Int, count: Int) {}
    override fun onMoved(fromPosition: Int, toPosition: Int) {}
    override fun onChanged(position: Int, count: Int, payload: Any?) {}
}
