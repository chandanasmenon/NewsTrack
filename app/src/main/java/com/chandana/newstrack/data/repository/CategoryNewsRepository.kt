package com.chandana.newstrack.data.repository

import com.chandana.newstrack.data.model.ApiSource
import com.chandana.newstrack.data.remote.NetworkService
import com.chandana.newstrack.di.ActivityScope
import com.chandana.newstrack.utils.AppConstant
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@ActivityScope
class CategoryNewsRepository @Inject constructor(private val networkService: NetworkService) {

    fun getCategories(): Flow<List<String>> {
        return flow {
            emit(AppConstant.CATEGORIES)
        }
    }

    fun getCategoryNews(category: String): Flow<List<ApiSource>> {
        return flow {
            emit(networkService.getCategoryBasedNews(category))
        }.map {
            it.sources
        }
    }
}