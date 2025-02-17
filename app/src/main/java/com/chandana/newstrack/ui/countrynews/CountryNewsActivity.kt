package com.chandana.newstrack.ui.countrynews

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.chandana.newstrack.NewsApplication
import com.chandana.newstrack.R
import com.chandana.newstrack.data.model.ApiSource
import com.chandana.newstrack.data.model.Code
import com.chandana.newstrack.databinding.ActivityCountryNewsBinding
import com.chandana.newstrack.di.component.DaggerActivityComponent
import com.chandana.newstrack.di.module.ActivityModule
import com.chandana.newstrack.ui.base.UiState
import com.chandana.newstrack.utils.AppConstant
import com.chandana.newstrack.utils.extensions.capitalizeWords
import com.chandana.newstrack.utils.extensions.displayErrorMessage
import com.chandana.newstrack.utils.extensions.launchCustomTab
import kotlinx.coroutines.launch
import javax.inject.Inject

class CountryNewsActivity : AppCompatActivity() {

    @Inject
    lateinit var viewModel: CountryNewsViewModel

    @Inject
    lateinit var adapter: CountryNewsAdapter

    private lateinit var binding: ActivityCountryNewsBinding

    companion object {
        fun getStartIntent(context: Context, country: Code): Intent {
            return Intent(context, CountryNewsActivity::class.java).apply {
                putExtra(AppConstant.COUNTRY, country.value)
                putExtra(AppConstant.CODE, country.id)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCountryNewsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        injectDependencies()
        setUpRecyclerView()
        val countryValue = intent.getStringExtra(AppConstant.COUNTRY)
        val countryCode = intent.getStringExtra(AppConstant.CODE)
        if (countryValue != null) {
            binding.titleTV.text = getString(
                R.string.news_countries_headline,
                countryValue.capitalizeWords()
            )
        }
        lifecycleScope.launch {
            if (countryCode != null) {
                setUpObserver(countryCode)
            }
        }
    }

    private fun setUpRecyclerView() {
        val recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.addItemDecoration(
            DividerItemDecoration(
                recyclerView.context,
                (recyclerView.layoutManager as LinearLayoutManager).orientation
            )
        )
        recyclerView.adapter = adapter
        adapter.itemClickListener = {
            launchCustomTab(it.url)
        }
    }

    private suspend fun setUpObserver(countryCode: String) {
        viewModel.getCountryNews(countryCode)
        repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.uiState.collect {
                when (it) {
                    UiState.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                        binding.recyclerView.visibility = View.GONE
                    }

                    is UiState.Error -> {
                        binding.progressBar.visibility = View.GONE
                        binding.errorMsgTV.visibility = View.VISIBLE
                        displayErrorMessage(it.message)
                    }

                    is UiState.Success -> {
                        binding.progressBar.visibility = View.GONE
                        binding.errorMsgTV.visibility = View.GONE
                        if (it.data.isEmpty()) {
                            binding.noDataDisplayTV.visibility = View.VISIBLE
                        } else {
                            binding.recyclerView.visibility = View.VISIBLE
                            setUpRecyclerData(it.data)
                        }
                    }
                }
            }
        }
    }

    private fun setUpRecyclerData(data: List<ApiSource>) {
        adapter.clearData()
        adapter.addData(data)
    }

    private fun injectDependencies() {
        DaggerActivityComponent.builder()
            .applicationComponent((application as NewsApplication).applicationComponent)
            .activityModule(ActivityModule(this)).build().inject(this)
    }
}