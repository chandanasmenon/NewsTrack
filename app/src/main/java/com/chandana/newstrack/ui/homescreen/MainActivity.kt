package com.chandana.newstrack.ui.homescreen

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.chandana.newstrack.databinding.ActivityMainBinding
import com.chandana.newstrack.ui.categorynews.CategoryDisplayActivity
import com.chandana.newstrack.ui.countrynews.CountryDisplayActivity
import com.chandana.newstrack.ui.offlinetopheadlines.OfflineTopHeadlinesActivity
import com.chandana.newstrack.ui.pagingtopheadlinesources.TopHeadlinesPagingActivity
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
        binding.paginationTopHeadlinesButton.setOnClickListener {
            val intent = Intent(this, TopHeadlinesPagingActivity::class.java)
            startActivity(intent)
        }
        binding.offlineTopHeadlinesButton.setOnClickListener {
            val intent = Intent(this, OfflineTopHeadlinesActivity::class.java)
            startActivity(intent)
        }
        binding.newsSourcesButton.setOnClickListener {
            val intent = Intent(this, CategoryDisplayActivity::class.java)
            startActivity(intent)
        }
        binding.countrySelectionButton.setOnClickListener {
            val intent = Intent(this, CountryDisplayActivity::class.java)
            startActivity(intent)
        }
        binding.languageSelectionButton.setOnClickListener {

        }
        binding.searchButton.setOnClickListener {

        }
    }
}