package com.chandana.newstrack.ui.categorynews

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chandana.newstrack.data.model.ApiSource
import com.chandana.newstrack.data.repository.CategoryNewsRepository
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
class CategoryNewsViewModel @Inject constructor(
    private val repository: CategoryNewsRepository,
    private val dispatcherProvider: DispatcherProvider
) : ViewModel() {
    private val _uiStateCategory = MutableStateFlow<UiState<List<String>>>(UiState.Loading)
    val uiStateCategory: StateFlow<UiState<List<String>>> = _uiStateCategory

    private val _uiState = MutableStateFlow<UiState<List<ApiSource>>>(UiState.Loading)
    val uiState: StateFlow<UiState<List<ApiSource>>> = _uiState

    fun getCategories() {
        viewModelScope.launch(dispatcherProvider.main) {
            repository.getCategories()
                .flowOn(dispatcherProvider.io)
                .catch {
                    _uiStateCategory.value = UiState.Error(it.message.toString())
                }
                .collect {
                    _uiStateCategory.value = UiState.Success(it)
                }
        }
    }

    fun getCategoryBasedNews(category: String) {
        viewModelScope.launch(dispatcherProvider.main) {
            repository.getCategoryNews(category)
                .flowOn(dispatcherProvider.io)
                .catch {
                    _uiState.value = UiState.Error(it.message.toString())
                }.collect {
                    _uiState.value = UiState.Success(it)
                }
        }
    }
}