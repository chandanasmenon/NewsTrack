package com.chandana.newstrack.ui.countrynews

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chandana.newstrack.data.model.ApiSource
import com.chandana.newstrack.data.model.Code
import com.chandana.newstrack.data.repository.CountryNewsRepository
import com.chandana.newstrack.ui.base.UiState
import com.chandana.newstrack.utils.DispatcherProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject

class CountryNewsViewModel @Inject constructor(
    private val repository: CountryNewsRepository,
    private val dispatcherProvider: DispatcherProvider
) : ViewModel() {
    private val _uiStateCountry = MutableStateFlow<UiState<List<Code>>>(UiState.Loading)
    val uiStateCountry: StateFlow<UiState<List<Code>>> = _uiStateCountry

    private val _uiState = MutableStateFlow<UiState<List<ApiSource>>>(UiState.Loading)
    val uiState: StateFlow<UiState<List<ApiSource>>> = _uiState

    fun getCountriesList() {
        viewModelScope.launch(dispatcherProvider.main) {
            repository.getCountriesList()
                .flowOn(dispatcherProvider.io)
                .catch { _uiStateCountry.value = UiState.Error(it.message.toString()) }
                .collect {
                    _uiStateCountry.value = UiState.Success(it)
                }
        }
    }

    fun getCountryNews(country: String) {
        viewModelScope.launch(dispatcherProvider.main) {
            repository.getCountryNews(country)
                .flowOn(dispatcherProvider.io)
                .catch { _uiState.value = UiState.Error(it.message.toString()) }
                .collect {
                    _uiState.value = UiState.Success(it)
                }
        }
    }
}