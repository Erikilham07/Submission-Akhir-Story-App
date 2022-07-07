package com.dicoding.mystoryapp.ui.login

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.dicoding.mystoryapp.*
import com.dicoding.mystoryapp.databinding.ActivityLoginBinding
import com.dicoding.mystoryapp.ui.signup.MainViewModelFactory
import com.dicoding.mystoryapp.ui.signup.SignupActivity
import com.dicoding.mystoryapp.utils.animateVisibility
import com.dicoding.mystoryapp.data.Result
import com.dicoding.mystoryapp.ui.main.MainActivity


class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var loginViewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        setupAction()
        setupViewModel()
        playAnimation()
    }

    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.imgView, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val title = ObjectAnimator.ofFloat(binding.txLogin, View.ALPHA, 1f).setDuration(500)
        val tvEmail = ObjectAnimator.ofFloat(binding.txEmail, View.ALPHA, 1f).setDuration(500)
        val edtEmail = ObjectAnimator.ofFloat(binding.editEmail, View.ALPHA, 1f).setDuration(500)
        val tvPassword = ObjectAnimator.ofFloat(binding.txPassword, View.ALPHA, 1f).setDuration(500)
        val edtPassword = ObjectAnimator.ofFloat(binding.editPassword, View.ALPHA, 1f).setDuration(500)
        val btnLogin = ObjectAnimator.ofFloat(binding.btnLogin, View.ALPHA, 1f).setDuration(500)
        val signup = ObjectAnimator.ofFloat(binding.txNewsignup, View.ALPHA, 1f).setDuration(500)
        val message = ObjectAnimator.ofFloat(binding.txDes, View.ALPHA, 1f).setDuration(500)

        AnimatorSet().apply {
            playSequentially(title, tvEmail, edtEmail, tvPassword, edtPassword, btnLogin, message, signup)
            start()
        }
    }

    private fun setupAction() {
        binding.txNewsignup.setOnClickListener {
            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
        }

        binding.btnLogin.setOnClickListener {
            login()
        }
    }

    private fun login() {
        val email = binding.editEmail.text.toString().trim()
        val password = binding.editPassword.text.toString().trim()
        when {
            email.isEmpty() -> {
                binding.editEmail.error = resources.getString(R.string.message_validation, "email")
            }
            password.isEmpty() -> {
                binding.editPassword.error = resources.getString(R.string.message_validation, "password")
            }
            else -> {
                loginViewModel.login(email, password).observe(this){result ->
                    if (result != null){
                        when(result) {
                            is Result.Loading -> {
                                showLoading(true)
                            }
                            is Result.Success -> {
                                showLoading(false)
                                val user = result.data
                                if (user.error){
                                    Toast.makeText(this@LoginActivity, user.message, Toast.LENGTH_SHORT).show()
                                }else{
                                    val token = user.loginResult?.token ?: ""
                                    loginViewModel.setToken(token, true)
                                }
                            }
                            is Result.Error -> {
                                showLoading(false)
                                Toast.makeText(
                                    this,
                                    resources.getString(R.string.login_error),
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
                }
            }
        }
    }

    private fun setupViewModel() {
        val factory: MainViewModelFactory = MainViewModelFactory.getInstance(this)
        loginViewModel = ViewModelProvider(this, factory)[LoginViewModel::class.java]

        loginViewModel.getToken().observe(this){ token ->
            if (token.isNotEmpty()){
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.apply {
            editEmail.isEnabled = !isLoading
            editPassword.isEnabled = !isLoading
            btnLogin.isEnabled = !isLoading

            if (isLoading) {
                viewProgressbar.animateVisibility(true)
            } else {
                viewProgressbar.animateVisibility(false)
            }
        }
    }
}