package com.chandana.newstrack.repository

import app.cash.turbine.test
import com.chandana.newstrack.data.model.Article
import com.chandana.newstrack.data.model.Entity
import com.chandana.newstrack.data.model.SearchResponse
import com.chandana.newstrack.data.remote.NetworkService
import com.chandana.newstrack.data.repository.SearchRepository
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.doReturn
import org.mockito.Mockito.doThrow
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class SearchRepositoryTest {

    @Mock
    lateinit var networkService: NetworkService
    private lateinit var repository: SearchRepository
    private var searchData = "Android"
    private var language = "en"
    private var sortBy = "publishedAt"

    @Before
    fun setUp() {
        repository = SearchRepository(networkService)
    }

    @Test
    fun getSearchResults_whenNetworkServiceResponseSuccess_shouldReturnSuccess() {
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
            val searchResponse = SearchResponse(articleList, "ok", 2)
            doReturn(searchResponse)
                .`when`(networkService)
                .getSearchResults(searchData)
            repository.getSearchResults(searchData).test {
                assertEquals(searchResponse.articles, awaitItem())
                cancelAndIgnoreRemainingEvents()
            }
            verify(networkService, times(1)).getSearchResults(searchData)
        }
    }

    @Test
    fun getSearchResults_whenNetworkServiceResponseError_shouldReturnError() {
        runTest {
            val message = "RuntimeException occurred"
            doThrow(RuntimeException(message))
                .`when`(networkService)
                .getSearchResults(searchData)
            repository.getSearchResults(searchData).test {
                assertEquals(RuntimeException(message).message.toString(), awaitError().message)
                cancelAndIgnoreRemainingEvents()
            }
            verify(networkService, times(1)).getSearchResults(searchData)
        }
    }

    @Test
    fun getFilterData_whenAppConstantResponseSuccess_shouldReturnSuccess() {
        runTest {
            val filterList = listOf("Language", "Sort By")
            repository.getFilterData().test {
                assertEquals(filterList, awaitItem())
                cancelAndIgnoreRemainingEvents()
            }
        }
    }


    @Test
    fun getFilterSearchResults_whenNetworkServiceResponseSuccess_shouldReturnSuccess() {
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
            val searchResponse = SearchResponse(articleList, "ok", 2)
            doReturn(searchResponse)
                .`when`(networkService)
                .getFilterSearchResults(searchData, language, sortBy)
            repository.getFilterSearchResults(searchData, language, sortBy).test {
                assertEquals(searchResponse.articles, awaitItem())
                cancelAndIgnoreRemainingEvents()
            }
            verify(networkService, times(1)).getFilterSearchResults(searchData, language, sortBy)
        }
    }

    @Test
    fun getFilterSearchResults_whenNetworkServiceResponseError_shouldReturnError() {
        runTest {
            val message = "RuntimeException occurred"
            doThrow(RuntimeException(message))
                .`when`(networkService)
                .getFilterSearchResults(searchData, language, sortBy)
            repository.getFilterSearchResults(searchData, language, sortBy).test {
                assertEquals(RuntimeException(message).message.toString(), awaitError().message)
                cancelAndIgnoreRemainingEvents()
            }
            verify(networkService, times(1)).getFilterSearchResults(searchData, language, sortBy)
        }
    }

}