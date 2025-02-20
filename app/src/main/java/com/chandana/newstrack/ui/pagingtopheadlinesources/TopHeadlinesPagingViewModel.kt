package com.chandana.newstrack.ui.pagingtopheadlinesources

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.chandana.newstrack.data.model.ApiSource
import com.chandana.newstrack.data.repository.topheadlinesources.TopHeadlinePagingRepository
import com.chandana.newstrack.ui.base.UiState
import com.chandana.newstrack.utils.DispatcherProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TopHeadlinesPagingViewModel @Inject constructor(
    private val repository: TopHeadlinePagingRepository,
    private val dispatcherProvider: DispatcherProvider
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<PagingData<ApiSource>>>(UiState.Loading)
    val uiState: StateFlow<UiState<PagingData<ApiSource>>> = _uiState

    init {
        getTopHeadLineSources()
    }

    private fun getTopHeadLineSources() {
        viewModelScope.launch(dispatcherProvider.main) {
            repository.getTopHeadlines().cachedIn(viewModelScope)
                .flowOn(dispatcherProvider.io)
                .catch { e ->
                    _uiState.value = UiState.Error(e.message.toString())
                }
                .collect {
                    _uiState.value = UiState.Success(it)
                }
        }
    }
}
