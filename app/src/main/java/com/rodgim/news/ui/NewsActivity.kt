package com.rodgim.news.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.rodgim.news.R
import com.rodgim.news.databinding.ActivityNewsBinding

class NewsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ActivityNewsBinding.inflate(layoutInflater).apply {
            setContentView(root)
            val navHostFragment = supportFragmentManager.findFragmentById(R.id.newsNavHostFragment)
            navHostFragment?.let {
                bottomNavigationView.setupWithNavController(it.findNavController())
            }
        }
    }
}
