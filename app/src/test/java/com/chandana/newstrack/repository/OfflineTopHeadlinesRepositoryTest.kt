package com.chandana.newstrack.repository

import app.cash.turbine.test
import com.chandana.newstrack.data.local.DatabaseService
import com.chandana.newstrack.data.local.entity.Source
import com.chandana.newstrack.data.model.ApiSource
import com.chandana.newstrack.data.model.TopHeadlineSources
import com.chandana.newstrack.data.remote.NetworkService
import com.chandana.newstrack.data.repository.topheadlinesources.OfflineTopHeadlinesRepository
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.doNothing
import org.mockito.Mockito.doReturn
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class OfflineTopHeadlinesRepositoryTest {
    @Mock
    lateinit var networkService: NetworkService

    @Mock
    lateinit var databaseService: DatabaseService

    private lateinit var repository: OfflineTopHeadlinesRepository

    @Before
    fun setUp() {
        repository = OfflineTopHeadlinesRepository(networkService, databaseService)
    }

    @Test
    fun getTopHeadlinesSources_whenDatabaseServiceResponseSuccess_shouldReturnSuccess() {
        runTest {
            val apiSource =
                ApiSource("category", "country", "description", "id", "language", "name", "url")
            val apiSourceList = mutableListOf<ApiSource>()
            apiSourceList.add(apiSource)
            val topHeadlinesResponse = TopHeadlineSources(
                status = "ok", sources = apiSourceList
            )
            val source = Source(
                sourceId = 0,
                category = "category",
                country = "country",
                description = "description",
                id = "id",
                language = "language",
                name = "name", url = "url"
            )
            val sourceList = mutableListOf<Source>()
            sourceList.add(source)
            doReturn(topHeadlinesResponse)
                .`when`(networkService)
                .getTopHeadlineSources()
            doNothing()
                .`when`(databaseService)
                .deleteAllAndInsertAll(sourceList)
            doReturn(flowOf(sourceList))
                .`when`(databaseService)
                .getSources()
            repository.getTopHeadlinesSources().test {
                assertEquals(sourceList, awaitItem())
                cancelAndIgnoreRemainingEvents()
            }
            verify(networkService, times(1)).getTopHeadlineSources()
            verify(databaseService, times(1)).deleteAllAndInsertAll(sourceList)
            verify(databaseService, times(1)).getSources()
        }
    }

    @Test
    fun getTopHeadlinesSources_whenDatabaseServiceResponseError_shouldReturnError() {
        runTest {
            val errorMessage = "Runtime exception occurred"
            val apiSource =
                ApiSource("category", "country", "description", "id", "language", "name", "url")
            val apiList = mutableListOf<ApiSource>()
            apiList.add(apiSource)
            val topHeadlinesResponse = TopHeadlineSources(
                status = "ok", sources = apiList
            )
            val source = Source(
                sourceId = 0,
                category = "category",
                country = "country",
                description = "description",
                id = "id",
                language = "language",
                name = "name", url = "url"
            )
            val sourceList = mutableListOf<Source>()
            sourceList.add(source)
            doReturn(topHeadlinesResponse)
                .`when`(networkService)
                .getTopHeadlineSources()
            doNothing()
                .`when`(databaseService)
                .deleteAllAndInsertAll(sourceList)
            doReturn(flow<List<Source>> {
                throw RuntimeException(errorMessage)
            })
                .`when`(databaseService)
                .getSources()
            repository.getTopHeadlinesSources().test {
                assertEquals(RuntimeException(errorMessage).message, awaitError().message)
                cancelAndIgnoreRemainingEvents()
            }
            verify(networkService, times(1)).getTopHeadlineSources()
            verify(databaseService, times(1)).deleteAllAndInsertAll(sourceList)
            verify(databaseService, times(1)).getSources()
        }
    }

    @Test
    fun getSourcesDirectlyFromDB_whenDatabaseServiceResponseSuccess_shouldReturnSuccess() {
        runTest {
            val source = Source(
                sourceId = 0,
                category = "category",
                country = "country",
                description = "description",
                id = "id",
                language = "language",
                name = "name", url = "url"
            )
            val sourceList = mutableListOf<Source>()
            sourceList.add(source)
            doReturn(flowOf(sourceList))
                .`when`(databaseService)
                .getSources()
            repository.getSourcesDirectlyFromDB().test {
                assertEquals(sourceList, awaitItem())
                cancelAndIgnoreRemainingEvents()
            }
            verify(databaseService, times(1)).getSources()
        }
    }

    @Test
    fun getSourcesDirectlyFromDB_whenDatabaseServiceResponseError_shouldReturnError() {
        runTest {
            val errorMessage = "Runtime exception occurred"
            doReturn(flow<List<Source>> {
                throw RuntimeException(errorMessage)
            })
                .`when`(databaseService)
                .getSources()
            repository.getSourcesDirectlyFromDB().test {
                assertEquals(RuntimeException(errorMessage).message, awaitError().message)
                cancelAndIgnoreRemainingEvents()
            }
            verify(databaseService, times(1)).getSources()
        }
    }
}