package com.dicoding.mystoryapp.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.dicoding.mystoryapp.data.repository.MainRepository
import com.dicoding.mystoryapp.data.retrofit.ApiConfig

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

object MainInjection {
    fun provideRepository(context: Context): MainRepository {
        val apiService = ApiConfig.getApiService()
        return MainRepository.getInstance(context.dataStore, apiService)
    }
}