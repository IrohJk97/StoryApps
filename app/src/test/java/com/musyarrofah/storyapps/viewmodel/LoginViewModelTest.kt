package com.musyarrofah.storyapps.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.musyarrofah.storyapps.login.LoginResponse
import com.musyarrofah.storyapps.utils.Result
import com.musyarrofah.storyapps.repository.StoryRepository
import com.musyarrofah.storyapps.utils.AuthDummy
import com.musyarrofah.storyapps.utils.MainDispatcherRule
import com.musyarrofah.storyapps.utils.getOrAwaitValue
import junit.framework.TestCase.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class LoginViewModelTest {
    @get: Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var repository: StoryRepository
    private lateinit var loginViewModel: LoginViewModel
    private val authLogin = AuthDummy.provideLoginResponse()
    private val authSaveUser = AuthDummy.getUser()
    private val email = "msyrrfh14@gmail.com"
    private val password = "123456789"

    @OptIn(ExperimentalCoroutinesApi::class)
    @get:Rule
    val mainDispatcher = MainDispatcherRule()

    @Before
    fun setup() {
        loginViewModel = LoginViewModel(repository)
    }

    @Test
    fun `if login success then return Success`(){
        val expectedLiveData = MutableLiveData<Result<LoginResponse>>()
        expectedLiveData.value = Result.Success(authLogin)

        `when`(repository.userLogin(email, password)).thenReturn(expectedLiveData)
        val actual = loginViewModel.userLogin(email, password).getOrAwaitValue()

        verify(repository).userLogin(email, password)
        assertNotNull(actual)
        assertTrue(actual is Result.Success)
    }

    @Test
    fun `if login error then return Async Error`() {
        val expectedLiveData = MutableLiveData<Result<LoginResponse>>(Result.Error("Dummy"))
        `when`(repository.userLogin(email, password)).thenReturn(expectedLiveData)

        val actual = loginViewModel.userLogin(email, password).getOrAwaitValue()

        verify(repository).userLogin(email, password)
        assertTrue(actual is Result.Error)
        assertNotNull(actual)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun saveUser() = runTest {
        loginViewModel.saveUser(authSaveUser)
        verify(repository).saveUserData(authSaveUser)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun logout() = runTest {
        loginViewModel.logout()
        verify(repository).logout()
    }

}


