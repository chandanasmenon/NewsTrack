package com.chandana.newstrack.ui.searchnews

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.chandana.newstrack.R
import com.chandana.newstrack.data.model.Article
import com.chandana.newstrack.databinding.ActivitySearchBinding
import com.chandana.newstrack.databinding.FilterBottomScreenBinding
import com.chandana.newstrack.ui.base.UiState
import com.chandana.newstrack.utils.AppConstant
import com.chandana.newstrack.utils.extensions.displayErrorMessage
import com.chandana.newstrack.utils.extensions.launchCustomTab
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class SearchActivity : AppCompatActivity() {

    private lateinit var viewModel: SearchNewsViewModel

    @Inject
    lateinit var filterAdapter: FilterDataAdapter

    @Inject
    lateinit var searchAdapter: SearchNewsAdapter

    private lateinit var binding: ActivitySearchBinding

    private lateinit var bottomSheetDialogBinding: FilterBottomScreenBinding

    private var searchQuery = ""
    private var language = ""
    private var sortBy = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        bottomSheetDialogBinding = FilterBottomScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this)[SearchNewsViewModel::class.java]
        setUpSearchRecyclerView()
        setUpFilterRecyclerView()
        lifecycleScope.launch {
            setUpSearchNewsObserver()
        }
        binding.openBottomSheetButton.isEnabled = false
        binding.openBottomSheetButton.setOnClickListener {
            bottomSheetDialogBinding.root.parent?.let { parent ->
                (parent as ViewGroup).removeView(bottomSheetDialogBinding.root)
            }
            lifecycleScope.launch {
                setUpFilterDataObserver()
            }
            showBottomSheet()
        }
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                bottomSheetDialogBinding.radioGroupSortBy.clearCheck()
                bottomSheetDialogBinding.radioGroupLanguage.clearCheck()
                if (newText != null) {
                    searchQuery = newText.toString()
                    binding.openBottomSheetButton.isEnabled =
                        newText.length >= AppConstant.MIN_SEARCH_CHAR
                }
                viewModel.setSearchQuery(newText.toString())
                return true
            }
        })
    }

    private fun setUpFilterRecyclerView() {
        val filterRecyclerView = bottomSheetDialogBinding.filterRecyclerView
        filterRecyclerView.layoutManager = LinearLayoutManager(this)
        filterRecyclerView.adapter = filterAdapter
        filterAdapter.itemClickListener = {
            when (it) {
                getString(R.string.language_text) -> {
                    bottomSheetDialogBinding.radioGroupLanguage.visibility = View.VISIBLE
                    bottomSheetDialogBinding.sortByLayout.visibility = View.INVISIBLE
                }

                getString(R.string.sort_by_text) -> {
                    bottomSheetDialogBinding.radioGroupLanguage.visibility = View.INVISIBLE
                    bottomSheetDialogBinding.sortByLayout.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun setUpSearchRecyclerView() {
        val searchRecyclerView = binding.searchRecyclerView
        searchRecyclerView.layoutManager = LinearLayoutManager(this)
        searchRecyclerView.addItemDecoration(
            DividerItemDecoration(
                searchRecyclerView.context,
                (searchRecyclerView.layoutManager as LinearLayoutManager).orientation
            )
        )
        searchRecyclerView.adapter = searchAdapter
        searchAdapter.itemClickListener = {
            launchCustomTab(it.url.toString())
        }
    }

    private fun setUpFilterData(list: List<String>) {
        filterAdapter.clearData()
        filterAdapter.addData(list)
    }

    private fun setUpSearchData(list: List<Article>) {
        searchAdapter.clearData()
        searchAdapter.addData(list)
    }

    private fun showBottomSheet() {
        val dialog = BottomSheetDialog(this, R.style.BottomSheetTheme)
        dialog.setContentView(bottomSheetDialogBinding.root)
        dialog.show()
        bottomSheetDialogBinding.applyFilterButton.setOnClickListener {
            val selectedLanguageId =
                bottomSheetDialogBinding.radioGroupLanguage.checkedRadioButtonId
            val selectedSortById = bottomSheetDialogBinding.radioGroupSortBy.checkedRadioButtonId
            if (selectedLanguageId != -1) {
                val selectedRadioButton =
                    bottomSheetDialogBinding.radioGroupLanguage.findViewById<RadioButton>(
                        selectedLanguageId
                    )
                language = selectedRadioButton.tag.toString()
            }
            if (selectedSortById != -1) {
                val selectedRadioButton =
                    bottomSheetDialogBinding.radioGroupSortBy.findViewById<RadioButton>(
                        selectedSortById
                    )
                sortBy = selectedRadioButton.tag.toString()
            }
            lifecycleScope.launch {
                if (language.isNotEmpty() || sortBy.isNotEmpty()) {
                    setUpFilterSearchNewsObserver(searchQuery, language, sortBy)
                }
            }
            dialog.dismiss()
        }
    }

    private suspend fun setUpSearchNewsObserver() {
        repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.uiStateSearchNews.collect {
                when (it) {
                    UiState.Loading -> {
                        binding.progressBarSearch.visibility = View.VISIBLE
                        binding.searchRecyclerView.visibility = View.GONE
                    }

                    is UiState.Error -> {
                        binding.progressBarSearch.visibility = View.GONE
                        binding.errorMsgTV.visibility = View.VISIBLE
                        displayErrorMessage(it.message)
                    }

                    is UiState.Success -> {
                        binding.progressBarSearch.visibility = View.GONE
                        binding.searchRecyclerView.visibility = View.VISIBLE
                        val finalList = mutableListOf<Article>()
                        for (item in it.data) {
                            if (item.title != getString(R.string.removed_text)) {
                                finalList.add(item)
                            }
                        }
                        setUpSearchData(finalList)
                    }
                }
            }
        }
    }

    private suspend fun setUpFilterSearchNewsObserver(
        query: String,
        language: String,
        sortBy: String
    ) {
        viewModel.getFilterSearchNewsList(q = query, language = language, sortBy = sortBy)
        repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.uiStateFilterSearchNews.collect {
                when (it) {
                    UiState.Loading -> {
                        binding.progressBarSearch.visibility = View.VISIBLE
                        binding.searchRecyclerView.visibility = View.GONE
                    }

                    is UiState.Error -> {
                        binding.progressBarSearch.visibility = View.GONE
                        binding.errorMsgTV.visibility = View.VISIBLE
                        displayErrorMessage(it.message)
                    }

                    is UiState.Success -> {
                        binding.progressBarSearch.visibility = View.GONE
                        binding.searchRecyclerView.visibility = View.VISIBLE
                        val finalList = mutableListOf<Article>()
                        for (item in it.data) {
                            if (item.title != getString(R.string.removed_text)) {
                                finalList.add(item)
                            }
                        }
                        setUpSearchData(finalList)
                    }
                }
            }
        }
    }

    private suspend fun setUpFilterDataObserver() {
        viewModel.getFilterData()
        repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.uiStateFilterData.collect {
                when (it) {
                    is UiState.Loading -> {
                        bottomSheetDialogBinding.progressBarFilter.visibility = View.VISIBLE
                    }

                    is UiState.Error -> {
                        bottomSheetDialogBinding.progressBarFilter.visibility = View.GONE
                        displayErrorMessage(it.message)
                    }

                    is UiState.Success -> {
                        bottomSheetDialogBinding.progressBarFilter.visibility = View.GONE
                        setUpFilterData(it.data)
                    }
                }
            }
        }
    }

}