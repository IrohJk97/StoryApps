package com.musyarrofah.storyapps.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.musyarrofah.storyapps.liststory.UserModel
import com.musyarrofah.storyapps.login.LoginResponse
import com.musyarrofah.storyapps.utils.Result
import com.musyarrofah.storyapps.repository.StoryRepository
import com.musyarrofah.storyapps.utils.AuthDummy
import com.musyarrofah.storyapps.utils.getOrAwaitValue
import junit.framework.TestCase.*
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class LoginViewModelTest {
    @get: Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var storyRepository: StoryRepository
    private lateinit var loginViewModel: LoginViewModel
    private val auth = AuthDummy.provideLoginResponse()
    private val email = "msyrrfh14@gmail.com"
    private val password = "123456789"

    @Before
    fun setup() {
        loginViewModel = LoginViewModel(storyRepository)
    }

    @Test
    fun `if login success then return Success`(){
        val expectedLiveData = MutableLiveData<Result<LoginResponse>>()
        expectedLiveData.value = Result.Success(auth)

        `when`(storyRepository.userLogin(email, password)).thenReturn(expectedLiveData)
        val actual = loginViewModel.userLogin(email, password).getOrAwaitValue()

        Mockito.verify(storyRepository).userLogin(email, password)
        assertNotNull(actual)
        assertTrue(actual is Result.Success)
    }

    @Test
    fun `if login error then return Async Error`() {
        val expectedLiveData = MutableLiveData<Result<LoginResponse>>(Result.Error("Dummy"))
        `when`(storyRepository.userLogin(email, password)).thenReturn(expectedLiveData)

        val actual = loginViewModel.userLogin(email, password).getOrAwaitValue()

        Mockito.verify(storyRepository).userLogin(email, password)
        assertTrue(actual is Result.Error)
        assertNotNull(actual)
    }

    @Test
    fun `when save user data is success`(){
        val expectedSaveUser = MutableLiveData<UserModel>()
        expectedSaveUser.value = AuthDummy.getUser()
        `when`(storyRepository.getUserData()).thenReturn(expectedSaveUser)
        val viewModel = CreateStoryViewModel(storyRepository)
        Assert.assertEquals(viewModel.getUser(), expectedSaveUser)
    }
}


