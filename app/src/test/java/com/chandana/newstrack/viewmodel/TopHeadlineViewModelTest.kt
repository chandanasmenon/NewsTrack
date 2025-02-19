package com.chandana.newstrack.viewmodel

import app.cash.turbine.test
import com.chandana.newstrack.data.model.Article
import com.chandana.newstrack.data.repository.topheadlinesources.TopHeadlineSourcesRepository
import com.chandana.newstrack.ui.base.UiState
import com.chandana.newstrack.ui.topheadlinesources.TopHeadlineViewModel
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
class TopHeadlineViewModelTest {

    @Mock
    private lateinit var topHeadlineSourcesRepository: TopHeadlineSourcesRepository

    private lateinit var dispatcherProvider: DispatcherProvider

    @Before
    fun setUp() {
        dispatcherProvider = TestDispatcherProvider()
    }

    @Test
    fun fetchNews_whenRepositoryResponseSuccess_shouldSetSuccessUiState() {
        runTest {
            doReturn(flowOf(emptyList<Article>()))
                .`when`(topHeadlineSourcesRepository)
                .getTopHeadlinesSources()
            val viewModel = TopHeadlineViewModel(
                repository = topHeadlineSourcesRepository,
                dispatcherProvider = dispatcherProvider
            )
            viewModel.uiState.test {
                assertEquals(UiState.Success(emptyList<List<Article>>()), awaitItem())
                cancelAndIgnoreRemainingEvents()
            }
            verify(topHeadlineSourcesRepository, times(1)).getTopHeadlinesSources()
        }
    }

    @Test
    fun fetchNews_whenRepositoryResponseError_shouldSetErrorUiState() {
        runTest {
            val errorMessage = "Error Message For You"
            doReturn(flow<List<Article>> {
                throw IllegalStateException(errorMessage)
            })
                .`when`(topHeadlineSourcesRepository)
                .getTopHeadlinesSources()
            val viewModel = TopHeadlineViewModel(
                repository = topHeadlineSourcesRepository,
                dispatcherProvider = dispatcherProvider
            )
            viewModel.uiState.test {
                assertEquals(
                    UiState.Error(errorMessage),
                    awaitItem()
                )
                cancelAndIgnoreRemainingEvents()
            }
            verify(topHeadlineSourcesRepository, times(1)).getTopHeadlinesSources()
        }
    }

}