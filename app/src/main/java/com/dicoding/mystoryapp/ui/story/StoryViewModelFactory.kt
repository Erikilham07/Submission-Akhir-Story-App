package com.dicoding.mystoryapp.ui.story

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dicoding.mystoryapp.data.repository.MainRepository
import com.dicoding.mystoryapp.data.repository.StoryRepository
import com.dicoding.mystoryapp.di.MainInjection
import com.dicoding.mystoryapp.di.StoryInjection
import com.dicoding.mystoryapp.ui.main.MainViewModel
import com.dicoding.mystoryapp.ui.maps.MapsViewModel

class StoryViewModelFactory private constructor(private val userRepo: MainRepository, private val storyRepo: StoryRepository) :
    ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                MainViewModel(userRepo, storyRepo) as T
            }
            modelClass.isAssignableFrom(StoryViewModel::class.java) -> {
                StoryViewModel(storyRepo) as T
            }
            modelClass.isAssignableFrom(MapsViewModel::class.java) -> {
                MapsViewModel(storyRepo) as T
            }
            else -> {
                throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
            }
        }
    }

    companion object {
        @Volatile
        private var instance: StoryViewModelFactory? = null
        fun getInstance(context: Context): StoryViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: StoryViewModelFactory(MainInjection.provideRepository(context), StoryInjection.provideRepository(context))
            }.also { instance = it }
    }
}