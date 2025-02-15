package com.chandana.newstrack.ui.topheadlinesources

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chandana.newstrack.data.model.ApiSource
import com.chandana.newstrack.data.repository.TopHeadlineSourcesRepository
import com.chandana.newstrack.ui.base.UiState
import com.chandana.newstrack.utils.DispatcherProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

class TopHeadlineViewModel(
    private val repository: TopHeadlineSourcesRepository,
    private val dispatcherProvider: DispatcherProvider
) : ViewModel() {
    private val _uiState = MutableStateFlow<UiState<List<ApiSource>>>(UiState.Loading)
    val uiState: StateFlow<UiState<List<ApiSource>>> = _uiState

    init {
        getTopHeadlinesSources()
    }

    private fun getTopHeadlinesSources() {
        viewModelScope.launch(dispatcherProvider.main) {
            repository.getTopHeadlinesSources()
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