package com.chandana.newstrack.data.repository.topheadlinesources

import com.chandana.newstrack.data.model.ApiSource
import com.chandana.newstrack.data.remote.NetworkService
import com.chandana.newstrack.di.ActivityScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@ActivityScope
class TopHeadlineSourcesRepository @Inject constructor(
    private val networkService: NetworkService
) {
    fun getTopHeadlinesSources(): Flow<List<ApiSource>> {
        return flow {
            emit(networkService.getTopHeadlineSources())
        }.map {
            it.sources
        }
    }
}