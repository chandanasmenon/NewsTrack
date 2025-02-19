package com.chandana.newstrack.viewmodel

import app.cash.turbine.test
import com.chandana.newstrack.data.model.Article
import com.chandana.newstrack.data.model.Entity
import com.chandana.newstrack.data.repository.SearchRepository
import com.chandana.newstrack.ui.base.UiState
import com.chandana.newstrack.ui.searchnews.SearchNewsViewModel
import com.chandana.newstrack.utils.AppConstant
import com.chandana.newstrack.utils.DispatcherProvider
import com.chandana.newstrack.utils.TestDispatcherProvider
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.doReturn
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class SearchNewsViewModelTest {

    @Mock
    lateinit var repository: SearchRepository

    private lateinit var dispatcherProvider: DispatcherProvider
    private lateinit var viewModel: SearchNewsViewModel
    private var searchData = "Android"
    private var language = "en"
    private var sortBy = "publishedAt"

    @Before
    fun setUp() {
        AppConstant.DEBOUNCE_TIMEOUT = 0L
        dispatcherProvider = TestDispatcherProvider()
        viewModel = SearchNewsViewModel(repository, dispatcherProvider)
    }

    @Test
    fun getSearchNewsList_whenRepositoryResponseSuccess_shouldSetSuccessUiState() {
        runTest {
            val dummyEntityOne = Entity("id1", "name1")
            val dummyArticleOne = Article(
                "author1", "content1", "description1",
                "publishedAt1", dummyEntityOne, "title1", "url1", "urlToImage1"
            )
            val dummyEntityTwo = Entity("id2", "name2")
            val dummyArticleTwo = Article(
                "author2", "content2", "description2",
                "publishedAt2", dummyEntityTwo, "title2", "url2", "urlToImage2"
            )
            val articleList = mutableListOf<Article>()
            articleList.add(dummyArticleOne)
            articleList.add(dummyArticleTwo)
            doReturn(flowOf(articleList))
                .`when`(repository)
                .getSearchResults(searchData)
            viewModel.setSearchQuery(searchData)
            viewModel.uiStateSearchNews.test {
                assertEquals(UiState.Success(articleList), awaitItem())
                cancelAndIgnoreRemainingEvents()
            }
            verify(repository, times(1)).getSearchResults(searchData)
        }
    }

    @Test
    fun getSearchNewsList_whenRepositoryResponseError_shouldSetErrorUiState() {
        runTest {
            val message = "IllegalStateException occurred"
            doReturn(flow<List<Article>> {
                throw IllegalStateException(message)
            }).`when`(repository)
                .getSearchResults(searchData)
            viewModel.setSearchQuery(searchData)
            viewModel.uiStateSearchNews.test {
                assertEquals(
                    UiState.Error(message),
                    awaitItem()
                )
                cancelAndIgnoreRemainingEvents()
            }
            verify(repository, times(1)).getSearchResults(searchData)
        }
    }

    @Test
    fun getFilterData_whenRepositoryResponseSuccess_shouldSetSuccessUiState() {
        runTest {
            val filterList = listOf("Language", "Sort By")
            doReturn(flowOf(filterList))
                .`when`(repository)
                .getFilterData()
            viewModel.getFilterData()
            viewModel.uiStateFilterData.test {
                assertEquals(UiState.Success(filterList), awaitItem())
                cancelAndIgnoreRemainingEvents()
            }
        }
        verify(repository, times(1)).getFilterData()
    }

    @Test
    fun getFilterData_whenRepositoryResponseError_shouldSetErrorUiState() {
        runTest {
            val message = "IllegalStateException occurred"
            doReturn(flow<List<String>> {
                throw IllegalStateException(message)
            }).`when`(repository)
                .getFilterData()
            viewModel.getFilterData()
            viewModel.uiStateFilterData.test {
                assertEquals(
                    UiState.Error(message),
                    awaitItem()
                )
                cancelAndIgnoreRemainingEvents()
            }
            verify(repository, times(1)).getFilterData()
        }
    }

    @Test
    fun getFilterSearchNewsList_whenRepositoryResponseSuccess_shouldSetSuccessUiState() {
        runTest {
            val dummyEntityOne = Entity("id1", "name1")
            val dummyArticleOne = Article(
                "author1", "content1", "description1",
                "publishedAt1", dummyEntityOne, "title1", "url1", "urlToImage1"
            )
            val dummyEntityTwo = Entity("id2", "name2")
            val dummyArticleTwo = Article(
                "author2", "content2", "description2",
                "publishedAt2", dummyEntityTwo, "title2", "url2", "urlToImage2"
            )
            val articleList = mutableListOf<Article>()
            articleList.add(dummyArticleOne)
            articleList.add(dummyArticleTwo)
            doReturn(flowOf(articleList))
                .`when`(repository)
                .getFilterSearchResults(q = searchData, language = language, sortBy = sortBy)
            viewModel.getFilterSearchNewsList(q = searchData, language = language, sortBy = sortBy)
            viewModel.uiStateFilterSearchNews.test {
                assertEquals(UiState.Success(articleList), awaitItem())
                cancelAndIgnoreRemainingEvents()
            }
            verify(repository, times(1)).getFilterSearchResults(
                q = searchData,
                language = language,
                sortBy = sortBy
            )
        }
    }

    @Test
    fun getFilterSearchNewsList_whenRepositoryResponseError_shouldSetErrorUiState() {
        runTest {
            val message = "IllegalStateException occurred"
            doReturn(flow<List<Article>> {
                throw IllegalStateException(message)
            }).`when`(repository)
                .getFilterSearchResults(q = searchData, language = language, sortBy = sortBy)
            viewModel.getFilterSearchNewsList(q = searchData, language = language, sortBy = sortBy)
            viewModel.uiStateFilterSearchNews.test {
                assertEquals(
                    UiState.Error(message),
                    awaitItem()
                )
                cancelAndIgnoreRemainingEvents()
            }
            verify(repository, times(1)).getFilterSearchResults(
                q = searchData,
                language = language,
                sortBy = sortBy
            )
        }
    }

    @After
    fun teardown() {
        AppConstant.DEBOUNCE_TIMEOUT = 300L
    }
}