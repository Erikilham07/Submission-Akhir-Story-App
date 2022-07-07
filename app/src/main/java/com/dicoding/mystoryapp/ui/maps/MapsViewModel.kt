package com.dicoding.mystoryapp.ui.maps

import androidx.lifecycle.ViewModel
import com.dicoding.mystoryapp.data.repository.StoryRepository

class MapsViewModel(private val storyRepo: StoryRepository) : ViewModel() {
    fun getAllStoryWithMaps(token: String) = storyRepo.getMaps(token)
}
