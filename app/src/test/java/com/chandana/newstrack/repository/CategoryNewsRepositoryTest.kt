package com.chandana.newstrack.repository

import app.cash.turbine.test
import com.chandana.newstrack.data.model.ApiSource
import com.chandana.newstrack.data.model.NewsSourcesResponse
import com.chandana.newstrack.data.remote.NetworkService
import com.chandana.newstrack.data.repository.CategoryNewsRepository
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
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

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class CategoryNewsRepositoryTest {
    private val category = "Business"

    @Mock
    lateinit var networkService: NetworkService

    private lateinit var repository: CategoryNewsRepository

    @Before
    fun setUp() {
        repository = CategoryNewsRepository(networkService)
    }

    @Test
    fun getCategories_whenAppConstantResponseSuccess_shouldReturnSuccess() {
        runTest {
            val categoryList =
                listOf(
                    "business",
                    "entertainment",
                    "general",
                    "health",
                    "science",
                    "sports",
                    "technology"
                )
            repository.getCategories().test {
                assertEquals(categoryList, awaitItem())
                cancelAndIgnoreRemainingEvents()
            }
        }
    }

    @Test
    fun getCategoryNews_whenNetworkServiceResponseSuccess_shouldReturnSuccess() {
        runTest {
            val sourceList = mutableListOf<ApiSource>()
            val sourceOne = ApiSource(
                "general",
                "us",
                "Your trusted source for breaking news, analysis, exclusive interviews, headlines, and videos at ABCNews.com.",
                "abc-news",
                "en",
                "ABC News",
                "https://abcnews.go.com"
            )
            val sourceTwo = ApiSource(
                "general",
                "au",
                "Australia's most trusted source of local, national and world news. Comprehensive, independent, in-depth analysis, the latest business, sport, weather and more.",
                "abc-news-au",
                "en",
                "ABC News (AU)",
                "https://www.abc.net.au/news"
            )
            sourceList.add(sourceOne)
            sourceList.add(sourceTwo)
            val categoryNewsResponse = NewsSourcesResponse(sourceList, "ok")
            doReturn(categoryNewsResponse)
                .`when`(networkService)
                .getCategoryBasedNews(category)
            repository.getCategoryNews(category).test {
                assertEquals(categoryNewsResponse.sources, awaitItem())
                cancelAndIgnoreRemainingEvents()
            }
            verify(networkService, times(1)).getCategoryBasedNews(category)
        }
    }

    @Test
    fun getCategoryNews_whenNetworkServiceResponseError_shouldReturnError() {
        runTest {
            val message = "RuntimeException occurred"
            doThrow(RuntimeException(message))
                .`when`(networkService)
                .getCategoryBasedNews(category)
            repository.getCategoryNews(category).test {
                assertEquals(RuntimeException(message).message, awaitError().message)
                cancelAndIgnoreRemainingEvents()
            }
            verify(networkService, times(1)).getCategoryBasedNews(category)
        }
    }

}