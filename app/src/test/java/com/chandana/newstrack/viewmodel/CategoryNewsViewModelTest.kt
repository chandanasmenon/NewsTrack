package com.chandana.newstrack.viewmodel

import app.cash.turbine.test
import com.chandana.newstrack.data.model.ApiSource
import com.chandana.newstrack.data.repository.CategoryNewsRepository
import com.chandana.newstrack.ui.base.UiState
import com.chandana.newstrack.ui.categorynews.CategoryNewsViewModel
import com.chandana.newstrack.utils.DispatcherProvider
import com.chandana.newstrack.utils.TestDispatcherProvider
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
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

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class CategoryNewsViewModelTest {
    private val category = "Business"

    @Mock
    private lateinit var repository: CategoryNewsRepository

    private lateinit var viewModel: CategoryNewsViewModel

    private lateinit var dispatcherProvider: DispatcherProvider

    @Before
    fun setUp() {
        dispatcherProvider = TestDispatcherProvider()
        viewModel = CategoryNewsViewModel(
            repository = repository,
            dispatcherProvider = dispatcherProvider
        )
    }

    @Test
    fun getCategories_whenRepositoryResponseSuccess_shouldSetSuccessUiState() {
        runTest {
            doReturn(flowOf(emptyList<String>()))
                .`when`(repository)
                .getCategories()
            viewModel.getCategories()
            viewModel.uiStateCategory.test {
                assertEquals(UiState.Success(emptyList<String>()), awaitItem())
                cancelAndIgnoreRemainingEvents()
            }
            verify(repository, times(1)).getCategories()
        }
    }

    @Test
    fun getCategories_whenRepositoryResponseError_shouldSetErrorUiState() {
        runTest {
            val message = "IllegalStateException error occurred"
            doReturn(flow<List<String>> {
                throw IllegalStateException(message)
            }).`when`(repository)
                .getCategories()
            viewModel.getCategories()
            viewModel.uiStateCategory.test {
                assertEquals(
                    UiState.Error(message),
                    awaitItem()
                )
                cancelAndIgnoreRemainingEvents()
            }
            verify(repository, times(1)).getCategories()
        }
    }

    @Test
    fun getCategoryBasedNews_whenRepositoryResponseSuccess_shouldSetSuccessUiState() {
        runTest {
            doReturn(flowOf(emptyList<List<ApiSource>>()))
                .`when`(repository)
                .getCategoryNews(category)
            viewModel.getCategoryBasedNews(category)
            viewModel.uiState.test {
                assertEquals(UiState.Success(emptyList<List<ApiSource>>()), awaitItem())
                cancelAndIgnoreRemainingEvents()
            }
            verify(repository, times(1)).getCategoryNews(category)
        }
    }

    @Test
    fun getCategoryBasedNews_whenRepositoryResponseError_shouldSetErrorUiState() {
        runTest {
            val message = "IllegalStateException error occurred"
            doReturn(flow<List<ApiSource>> {
                throw IllegalStateException(message)
            }).`when`(repository)
                .getCategoryNews(category)
            viewModel.getCategoryBasedNews(category)
            viewModel.uiState.test {
                assertEquals(
                    UiState.Error(message),
                    awaitItem()
                )
                cancelAndIgnoreRemainingEvents()
            }
            verify(repository, times(1)).getCategoryNews(category)
        }
    }
}