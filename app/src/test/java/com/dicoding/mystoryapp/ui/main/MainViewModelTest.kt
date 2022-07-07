package com.dicoding.mystoryapp.ui.main

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import androidx.recyclerview.widget.ListUpdateCallback
import com.dicoding.mystoryapp.MainCoroutineRule
import com.dicoding.mystoryapp.PagingDataTest
import com.dicoding.mystoryapp.adapter.ListAdapterStory
import com.dicoding.mystoryapp.data.local.entity.Story
import com.dicoding.mystoryapp.getOrAwaitValue
import com.dicoding.mystoryapp.utils.DataDummy
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class MainViewModelTest {
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @Mock
    private lateinit var mainViewModel: MainViewModel
    private val dummyToken = "jksfalkjflkjfaieoivmm"

    @Test
    fun `Logout successfully`() = mainCoroutineRule.runBlockingTest {
        mainViewModel.logout()
        Mockito.verify(mainViewModel).logout()
    }

    @Test
    fun `get token successfully`() {
        val expectedToken = MutableLiveData<String>()
        expectedToken.value = dummyToken
        Mockito.`when`(mainViewModel.getToken()).thenReturn(expectedToken)

        val actualToken = mainViewModel.getToken().getOrAwaitValue()
        Mockito.verify(mainViewModel).getToken()
        Assert.assertNotNull(actualToken)
        Assert.assertEquals(dummyToken, actualToken)
    }

    @Test
    fun `get session login successfully`() {
        val dummySession = true
        val expectedSession = MutableLiveData<Boolean>()
        expectedSession.value = dummySession
        Mockito.`when`(mainViewModel.isLogin()).thenReturn(expectedSession)

        val actualSession = mainViewModel.isLogin().getOrAwaitValue()
        Mockito.verify(mainViewModel).isLogin()
        Assert.assertNotNull(actualSession)
        Assert.assertEquals(dummySession, actualSession)
    }

    @Test
    fun `when get list story should not null`() = mainCoroutineRule.runBlockingTest {
        val dummyStories = DataDummy.generateDummyStoriesList()
        val expectedStories = MutableLiveData<PagingData<Story>>()
        expectedStories.value = PagingDataTest.snapshot(dummyStories)
        Mockito.`when`(mainViewModel.getStories(dummyToken)).thenReturn(expectedStories)
        val actualStories = mainViewModel.getStories(dummyToken).getOrAwaitValue()

        val noopListUpdateCallback = object : ListUpdateCallback {
            override fun onInserted(position: Int, count: Int) {}
            override fun onRemoved(position: Int, count: Int) {}
            override fun onMoved(fromPosition: Int, toPosition: Int) {}
            override fun onChanged(position: Int, count: Int, payload: Any?) {}
        }
        val storyDiffer = AsyncPagingDataDiffer(
            diffCallback = ListAdapterStory.DIFF_CALLBACK,
            updateCallback = noopListUpdateCallback,
            mainDispatcher = mainCoroutineRule.dispatcher,
            workerDispatcher = mainCoroutineRule.dispatcher,
        )
        storyDiffer.submitData(actualStories)

        advanceUntilIdle()

        Mockito.verify(mainViewModel).getStories(dummyToken)
        Assert.assertNotNull(actualStories)
        Assert.assertEquals(dummyStories.size, storyDiffer.snapshot().size)
    }
}