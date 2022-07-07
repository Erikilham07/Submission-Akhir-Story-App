package com.dicoding.mystoryapp.ui.maps

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.dicoding.mystoryapp.data.repository.StoryRepository
import com.dicoding.mystoryapp.data.response.StoriesResponse
import com.dicoding.mystoryapp.utils.DataDummy
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import com.dicoding.mystoryapp.data.Result
import com.dicoding.mystoryapp.getOrAwaitValue
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class MapsViewModelTest {
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var storyRepository: StoryRepository
    private lateinit var mapsViewModel: MapsViewModel
    private val dummyToken = "iyiobhvhxfcbvhtg"
    private val dummyStories = DataDummy.generateDummyStoriesResponse()

    @Before
    fun setUp() {
        mapsViewModel = MapsViewModel(storyRepository)
    }
    @Test
    fun `when get Maps story Should Not Null and Return Success`() {
        val expectedStories = MutableLiveData<Result<StoriesResponse>>()
        expectedStories.value = Result.Success(dummyStories)
        Mockito.`when`(mapsViewModel.getAllStoryWithMaps(dummyToken)).thenReturn(expectedStories)

        val actualStories = mapsViewModel.getAllStoryWithMaps(dummyToken).getOrAwaitValue()

        Mockito.verify(storyRepository).getMaps(dummyToken)
        Assert.assertNotNull(actualStories)
        Assert.assertTrue(actualStories is Result.Success)
        Assert.assertSame(dummyStories, (actualStories as Result.Success).data)
        Assert.assertEquals(dummyStories.listStory.size, actualStories.data.listStory.size)
    }

    @Test
    fun `when Network error Should Return Error`() {
        val expectedStories = MutableLiveData<Result<StoriesResponse>>()
        expectedStories.value = Result.Error("Error")
        Mockito.`when`(mapsViewModel.getAllStoryWithMaps(dummyToken)).thenReturn(expectedStories)

        val actualStories = mapsViewModel.getAllStoryWithMaps(dummyToken).getOrAwaitValue()

        Mockito.verify(storyRepository).getMaps(dummyToken)
        Assert.assertNotNull(actualStories)
        Assert.assertTrue(actualStories is Result.Error)
    }
}