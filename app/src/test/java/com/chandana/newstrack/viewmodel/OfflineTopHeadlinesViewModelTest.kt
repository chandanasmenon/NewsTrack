package com.chandana.newstrack.viewmodel

import app.cash.turbine.test
import com.chandana.newstrack.data.local.entity.Source
import com.chandana.newstrack.data.repository.topheadlinesources.OfflineTopHeadlinesRepository
import com.chandana.newstrack.ui.base.UiState
import com.chandana.newstrack.ui.offlinetopheadlines.OfflineTopHeadlinesViewModel
import com.chandana.newstrack.utils.DispatcherProvider
import com.chandana.newstrack.utils.NetworkHelper
import com.chandana.newstrack.utils.TestDispatcherProvider
import com.chandana.newstrack.utils.TestNetworkHelper
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
class OfflineTopHeadlinesViewModelTest {
    @Mock
    lateinit var offlineTopHeadlinesRepository: OfflineTopHeadlinesRepository

    private lateinit var networkHelper: NetworkHelper

    private lateinit var dispatcherProvider: DispatcherProvider

    @Before
    fun setUp() {
        networkHelper = TestNetworkHelper()
        dispatcherProvider = TestDispatcherProvider()
    }

    @Test
    fun fetchSources_whenRepositoryResponseSuccess_shouldSetSuccessUiState() {
        runTest {
            val source =
                Source(1, "category", "country", "description", "id", "language", "name", "url")
            val sourceList = mutableListOf<Source>()
            sourceList.add(source)
            doReturn(flowOf(sourceList))
                .`when`(offlineTopHeadlinesRepository)
                .getTopHeadlinesSources()
            val viewModel = OfflineTopHeadlinesViewModel(
                networkHelper,
                dispatcherProvider,
                offlineTopHeadlinesRepository
            )
            viewModel.uiState.test {
                assertEquals(UiState.Success(sourceList), awaitItem())
                cancelAndIgnoreRemainingEvents()
            }
            verify(offlineTopHeadlinesRepository, times(1)).getTopHeadlinesSources()
        }
    }

    @Test
    fun fetchSources_whenRepositoryResponseError_shouldSetErrorUiState() {
        runTest {
            val errorMessage = "IllegalStateException occurred"
            doReturn(flow<List<Source>> {
                throw IllegalStateException(errorMessage)
            })
                .`when`(offlineTopHeadlinesRepository)
                .getTopHeadlinesSources()
            val viewModel = OfflineTopHeadlinesViewModel(
                networkHelper,
                dispatcherProvider,
                offlineTopHeadlinesRepository
            )
            viewModel.uiState.test {
                assertEquals(UiState.Error(errorMessage), awaitItem())
                cancelAndIgnoreRemainingEvents()
            }
            verify(offlineTopHeadlinesRepository, times(1)).getTopHeadlinesSources()
        }
    }

    @Test
    fun fetchSourcesDirectlyFromDB_whenRepositoryResponseSuccess_shouldSetSuccessUiState() {
        runTest {
            val source =
                Source(1, "category", "country", "description", "id", "language", "name", "url")
            val sourceList = mutableListOf<Source>()
            sourceList.add(source)
            doReturn(flowOf(sourceList))
                .`when`(offlineTopHeadlinesRepository)
                .getSourcesDirectlyFromDB()
            val viewModel = OfflineTopHeadlinesViewModel(
                networkHelper,
                dispatcherProvider,
                offlineTopHeadlinesRepository
            )
            viewModel.fetchSourcesDirectlyFromDB()
            viewModel.uiState.test {
                assertEquals(UiState.Success(sourceList), awaitItem())
                cancelAndIgnoreRemainingEvents()
            }
            verify(offlineTopHeadlinesRepository, times(1)).getSourcesDirectlyFromDB()
        }
    }

    @Test
    fun fetchSourcesDirectlyFromDB_whenRepositoryResponseError_shouldSetErrorUiState() {
        runTest {
            val errorMessage = "IllegalStateException occurred"
            doReturn(flow<List<Source>> {
                throw IllegalStateException(errorMessage)
            })
                .`when`(offlineTopHeadlinesRepository)
                .getSourcesDirectlyFromDB()
            val viewModel = OfflineTopHeadlinesViewModel(
                networkHelper,
                dispatcherProvider,
                offlineTopHeadlinesRepository
            )
            viewModel.fetchSourcesDirectlyFromDB()
            viewModel.uiState.test {
                assertEquals(UiState.Error(errorMessage), awaitItem())
                cancelAndIgnoreRemainingEvents()
            }
            verify(offlineTopHeadlinesRepository, times(1)).getSourcesDirectlyFromDB()
        }
    }

}