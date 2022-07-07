package com.dicoding.mystoryapp.ui.signup

import androidx.lifecycle.ViewModel
import com.dicoding.mystoryapp.data.repository.MainRepository

class SignupViewModel(private val repo: MainRepository) : ViewModel() {

    fun register(name: String, email: String, password: String) = repo.register(name, email, password)
}