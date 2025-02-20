package com.chandana.newstrack.ui.languagenews

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chandana.newstrack.data.model.ApiSource
import com.chandana.newstrack.data.model.Code
import com.chandana.newstrack.data.repository.LanguageNewsRepository
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
class LanguageNewsViewModel @Inject constructor(
    private val repository: LanguageNewsRepository,
    private val dispatcherProvider: DispatcherProvider
) : ViewModel() {
    private val _uiStateLanguage = MutableStateFlow<UiState<List<Code>>>(UiState.Loading)
    val uiStateLanguage: StateFlow<UiState<List<Code>>> = _uiStateLanguage

    private val _uiState = MutableStateFlow<UiState<List<ApiSource>>>(UiState.Loading)
    val uiState: StateFlow<UiState<List<ApiSource>>> = _uiState

    fun getLanguagesList() {
        viewModelScope.launch(dispatcherProvider.main) {
            repository.getLanguageList()
                .flowOn(dispatcherProvider.io)
                .catch { _uiStateLanguage.value = UiState.Error(it.message.toString()) }
                .collect {
                    _uiStateLanguage.value = UiState.Success(it)
                }
        }
    }

    fun getLanguageBasedNews(language: String) {
        viewModelScope.launch(dispatcherProvider.main) {
            repository.getLanguageBasedNews(language)
                .flowOn(dispatcherProvider.io)
                .catch { _uiState.value = UiState.Error(it.message.toString()) }
                .collect {
                    _uiState.value = UiState.Success(it)
                }
        }
    }
}