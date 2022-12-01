package com.musyarrofah.storyapps.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.musyarrofah.storyapps.register.RegisterResponse
import com.musyarrofah.storyapps.repository.StoryRepository
import com.musyarrofah.storyapps.utils.AuthDummy
import com.musyarrofah.storyapps.utils.Result
import com.musyarrofah.storyapps.utils.getOrAwaitValue
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class RegisterViewModelTest {
    @get: Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var repository: StoryRepository
    private lateinit var registerViewModel: RegisterViewModel
    private val auth = AuthDummy.provideRegisterResponse()
    private val name = "Musyarrofah"
    private val email = "msyrrfh14@gmail.com"
    private val password = "123456789"

    @Before
    fun setup() {
        registerViewModel = RegisterViewModel(repository)
    }

    @Test
    fun `when register called from repository it should return Success and not null`() {
        val expected = MutableLiveData<Result<RegisterResponse>>()
        expected.value = Result.Success(auth)
        `when`(repository.userRegister(name, email, password)).thenReturn(expected)

        val actual = registerViewModel.userRegister(name, email, password).getOrAwaitValue()
        Assert.assertTrue(actual is Result.Success)
        Assert.assertNotNull(actual)
    }

    @Test
    fun `when register failed it should return Error and also not null`() {
        val expected = MutableLiveData<Result<RegisterResponse>>()
        expected.value = Result.Error("Something Error")
        `when`(repository.userRegister(name, email, password)).thenReturn(expected)

        val actual = registerViewModel.userRegister(name, email, password).getOrAwaitValue()
        Assert.assertTrue(actual is Result.Error)
        Assert.assertNotNull(actual)
    }
}