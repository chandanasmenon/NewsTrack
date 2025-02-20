package com.chandana.newstrack.ui.categorynews

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.chandana.newstrack.R
import com.chandana.newstrack.data.model.ApiSource
import com.chandana.newstrack.databinding.ActivityCategoryNewsBinding
import com.chandana.newstrack.ui.base.UiState
import com.chandana.newstrack.utils.AppConstant.CATEGORY
import com.chandana.newstrack.utils.extensions.capitalizeWords
import com.chandana.newstrack.utils.extensions.displayErrorMessage
import com.chandana.newstrack.utils.extensions.launchCustomTab
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class CategoryNewsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCategoryNewsBinding

    private lateinit var viewModel: CategoryNewsViewModel

    @Inject
    lateinit var adapter: CategoryNewsAdapter

    companion object {
        fun getStartIntent(context: Context, category: String): Intent {
            return Intent(context, CategoryNewsActivity::class.java)
                .apply {
                    putExtra(CATEGORY, category)
                }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCategoryNewsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this)[CategoryNewsViewModel::class.java]
        val categoryValue = intent.getStringExtra(CATEGORY) ?: ""
        binding.titleTV.text = getString(
            R.string.news_categories_headline,
            categoryValue.capitalizeWords()
        )
        setUpRecyclerView()
        lifecycleScope.launch {
            setUpObserver(categoryValue)
        }
    }

    private suspend fun setUpObserver(category: String) {
        viewModel.getCategoryBasedNews(category)
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

    private fun setUpRecyclerData(list: List<ApiSource>) {
        adapter.clearData()
        adapter.addData(list)
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

}