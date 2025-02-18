package com.chandana.newstrack.repository

import app.cash.turbine.test
import com.chandana.newstrack.data.model.ApiSource
import com.chandana.newstrack.data.model.Code
import com.chandana.newstrack.data.model.NewsSourcesResponse
import com.chandana.newstrack.data.remote.NetworkService
import com.chandana.newstrack.data.repository.LanguageNewsRepository
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
class LanguageNewsRepositoryTest {
    @Mock
    lateinit var networkService: NetworkService
    private lateinit var repository: LanguageNewsRepository
    private val languageCode = Code("en", "English")

    @Before
    fun setUp() {
        repository = LanguageNewsRepository(networkService)
    }

    @Test
    fun getLanguageList_whenAppConstantResponseSuccess_shouldReturnSuccess() {
        runTest {
            val languageList = listOf(
                Code("ar", "Arabic"),
                Code("de", "German"),
                Code("en", "English"),
                Code("es", "Spanish"),
                Code("fr", "French"),
                Code("he", "Hebrew"),
                Code("it", "Italian"),
                Code("nl", "Dutch"),
                Code("no", "Norwegian"),
                Code("pt", "Portuguese"),
                Code("ru", "Russian"),
                Code("sv", "Swedish"),
                Code("ud", "Urdu"),
                Code("zh", "Chinese")
            )
            repository.getLanguageList().test {
                assertEquals(languageList, awaitItem())
                cancelAndIgnoreRemainingEvents()
            }
        }
    }

    @Test
    fun getLanguageBasedNews_whenNetworkServiceResponseSuccess_shouldReturnSuccess() {
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
            val source =
                ApiSource("category", "country", "description", "id", "language", "name", "url")
            sourceList.add(sourceOne)
            sourceList.add(sourceTwo)
            sourceList.add(source)
            val languageNewsResponse = NewsSourcesResponse(sourceList, "ok")
            doReturn(languageNewsResponse)
                .`when`(networkService)
                .getLanguageBasedNews(
                    languageCode.id
                )
            repository.getLanguageBasedNews(languageCode.id).test {
                assertEquals(languageNewsResponse.sources, awaitItem())
                cancelAndIgnoreRemainingEvents()
            }
            verify(networkService, times(1)).getLanguageBasedNews(languageCode.id)
        }
    }

    @Test
    fun getLanguageBasedNews_whenNetworkServiceResponseError_shouldReturnError() {
        runTest {
            val message = "RuntimeException occurred"
            doThrow(RuntimeException(message))
                .`when`(networkService)
                .getLanguageBasedNews(languageCode.id)
            repository.getLanguageBasedNews(languageCode.id).test {
                assertEquals(RuntimeException(message).message.toString(), awaitError().message)
                cancelAndIgnoreRemainingEvents()
            }
            verify(networkService, times(1)).getLanguageBasedNews(languageCode.id)
        }
    }
}