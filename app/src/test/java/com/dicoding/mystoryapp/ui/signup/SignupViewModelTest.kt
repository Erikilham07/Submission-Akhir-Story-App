package com.dicoding.mystoryapp.ui.signup

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.dicoding.mystoryapp.data.repository.MainRepository
import com.dicoding.mystoryapp.utils.DataDummy
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import com.dicoding.mystoryapp.data.Result
import com.dicoding.mystoryapp.data.response.RegisterResponse
import com.dicoding.mystoryapp.getOrAwaitValue
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class SignupViewModelTest{
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var mainRepository: MainRepository
    private lateinit var signupViewModel: SignupViewModel
    private val dummySignupResponse = DataDummy.generateDummySignupResponse()
    private val dummyName = "Anonim"
    private val dummyEmail = "anonim@dicoding.com"
    private val dummyPassword = "hide"

    @Before
    fun setup() {
        signupViewModel = SignupViewModel(mainRepository)
    }

    @Test
    fun `Signup and Result Success`() {
        val expectedSignupResponse = MutableLiveData<Result<RegisterResponse>>()
        expectedSignupResponse.value = Result.Success(dummySignupResponse)

        Mockito.`when`(signupViewModel.register(dummyName, dummyEmail, dummyPassword)).thenReturn(expectedSignupResponse)

        val actualSignupResponse = signupViewModel.register(dummyName, dummyEmail, dummyPassword).getOrAwaitValue()
        Mockito.verify(mainRepository).register(dummyName, dummyEmail, dummyPassword)
        Assert.assertNotNull(actualSignupResponse)
        Assert.assertTrue(actualSignupResponse is Result.Success)
        Assert.assertSame(dummySignupResponse, (actualSignupResponse as Result.Success).data)
    }

    @Test
    fun `Signup failed and Result Error`() {
        val signupResponse = MutableLiveData<Result<RegisterResponse>>()
        signupResponse.value = Result.Error("Error")

        Mockito.`when`(signupViewModel.register(dummyName, dummyEmail, dummyPassword)).thenReturn(signupResponse)

        val actualSignupResponse = signupViewModel.register(dummyName, dummyEmail, dummyPassword).getOrAwaitValue()
        Mockito.verify(mainRepository).register(dummyName, dummyEmail, dummyPassword)
        Assert.assertNotNull(actualSignupResponse)
        Assert.assertTrue(actualSignupResponse is Result.Error)
    }
}