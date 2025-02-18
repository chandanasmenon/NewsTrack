package com.chandana.newstrack.repository

import app.cash.turbine.test
import com.chandana.newstrack.data.model.ApiSource
import com.chandana.newstrack.data.model.TopHeadlineSources
import com.chandana.newstrack.data.remote.NetworkService
import com.chandana.newstrack.data.repository.topheadlinesources.TopHeadlineSourcesRepository
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
class TopHeadlineRepositoryTest {
    @Mock
    lateinit var networkService: NetworkService

    private lateinit var repository: TopHeadlineSourcesRepository

    @Before
    fun setUp() {
        repository = TopHeadlineSourcesRepository(networkService)
    }

    @Test
    fun getTopHeadlinesSources_whenNetworkServiceResponseSuccess_shouldReturnSuccess() {
        runTest {
            val source =
                ApiSource("category", "country", "description", "id", "language", "name", "url")
            val list = mutableListOf<ApiSource>()
            list.add(source)
            val topHeadlinesResponse = TopHeadlineSources(
                status = "ok", sources = list
            )
            doReturn(topHeadlinesResponse)
                .`when`(networkService)
                .getTopHeadlineSources()
            repository.getTopHeadlinesSources().test {
                assertEquals(topHeadlinesResponse.sources, awaitItem())
                cancelAndIgnoreRemainingEvents()
            }
            verify(networkService, times(1)).getTopHeadlineSources()
        }

    }

    @Test
    fun getTopHeadlinesSources_whenNetworkServiceResponseError_shouldReturnError() {
        runTest {
            val error = "RuntimeException occurred"
            doThrow(RuntimeException(error))
                .`when`(networkService)
                .getTopHeadlineSources()
            repository.getTopHeadlinesSources().test {
                assertEquals(RuntimeException(error).message.toString(), awaitError().message)
                cancelAndIgnoreRemainingEvents()
            }
            verify(networkService, times(1)).getTopHeadlineSources()
        }
    }
}
