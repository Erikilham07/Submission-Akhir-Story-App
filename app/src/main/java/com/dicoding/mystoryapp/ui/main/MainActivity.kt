package com.dicoding.mystoryapp.ui.main

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.mystoryapp.R
import com.dicoding.mystoryapp.adapter.ListAdapterStory
import com.dicoding.mystoryapp.adapter.LoadingAdapter
import com.dicoding.mystoryapp.databinding.ActivityMainBinding
import com.dicoding.mystoryapp.ui.login.LoginActivity
import com.dicoding.mystoryapp.ui.story.StoryViewModelFactory
import com.dicoding.mystoryapp.ui.maps.MapsActivity
import com.dicoding.mystoryapp.ui.story.StoryActivity


class MainActivity : AppCompatActivity() {

private lateinit var binding: ActivityMainBinding
private lateinit var mainViewModel: MainViewModel
private lateinit var token: String

override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivityMainBinding.inflate(layoutInflater)
    setContentView(binding.root)

    if (applicationContext.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
        binding.rvStories.layoutManager = GridLayoutManager(this, 2)
    } else {
        binding.rvStories.layoutManager = LinearLayoutManager(this)
    }

    title = "Story"
    setupViewModel()
}

private fun setupViewModel() {
    val factory: StoryViewModelFactory = StoryViewModelFactory.getInstance(this)
    mainViewModel = ViewModelProvider(
        this,
        factory
    )[MainViewModel::class.java]

    mainViewModel.isLogin().observe(this){
        if (!it){
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }
    mainViewModel.getToken().observe(this){ token ->
        this.token = token
        if (token.isNotEmpty()){
            val adapter = ListAdapterStory()
            binding.rvStories.adapter = adapter.withLoadStateFooter(
                footer = LoadingAdapter {
                    adapter.retry()
                }
            )
            mainViewModel.getStories(token).observe(this){result ->
                adapter.submitData(lifecycle, result)
            }
        }
    }
}

override fun onCreateOptionsMenu(menu: Menu): Boolean {
    val inflater = menuInflater
    inflater.inflate(R.menu.item_menu, menu)

    return true
}

override fun onOptionsItemSelected(item: MenuItem): Boolean {
    return when (item.itemId) {
        R.id.logout -> {
            mainViewModel.logout()
            true
        }
        R.id.location -> {
            val intent = Intent(this, MapsActivity::class.java)
            intent.putExtra(MapsActivity.EXTRA_TOKEN, token)
            startActivity(intent)
            true
        }
        R.id.add_story -> {
            val intent = Intent(this, StoryActivity::class.java)
            intent.putExtra(StoryActivity.EXTRA_TOKEN, token)
            startActivity(intent)
            true
        }
        R.id.setting -> {
            startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
            true
        }
        else -> true
    }
}
}