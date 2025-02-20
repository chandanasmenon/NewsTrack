package com.chandana.newstrack.ui.offlinetopheadlines

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.chandana.newstrack.data.local.entity.Source
import com.chandana.newstrack.databinding.ActivityOfflineTopHeadlinesBinding
import com.chandana.newstrack.ui.base.UiState
import com.chandana.newstrack.utils.extensions.displayErrorMessage
import com.chandana.newstrack.utils.extensions.launchCustomTab
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class OfflineTopHeadlinesActivity : AppCompatActivity() {
    @Inject
    lateinit var adapter: OfflineTopHeadlinesAdapter

    private lateinit var viewModel: OfflineTopHeadlinesViewModel

    private lateinit var binding: ActivityOfflineTopHeadlinesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOfflineTopHeadlinesBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(this)[OfflineTopHeadlinesViewModel::class.java]
        setContentView(binding.root)
        setupUI()
        lifecycleScope.launch {
            setupObserver()
        }
    }

    private suspend fun setupObserver() {
        repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.uiState.collect {
                when (it) {
                    is UiState.Loading -> {
                        binding.progressBarOfflineTopHeadlines.visibility = View.VISIBLE
                        binding.recyclerViewOfflineTopHeadlines.visibility = View.GONE
                    }

                    is UiState.Success -> {
                        binding.progressBarOfflineTopHeadlines.visibility = View.GONE
                        binding.recyclerViewOfflineTopHeadlines.visibility = View.VISIBLE
                        setupRecyclerData(it.data)
                    }

                    is UiState.Error -> {
                        binding.progressBarOfflineTopHeadlines.visibility = View.GONE
                        binding.errorMsgOfflineTopHeadlines.visibility = View.VISIBLE
                        displayErrorMessage(it.message)
                    }
                }
            }
        }
    }

    private fun setupUI() {
        val recyclerView = binding.recyclerViewOfflineTopHeadlines
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

    private fun setupRecyclerData(data: List<Source>) {
        adapter.clearData()
        adapter.addData(data)
    }

}