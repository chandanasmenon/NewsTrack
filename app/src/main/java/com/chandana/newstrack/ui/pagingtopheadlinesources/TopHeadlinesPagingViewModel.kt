package com.chandana.newstrack.ui.pagingtopheadlinesources

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.chandana.newstrack.data.model.ApiSource
import com.chandana.newstrack.data.repository.topheadlinesources.TopHeadlinePagingRepository
import com.chandana.newstrack.utils.DispatcherProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TopHeadlinesPagingViewModel @Inject constructor(
    private val topHeadlineRepository: TopHeadlinePagingRepository,
    private val dispatcherProvider: DispatcherProvider
) :
    ViewModel() {

    private val _uiState = MutableStateFlow<PagingData<ApiSource>>(value = PagingData.empty())

    val uiState: StateFlow<PagingData<ApiSource>> = _uiState

    init {
        getPagingTopHeadlines()
    }

    private fun getPagingTopHeadlines() {
        viewModelScope.launch {
            topHeadlineRepository.getTopHeadlines()
                .flowOn(dispatcherProvider.io)
                .collect {
                    _uiState.value = it
                }
        }
    }

}
