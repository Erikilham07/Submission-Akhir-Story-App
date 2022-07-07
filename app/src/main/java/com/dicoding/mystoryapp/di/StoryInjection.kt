package com.dicoding.mystoryapp.di

import android.content.Context
import com.dicoding.mystoryapp.data.local.room.StoryDatabase
import com.dicoding.mystoryapp.data.repository.StoryRepository
import com.dicoding.mystoryapp.data.retrofit.ApiConfig

object StoryInjection {
    fun provideRepository(context: Context): StoryRepository {
        val apiService = ApiConfig.getApiService()
        val database = StoryDatabase.getDatabase(context)
        return StoryRepository(apiService, database)
    }
}