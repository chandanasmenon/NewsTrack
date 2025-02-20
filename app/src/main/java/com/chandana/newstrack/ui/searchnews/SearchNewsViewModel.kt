package com.chandana.newstrack.ui.searchnews

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chandana.newstrack.data.model.Article
import com.chandana.newstrack.data.repository.SearchRepository
import com.chandana.newstrack.ui.base.UiState
import com.chandana.newstrack.utils.AppConstant
import com.chandana.newstrack.utils.DispatcherProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchNewsViewModel @Inject constructor(
    private val repository: SearchRepository,
    private val dispatcherProvider: DispatcherProvider
) : ViewModel() {
    private val searchQuery = MutableStateFlow("")
    private val _uiStateSearchNews = MutableStateFlow<UiState<List<Article>>>(UiState.Loading)
    val uiStateSearchNews: StateFlow<UiState<List<Article>>> = _uiStateSearchNews

    private val _uiStateFilterSearchNews = MutableStateFlow<UiState<List<Article>>>(UiState.Loading)
    val uiStateFilterSearchNews: StateFlow<UiState<List<Article>>> = _uiStateFilterSearchNews

    private val _uiStateFilterData = MutableStateFlow<UiState<List<String>>>(UiState.Loading)
    val uiStateFilterData: StateFlow<UiState<List<String>>> = _uiStateFilterData

    init {
        getSearchNewsList()
    }

    fun setSearchQuery(query: String) {
        searchQuery.value = query
    }

    private fun getSearchNewsList() {
        viewModelScope.launch(dispatcherProvider.main) {
            searchQuery.debounce(AppConstant.DEBOUNCE_TIMEOUT)
                .filter {
                    if (it.isNotEmpty() && it.length >= AppConstant.MIN_SEARCH_CHAR) {
                        return@filter true
                    } else {
                        _uiStateSearchNews.value = UiState.Success(emptyList())
                        return@filter false
                    }
                }
                .distinctUntilChanged()
                .flatMapLatest {
                    _uiStateSearchNews.value = UiState.Loading
                    return@flatMapLatest repository.getSearchResults(searchQuery = searchQuery.value)
                        .catch { e ->
                            _uiStateSearchNews.value = UiState.Error(e.message.toString())
                        }
                }
                .flowOn(dispatcherProvider.io)
                .collect {
                    _uiStateSearchNews.value = UiState.Success(it)
                }
        }
    }

    fun getFilterData() {
        viewModelScope.launch(dispatcherProvider.main) {
            repository.getFilterData()
                .flowOn(dispatcherProvider.io)
                .catch {
                    _uiStateFilterData.value = UiState.Error(it.message.toString())
                }
                .collect {
                    _uiStateFilterData.value = UiState.Success(it)
                }
        }
    }

    fun getFilterSearchNewsList(q: String, language: String, sortBy: String) {
        viewModelScope.launch(dispatcherProvider.main) {
            repository.getFilterSearchResults(q = q, language = language, sortBy = sortBy)
                .flowOn(dispatcherProvider.io)
                .catch {
                    _uiStateFilterSearchNews.value = UiState.Error(it.message.toString())
                }
                .collect {
                    _uiStateFilterSearchNews.value = UiState.Success(it)
                }
        }
    }

}