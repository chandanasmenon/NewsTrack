package com.chandana.newstrack.data.repository

import com.chandana.newstrack.data.local.DatabaseService
import com.chandana.newstrack.data.local.entity.Source
import com.chandana.newstrack.data.model.toSourceEntity
import com.chandana.newstrack.data.remote.NetworkService
import com.chandana.newstrack.di.ActivityScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@ActivityScope
class OfflineTopHeadlinesRepository @Inject constructor(
    private val networkService: NetworkService,
    private val databaseService: DatabaseService
) {

    fun getTopHeadlinesSources(): Flow<List<Source>> {
        return flow {
            emit(networkService.getTopHeadlineSources())
        }.map {
            it.sources.map { apiSources -> apiSources.toSourceEntity() }
        }.flatMapConcat { articles ->
            flow {
                emit(databaseService.deleteAllAndInsertAll((articles)))
            }
        }.flatMapConcat {
            databaseService.getSources()
        }
    }

    fun getSourcesDirectlyFromDB(): Flow<List<Source>> {
        return databaseService.getSources()
    }
}