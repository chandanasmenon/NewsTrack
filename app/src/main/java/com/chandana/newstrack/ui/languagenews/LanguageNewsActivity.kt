package com.chandana.newstrack.ui.languagenews

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
import com.chandana.newstrack.data.model.Code
import com.chandana.newstrack.databinding.ActivityLanguageNewsBinding
import com.chandana.newstrack.ui.base.UiState
import com.chandana.newstrack.utils.AppConstant
import com.chandana.newstrack.utils.extensions.capitalizeWords
import com.chandana.newstrack.utils.extensions.displayErrorMessage
import com.chandana.newstrack.utils.extensions.launchCustomTab
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class LanguageNewsActivity : AppCompatActivity() {

    private lateinit var viewModel: LanguageNewsViewModel

    @Inject
    lateinit var adapter: LanguageNewsAdapter

    private lateinit var binding: ActivityLanguageNewsBinding

    companion object {
        fun getStartIntent(context: Context, code: Code): Intent {
            return Intent(context, LanguageNewsActivity::class.java).apply {
                putExtra(AppConstant.LANGUAGE, code.value)
                putExtra(AppConstant.CODE, code.id)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLanguageNewsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this)[LanguageNewsViewModel::class.java]
        val languageValue = intent.getStringExtra(AppConstant.LANGUAGE)
        val languageCode = intent.getStringExtra(AppConstant.CODE)
        if (languageValue != null) {
            binding.titleTV.text = getString(
                R.string.news_languages_headline,
                languageValue.capitalizeWords()
            )
        }
        setUpRecyclerView()
        lifecycleScope.launch {
            if (languageValue != null && languageCode != null) {
                val code = Code(languageCode, languageValue)
                setUpObserver(code)
            }
        }

    }

    private suspend fun setUpObserver(code: Code) {
        viewModel.getLanguageBasedNews(code.id)
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
                            binding.noDataDisplayTV.text =
                                getString(R.string.language_news_empty_display_text, code.value)
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