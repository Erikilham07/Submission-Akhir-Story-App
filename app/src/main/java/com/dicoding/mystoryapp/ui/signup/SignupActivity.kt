package com.dicoding.mystoryapp.ui.signup

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import com.dicoding.mystoryapp.*
import com.dicoding.mystoryapp.databinding.ActivitySignupBinding
import com.dicoding.mystoryapp.ui.login.LoginActivity
import com.dicoding.mystoryapp.utils.animateVisibility
import com.dicoding.mystoryapp.data.Result

class SignupActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignupBinding
    private lateinit var signupViewModel: SignupViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
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

        val title = ObjectAnimator.ofFloat(binding.txSignup, View.ALPHA, 1f).setDuration(500)
        val tvName = ObjectAnimator.ofFloat(binding.txName, View.ALPHA, 1f).setDuration(500)
        val edtName = ObjectAnimator.ofFloat(binding.editName, View.ALPHA, 1f).setDuration(500)
        val tvEmail = ObjectAnimator.ofFloat(binding.txEmail, View.ALPHA, 1f).setDuration(500)
        val edtEmail = ObjectAnimator.ofFloat(binding.editEmail, View.ALPHA, 1f).setDuration(500)
        val tvPassword = ObjectAnimator.ofFloat(binding.txPassword, View.ALPHA, 1f).setDuration(500)
        val edtPassword = ObjectAnimator.ofFloat(binding.editPassword, View.ALPHA, 1f).setDuration(500)
        val btnSignup = ObjectAnimator.ofFloat(binding.btnSignup, View.ALPHA, 1f).setDuration(500)
        val message = ObjectAnimator.ofFloat(binding.tvKet, View.ALPHA, 1f).setDuration(500)
        val login = ObjectAnimator.ofFloat(binding.tvLogin, View.ALPHA, 1f).setDuration(500)

        AnimatorSet().apply {
            playSequentially(title,tvName,edtName, tvEmail, edtEmail, tvPassword, edtPassword, btnSignup, message, login)
            start()
        }
    }

    private fun setupViewModel() {
        val factory: MainViewModelFactory = MainViewModelFactory.getInstance(this)
        signupViewModel = ViewModelProvider(this, factory)[SignupViewModel::class.java]
    }

    private fun setupAction() {
        binding.btnSignup.setOnClickListener {
            val name = binding.editName.text.toString().trim()
            val email = binding.editEmail.text.toString().trim()
            val password = binding.editPassword.text.toString().trim()
            when {
                name.isEmpty() -> {
                    binding.editName.error = resources.getString(R.string.message_validation, "name")
                }
                email.isEmpty() -> {
                    binding.editEmail.error = resources.getString(R.string.message_validation, "email")
                }
                password.isEmpty() -> {
                    binding.editPassword.error = resources.getString(R.string.message_validation, "password")
                }
                else -> {
                    signupViewModel.register(name, email, password).observe(this){ result ->
                        if (result != null){
                            when(result) {
                                is Result.Loading -> {
                                    showLoading(true)
                                }
                                is Result.Success -> {
                                    showLoading(false)
                                    val user = result.data
                                    if (user.error){
                                        Toast.makeText(this@SignupActivity, user.message, Toast.LENGTH_SHORT).show()
                                    }else{
                                        AlertDialog.Builder(this@SignupActivity).apply {
                                            setTitle("Yeah!")
                                            setMessage("Your account successfully created!")
                                            setPositiveButton("Next") { _, _ ->
                                                finish()
                                            }
                                            create()
                                            show()
                                        }
                                    }
                                }
                                is Result.Error -> {
                                    showLoading(false)
                                    Toast.makeText(
                                        this,
                                        resources.getString(R.string.signup_error),
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        }
                    }
                }
            }
        }

        binding.tvLogin.setOnClickListener{
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.apply {
            editEmail.isEnabled = !isLoading
            editPassword.isEnabled = !isLoading
            editName.isEnabled = !isLoading
            btnSignup.isEnabled = !isLoading

            if (isLoading) {
                viewProgressbar.animateVisibility(true)
            } else {
                viewProgressbar.animateVisibility(false)
            }
        }
    }
}