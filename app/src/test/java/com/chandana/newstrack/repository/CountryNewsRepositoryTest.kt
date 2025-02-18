package com.chandana.newstrack.repository

import app.cash.turbine.test
import com.chandana.newstrack.data.model.ApiSource
import com.chandana.newstrack.data.model.Code
import com.chandana.newstrack.data.model.NewsSourcesResponse
import com.chandana.newstrack.data.remote.NetworkService
import com.chandana.newstrack.data.repository.CountryNewsRepository
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
class CountryNewsRepositoryTest {

    @Mock
    lateinit var networkService: NetworkService

    private lateinit var repository: CountryNewsRepository

    private val countryCode = Code("in", "India")

    @Before
    fun setUp() {
        repository = CountryNewsRepository(networkService)
    }

    @Test
    fun getCountriesList_whenAppConstantResponseSuccess_shouldReturnSuccess() {
        runTest {
            val countryList = listOf(
                Code("ae", "United Arab Emirates"),
                Code("ar", "Argentina"),
                Code("at", "Austria"),
                Code("au", "Australia"),
                Code("be", "Belgium"),
                Code("bg", "Bulgaria"),
                Code("br", "Brazil"),
                Code("ca", "Canada"),
                Code("ch", "Switzerland"),
                Code("cn", "China"),
                Code("co", "Colombia"),
                Code("cu", "Cuba"),
                Code("cz", "Czech Republic"),
                Code("de", "Germany"),
                Code("eg", "Egypt"),
                Code("fr", "France"),
                Code("gb", "United Kingdom"),
                Code("gr", "Greece"),
                Code("hk", "Hong Kong"),
                Code("hu", "Hungary"),
                Code("id", "Indonesia"),
                Code("ie", "Ireland"),
                Code("il", "Israel"),
                Code("in", "India"),
                Code("it", "Italy"),
                Code("jp", "Japan"),
                Code("kr", "South Korea"),
                Code("lt", "Lithuania"),
                Code("lv", "Latvia"),
                Code("ma", "Morocco"),
                Code("mx", "Mexico"),
                Code("my", "Malaysia"),
                Code("ng", "Nigeria"),
                Code("nl", "Netherlands"),
                Code("no", "Norway"),
                Code("nz", "New Zealand"),
                Code("ph", "Philippines"),
                Code("pl", "Poland"),
                Code("pt", "Portugal"),
                Code("ro", "Romania"),
                Code("rs", "Serbia"),
                Code("ru", "Russia"),
                Code("sa", "Saudi Arabia"),
                Code("se", "Sweden"),
                Code("sg", "Singapore"),
                Code("si", "Slovenia"),
                Code("sk", "Slovakia"),
                Code("th", "Thailand"),
                Code("tr", "Turkey"),
                Code("tw", "Taiwan"),
                Code("ua", "Ukraine"),
                Code("us", "United States"),
                Code("ve", "Venezuela"),
                Code("za", "South Africa")
            )
            repository.getCountriesList().test {
                assertEquals(countryList, awaitItem())
                cancelAndIgnoreRemainingEvents()
            }
        }
    }

    @Test
    fun getCountryNews_whenNetworkServiceResponseSuccess_shouldReturnSuccess() {
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
            val countryNewsResponse = NewsSourcesResponse(sourceList, "ok")
            doReturn(countryNewsResponse)
                .`when`(networkService)
                .getCountryBasedNews(countryCode.id)
            repository.getCountryNews(countryCode.id).test {
                assertEquals(countryNewsResponse.sources, awaitItem())
                cancelAndIgnoreRemainingEvents()
            }
            verify(networkService, times(1)).getCountryBasedNews(countryCode.id)
        }
    }

    @Test
    fun getCountryNews_whenNetworkServiceResponseError_shouldReturnError() {
        runTest {
            val message = "RuntimeException occurred"
            doThrow(RuntimeException(message))
                .`when`(networkService)
                .getCountryBasedNews(countryCode.id)
            repository.getCountryNews(countryCode.id).test {
                assertEquals(RuntimeException(message).message.toString(), awaitError().message)
                cancelAndIgnoreRemainingEvents()
            }
            verify(networkService, times(1)).getCountryBasedNews(countryCode.id)
        }
    }

}