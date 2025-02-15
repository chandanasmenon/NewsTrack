package com.chandana.newstrack.ui.topheadlinesources

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.chandana.newstrack.NewsApplication
import com.chandana.newstrack.data.model.ApiSource
import com.chandana.newstrack.databinding.ActivityTopHeadLineSourcesBinding
import com.chandana.newstrack.di.component.DaggerActivityComponent
import com.chandana.newstrack.di.module.ActivityModule
import com.chandana.newstrack.ui.base.UiState
import com.chandana.newstrack.utils.extensions.displayErrorMessage
import com.chandana.newstrack.utils.extensions.launchCustomTab
import kotlinx.coroutines.launch
import javax.inject.Inject

class TopHeadlineSourcesActivity : AppCompatActivity() {
    @Inject
    lateinit var viewModel: TopHeadlineViewModel

    @Inject
    lateinit var adapter: TopHeadlineSourcesAdapter

    private lateinit var binding: ActivityTopHeadLineSourcesBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTopHeadLineSourcesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        injectDependencies()
        setupUI()
        lifecycleScope.launch {
            setupObserver()
        }
    }

    private suspend fun setupObserver() {
        repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.uiState.collect {
                setUiState(it)
            }
        }
    }

    private fun setUiState(uiState: UiState<List<ApiSource>>) {
        when (uiState) {
            is UiState.Loading -> {
                binding.progressBarTopHeadlineSources.visibility = View.VISIBLE
                binding.recyclerViewTopHeadlineSources.visibility = View.GONE
            }

            is UiState.Success -> {
                binding.progressBarTopHeadlineSources.visibility = View.GONE
                binding.recyclerViewTopHeadlineSources.visibility = View.VISIBLE
                setupRecyclerData(uiState.data)
            }

            is UiState.Error -> {
                binding.progressBarTopHeadlineSources.visibility = View.GONE
                binding.errorMsgTopHeadlineTV.visibility = View.VISIBLE
                displayErrorMessage(uiState.message)
            }
        }
    }

    private fun setupUI() {
        val recyclerView = binding.recyclerViewTopHeadlineSources
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

    private fun setupRecyclerData(data: List<ApiSource>) {
        adapter.clearData()
        adapter.addData(data)
    }

    private fun injectDependencies() {
        DaggerActivityComponent.builder()
            .applicationComponent((application as NewsApplication).applicationComponent)
            .activityModule(ActivityModule(this)).build().inject(this)
    }

}