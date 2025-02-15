package com.chandana.newstrack.ui.pagingtopheadlinesources

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.chandana.newstrack.NewsApplication
import com.chandana.newstrack.databinding.ActivityPagingTopheadlinesBinding
import com.chandana.newstrack.di.component.DaggerActivityComponent
import com.chandana.newstrack.di.module.ActivityModule
import com.chandana.newstrack.ui.base.UiState
import com.chandana.newstrack.ui.base.UiState.Success
import com.chandana.newstrack.utils.extensions.displayErrorMessage
import com.chandana.newstrack.utils.extensions.launchCustomTab
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

class TopHeadlinesPagingActivity : AppCompatActivity() {

    @Inject
    lateinit var viewModel: TopHeadlinesPagingViewModel

    @Inject
    lateinit var adapter: TopHeadlinesPagingAdapter

    private lateinit var binding: ActivityPagingTopheadlinesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPagingTopheadlinesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        injectDependencies()
        setupUI()
        lifecycleScope.launch {
            setupObserver()
        }
    }

    private suspend fun setupObserver() {
        repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.uiState.collectLatest {
                when (it) {
                    is UiState.Loading -> {
                        binding.progressBarPagingTopHeadlines.visibility = View.VISIBLE
                        binding.recyclerViewPagingTopHeadlines.visibility = View.GONE
                    }

                    is Success -> {
                        binding.progressBarPagingTopHeadlines.visibility = View.GONE
                        binding.recyclerViewPagingTopHeadlines.visibility = View.VISIBLE
                        adapter.submitData(it.data)
                    }

                    is UiState.Error -> {
                        binding.progressBarPagingTopHeadlines.visibility = View.GONE
                        binding.errorMsgPagingTopHeadlines.visibility = View.VISIBLE
                        displayErrorMessage(it.message)
                    }
                }
            }
        }
    }

    private fun setupUI() {
        val recyclerView = binding.recyclerViewPagingTopHeadlines
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

    private fun injectDependencies() {
        DaggerActivityComponent.builder()
            .applicationComponent((application as NewsApplication).applicationComponent)
            .activityModule(ActivityModule(this)).build().inject(this)
    }

}