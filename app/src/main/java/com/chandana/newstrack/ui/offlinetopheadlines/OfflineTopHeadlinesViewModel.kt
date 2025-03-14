package com.chandana.newstrack.ui.offlinetopheadlines

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chandana.newstrack.data.local.entity.Source
import com.chandana.newstrack.data.repository.topheadlinesources.OfflineTopHeadlinesRepository
import com.chandana.newstrack.ui.base.UiState
import com.chandana.newstrack.utils.DispatcherProvider
import com.chandana.newstrack.utils.NetworkHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OfflineTopHeadlinesViewModel @Inject constructor(
    networkHelper: NetworkHelper,
    private val dispatcherProvider: DispatcherProvider,
    private val offlineTopHeadlinesRepository: OfflineTopHeadlinesRepository
) :
    ViewModel() {

    private val _uiState = MutableStateFlow<UiState<List<Source>>>(UiState.Loading)

    val uiState: StateFlow<UiState<List<Source>>> = _uiState

    init {
        if (networkHelper.isNetworkConnected()) {
            fetchSources()
        } else {
            fetchSourcesDirectlyFromDB()
        }
    }

    private fun fetchSources() {
        viewModelScope.launch(dispatcherProvider.main) {
            offlineTopHeadlinesRepository.getTopHeadlinesSources()
                .flowOn(dispatcherProvider.io)
                .catch { e ->
                    _uiState.value = UiState.Error(e.message.toString())
                }.collect {
                    _uiState.value = UiState.Success(it)
                }
        }
    }

    fun fetchSourcesDirectlyFromDB() {
        viewModelScope.launch(dispatcherProvider.main) {
            offlineTopHeadlinesRepository.getSourcesDirectlyFromDB()
                .flowOn(dispatcherProvider.io)
                .catch { e ->
                    _uiState.value = UiState.Error(e.message.toString())
                }.collect {
                    _uiState.value = UiState.Success(it)
                }
        }
    }

}