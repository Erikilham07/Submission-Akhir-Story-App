package com.dicoding.mystoryapp.ui.story

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.dicoding.mystoryapp.data.repository.StoryRepository
import com.dicoding.mystoryapp.data.response.UploadStoryResponse
import com.dicoding.mystoryapp.utils.DataDummy
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import com.dicoding.mystoryapp.data.Result
import com.dicoding.mystoryapp.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Rule
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class StoryViewModelTest{
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var storyRepository: StoryRepository
    private lateinit var storyViewModel: StoryViewModel
    private val dummyToken = "iyiobhvhxfcbvhtg"
    private val dummyUploadResponse = DataDummy.generateDummyUploadStoryResponse()
    private val dummyFile = DataDummy.generateDummyMultipartFile()
    private val dummyRequestBody = DataDummy.generateDummyRequestBody()

    @Before
    fun setUp() {
        storyViewModel = StoryViewModel(storyRepository)
    }

    @Test
    fun `Story success`() {
        val expectedUploadResponse = MutableLiveData<Result<UploadStoryResponse>>()
        expectedUploadResponse.value = Result.Success(dummyUploadResponse)

        `when`(storyViewModel.uploadStory(dummyToken,dummyFile, dummyRequestBody, null, null)).thenReturn(expectedUploadResponse)

        val actualUploadResponse = storyViewModel.uploadStory(dummyToken,dummyFile, dummyRequestBody, null, null).getOrAwaitValue()
        Mockito.verify(storyRepository).uploadStory(dummyToken, dummyFile, dummyRequestBody, null, null)
        Assert.assertNotNull(actualUploadResponse)
        Assert.assertTrue(actualUploadResponse is Result.Success)
    }

    @Test
    fun `Story failed`() {
        val expectedUploadResponse = MutableLiveData<Result<UploadStoryResponse>>()
        expectedUploadResponse.value = Result.Error("Error")

        `when`(storyViewModel.uploadStory(dummyToken,dummyFile, dummyRequestBody, null, null)).thenReturn(expectedUploadResponse)

        val actualUploadResponse = storyViewModel.uploadStory(dummyToken,dummyFile, dummyRequestBody, null, null).getOrAwaitValue()
        Mockito.verify(storyRepository).uploadStory(dummyToken, dummyFile, dummyRequestBody, null, null)
        Assert.assertNotNull(actualUploadResponse)
        Assert.assertTrue(actualUploadResponse is Result.Error)
    }
}