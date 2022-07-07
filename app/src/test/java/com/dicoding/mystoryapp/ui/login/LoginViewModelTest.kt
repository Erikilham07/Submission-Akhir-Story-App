package com.dicoding.mystoryapp.ui.login

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.dicoding.mystoryapp.MainCoroutineRule
import com.dicoding.mystoryapp.data.repository.MainRepository
import com.dicoding.mystoryapp.data.response.LoginResponse
import com.dicoding.mystoryapp.utils.DataDummy
import com.dicoding.mystoryapp.data.Result
import com.dicoding.mystoryapp.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class LoginViewModelTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var userRepository: MainRepository
    private lateinit var loginViewModel: LoginViewModel
    private val dummyLoginResponse = DataDummy.generateDummyLoginResponse()
    private val dummyToken = "iyiobhvhxfcbvhtg"
    private val dummyEmail = "anonim@dicoding.com"
    private val dummyPassword = "hide"

    @Before
    fun setup() {
        loginViewModel = LoginViewModel(userRepository)
    }

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @Test
    fun `Login success and Result Success`() {
        val expectedLoginResponse = MutableLiveData<Result<LoginResponse>>()
        expectedLoginResponse.value = Result.Success(dummyLoginResponse)

        Mockito.`when`(loginViewModel.login(dummyEmail, dummyPassword)).thenReturn(expectedLoginResponse)

        val actualLoginResponse = loginViewModel.login(dummyEmail, dummyPassword).getOrAwaitValue()
        Mockito.verify(userRepository).login(dummyEmail, dummyPassword)
        Assert.assertNotNull(actualLoginResponse)
        Assert.assertTrue(actualLoginResponse is Result.Success)
        Assert.assertSame(dummyLoginResponse, (actualLoginResponse as Result.Success).data)
    }

    @Test
    fun `Signup failed and Result Error`() {
        val loginResponse = MutableLiveData<Result<LoginResponse>>()
        loginResponse.value = Result.Error("Error")

        Mockito.`when`(loginViewModel.login(dummyEmail, dummyPassword)).thenReturn(loginResponse)

        val actualLoginResponse = loginViewModel.login(dummyEmail, dummyPassword).getOrAwaitValue()
        Mockito.verify(userRepository).login(dummyEmail, dummyPassword)
        Assert.assertNotNull(actualLoginResponse)
        Assert.assertTrue(actualLoginResponse is Result.Error)
    }

    @Test
    fun `Save token successfully`() = mainCoroutineRule.runBlockingTest {
        loginViewModel.setToken(dummyToken, true)
        Mockito.verify(userRepository).setToken(dummyToken, true)
    }
}
