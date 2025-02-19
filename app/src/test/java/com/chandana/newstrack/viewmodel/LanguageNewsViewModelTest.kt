package com.chandana.newstrack.viewmodel

import app.cash.turbine.test
import com.chandana.newstrack.data.model.ApiSource
import com.chandana.newstrack.data.model.Code
import com.chandana.newstrack.data.repository.LanguageNewsRepository
import com.chandana.newstrack.ui.base.UiState
import com.chandana.newstrack.ui.languagenews.LanguageNewsViewModel
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
class LanguageNewsViewModelTest {
    @Mock
    lateinit var repository: LanguageNewsRepository
    private lateinit var dispatcherProvider: DispatcherProvider
    private lateinit var viewModel: LanguageNewsViewModel
    private val languageCode = Code("en", "English")

    @Before
    fun setup() {
        dispatcherProvider = TestDispatcherProvider()
        viewModel = LanguageNewsViewModel(repository, dispatcherProvider)
    }

    @Test
    fun getLanguagesList_whenRepositoryResponseSuccess_shouldSetSuccessUiState() {
        runTest {
            doReturn(flowOf(emptyList<List<Code>>()))
                .`when`(repository)
                .getLanguageList()
            viewModel.getLanguagesList()
            viewModel.uiStateLanguage.test {
                assertEquals(UiState.Success(emptyList<List<Code>>()), awaitItem())
                cancelAndIgnoreRemainingEvents()
            }
            verify(repository, times(1)).getLanguageList()
        }
    }

    @Test
    fun getLanguagesList_whenRepositoryResponseError_shouldSetErrorUiState() {
        runTest {
            val message = "IllegalStateException occurred"
            doReturn(flow<List<Code>> {
                throw IllegalStateException(message)
            })
                .`when`(repository)
                .getLanguageList()
            viewModel.getLanguagesList()
            viewModel.uiStateLanguage.test {
                assertEquals(
                    UiState.Error(message),
                    awaitItem()
                )
                cancelAndIgnoreRemainingEvents()
            }
            verify(repository, times(1)).getLanguageList()
        }
    }

    @Test
    fun getLanguageBasedNews_whenRepositoryResponseSuccess_shouldSetSuccessUiState() {
        runTest {
            doReturn(flowOf(emptyList<List<ApiSource>>()))
                .`when`(repository)
                .getLanguageBasedNews(languageCode.id)
            viewModel.getLanguageBasedNews(languageCode.id)
            viewModel.uiState.test {
                assertEquals(UiState.Success(emptyList<List<ApiSource>>()), awaitItem())
                cancelAndIgnoreRemainingEvents()
            }
            verify(repository, times(1)).getLanguageBasedNews(languageCode.id)
        }
    }

    @Test
    fun getLanguageBasedNews_whenRepositoryResponseError_shouldSetErrorUiState() {
        runTest {
            val message = "IllegalStateException occurred"
            doReturn(flow<List<ApiSource>> {
                throw IllegalStateException(message)
            })
                .`when`(repository)
                .getLanguageBasedNews(languageCode.id)
            viewModel.getLanguageBasedNews(languageCode.id)
            viewModel.uiState.test {
                assertEquals(
                    UiState.Error(message),
                    awaitItem()
                )
                cancelAndIgnoreRemainingEvents()
            }
            verify(repository, times(1)).getLanguageBasedNews(languageCode.id)
        }
    }
}