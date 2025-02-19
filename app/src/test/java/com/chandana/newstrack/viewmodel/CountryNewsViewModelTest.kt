package com.chandana.newstrack.viewmodel

import app.cash.turbine.test
import com.chandana.newstrack.data.model.ApiSource
import com.chandana.newstrack.data.model.Code
import com.chandana.newstrack.data.repository.CountryNewsRepository
import com.chandana.newstrack.ui.base.UiState
import com.chandana.newstrack.ui.countrynews.CountryNewsViewModel
import com.chandana.newstrack.utils.DispatcherProvider
import com.chandana.newstrack.utils.TestDispatcherProvider
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.doReturn
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class CountryNewsViewModelTest {
    private val countryCode = Code("in", "India")

    @Mock
    lateinit var repository: CountryNewsRepository

    private lateinit var viewModel: CountryNewsViewModel

    private lateinit var dispatcherProvider: DispatcherProvider

    @Before
    fun setUp() {
        dispatcherProvider = TestDispatcherProvider()
        viewModel = CountryNewsViewModel(repository, dispatcherProvider)
    }

    @Test
    fun getCountriesList_whenRepositoryResponseSuccess_shouldSetSuccessUiState() {
        runTest {
            doReturn(flowOf(emptyList<Code>()))
                .`when`(repository)
                .getCountriesList()
            viewModel.getCountriesList()
            viewModel.uiStateCountry.test {
                assertEquals(UiState.Success(emptyList<Code>()), awaitItem())
                cancelAndIgnoreRemainingEvents()
            }
            verify(repository, times(1)).getCountriesList()
        }
    }

    @Test
    fun getCountriesList_whenRepositoryResponseError_shouldSetErrorUiState() {
        runTest {
            val message = "IllegalStateException occurred"
            doReturn(flow<List<Code>> {
                throw IllegalStateException(message)
            }).`when`(repository).getCountriesList()
            viewModel.getCountriesList()
            viewModel.uiStateCountry.test {
                assertEquals(
                    UiState.Error(message),
                    awaitItem()
                )
                cancelAndIgnoreRemainingEvents()
            }
            verify(repository, times(1)).getCountriesList()
        }
    }

    @Test
    fun getCountryNews_whenRepositoryResponseSuccess_shouldSetSuccessUiState() {
        runTest {
            doReturn(flowOf(emptyList<List<ApiSource>>()))
                .`when`(repository)
                .getCountryNews(countryCode.id)
            viewModel.getCountryNews(countryCode.id)
            viewModel.uiState.test {
                assertEquals(UiState.Success(emptyList<List<ApiSource>>()), awaitItem())
                cancelAndIgnoreRemainingEvents()
            }
            verify(repository, times(1)).getCountryNews(countryCode.id)
        }
    }

    @Test
    fun getCountryNews_whenRepositoryResponseError_shouldSetErrorUiState() {
        runTest {
            val message = "IllegalStateException occurred"
            doReturn(flow<List<ApiSource>> {
                throw IllegalStateException(message)
            })
                .`when`(repository)
                .getCountryNews(countryCode.id)
            viewModel.getCountryNews(countryCode.id)
            viewModel.uiState.test {
                assertEquals(
                    UiState.Error(message),
                    awaitItem()
                )
                cancelAndIgnoreRemainingEvents()
            }
            verify(repository, times(1)).getCountryNews(countryCode.id)
        }
    }

}