package com.dicoding.mystoryapp.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.paging.*
import com.dicoding.mystoryapp.data.response.StoriesResponse
import com.dicoding.mystoryapp.data.retrofit.ApiService
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.lang.Exception
import com.dicoding.mystoryapp.data.Result
import com.dicoding.mystoryapp.data.StoryRemoteMediator
import com.dicoding.mystoryapp.data.local.entity.Story
import com.dicoding.mystoryapp.data.local.room.StoryDatabase
import com.dicoding.mystoryapp.data.response.UploadStoryResponse
import com.dicoding.mystoryapp.utils.wrapEspressoIdlingResource

class StoryRepository(private val apiService: ApiService, private val storyDatabase: StoryDatabase){

    fun getStories(token: String): LiveData<PagingData<Story>> {
        wrapEspressoIdlingResource {
            @OptIn(ExperimentalPagingApi::class)
            return Pager(
                config = PagingConfig(
                    pageSize = 5
                ),
                remoteMediator = StoryRemoteMediator(storyDatabase, apiService, token),
                pagingSourceFactory = {
                    storyDatabase.storyDao().getAllStories()
                }
            ).liveData
        }
    }

    fun getMaps(token: String): LiveData<Result<StoriesResponse>> = liveData{
        emit(Result.Loading)
        try {
            val client = apiService.getStories("Bearer $token", location = 1)
            emit(Result.Success(client))
        }catch (e : Exception){
            emit(Result.Error(e.message.toString()))
        }
    }

    fun uploadStory(token: String, imageMultipart: MultipartBody.Part, desc: RequestBody, lat: RequestBody?, lon: RequestBody?): LiveData<Result<UploadStoryResponse>> = liveData{
        emit(Result.Loading)
        try {
            val client = apiService.uploadStory("Bearer $token",imageMultipart, desc, lat, lon)
            emit(Result.Success(client))
        }catch (e : Exception){
            emit(Result.Error(e.message.toString()))
        }
    }
}