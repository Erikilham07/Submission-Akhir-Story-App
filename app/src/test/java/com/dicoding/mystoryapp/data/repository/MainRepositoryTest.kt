package com.dicoding.mystoryapp.data.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.dicoding.mystoryapp.MainCoroutineRule
import com.dicoding.mystoryapp.data.FakeApiService
import com.dicoding.mystoryapp.data.retrofit.ApiService
import com.dicoding.mystoryapp.utils.DataDummy
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class MainRepositoryTest{
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @Mock
    private lateinit var apiService: ApiService
    @Mock
    private lateinit var dataStore : DataStore<Preferences>
    private lateinit var mainRepository: MainRepository

    private val dummyName = "Anonim"
    private val dummyEmail = "anonim@dicoding.com"
    private val dummyPassword = "hide"

    @Before
    fun setup() {
        apiService = FakeApiService()
        mainRepository = MainRepository.getInstance(dataStore, apiService)
    }

    @Test
    fun `Login response Should Not Null`() = mainCoroutineRule.runBlockingTest{
        val expectedResponse = DataDummy.generateDummyLoginResponse()
        val actualResponse = apiService.login(dummyEmail, dummyPassword)
        Assert.assertNotNull(actualResponse)
        Assert.assertEquals(actualResponse, expectedResponse)
    }

    @Test
    fun `Register response Should Not Null`() = mainCoroutineRule.runBlockingTest{
        val expectedResponse = DataDummy.generateDummySignupResponse()
        val actualResponse = apiService.register(dummyName, dummyEmail, dummyPassword)
        Assert.assertNotNull(actualResponse)
        Assert.assertEquals(actualResponse, expectedResponse)
    }
}