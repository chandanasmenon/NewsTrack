package com.chandana.newstrack.ui.homescreen

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.chandana.newstrack.databinding.ActivityMainBinding
import com.chandana.newstrack.ui.topheadlinesources.TopHeadlineSourcesActivity

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.topHeadlinesButton.setOnClickListener {
            val intent = Intent(this, TopHeadlineSourcesActivity::class.java)
            startActivity(intent)
        }
        binding.offlineTopHeadlinesButton.setOnClickListener {

        }
        binding.paginationTopHeadlinesButton.setOnClickListener {

        }
        binding.newsSourcesButton.setOnClickListener {

        }
        binding.countrySelectionButton.setOnClickListener {

        }
        binding.languageSelectionButton.setOnClickListener {

        }
        binding.searchButton.setOnClickListener {

        }
    }
}