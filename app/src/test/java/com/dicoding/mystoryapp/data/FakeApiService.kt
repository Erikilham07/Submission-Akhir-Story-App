package com.dicoding.mystoryapp.data

import com.dicoding.mystoryapp.data.response.LoginResponse
import com.dicoding.mystoryapp.data.response.RegisterResponse
import com.dicoding.mystoryapp.data.response.StoriesResponse
import com.dicoding.mystoryapp.data.response.UploadStoryResponse
import com.dicoding.mystoryapp.data.retrofit.ApiService
import com.dicoding.mystoryapp.utils.DataDummy
import okhttp3.MultipartBody
import okhttp3.RequestBody

class FakeApiService : ApiService {
    private val dummyLoginResponse = DataDummy.generateDummyLoginResponse()
    private val dummySignupResponse = DataDummy.generateDummySignupResponse()
    private val dummyStories = DataDummy.generateDummyStoriesResponse()
    private val dummyUploadStory = DataDummy.generateDummyUploadStoryResponse()

    override suspend fun register(name: String, email: String, password: String): RegisterResponse {
        return dummySignupResponse
    }

    override suspend fun login(email: String, password: String): LoginResponse {
        return dummyLoginResponse
    }

    override suspend fun getStories(
        token: String,
        page: Int?,
        size: Int?,
        location: Int
    ): StoriesResponse {
        return dummyStories
    }

    override suspend fun uploadStory(
        token: String,
        file: MultipartBody.Part,
        description: RequestBody,
        lat: RequestBody?,
        lon: RequestBody?
    ): UploadStoryResponse {
        return dummyUploadStory
    }
}