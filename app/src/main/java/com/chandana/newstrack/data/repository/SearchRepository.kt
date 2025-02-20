package com.chandana.newstrack.data.repository

import com.chandana.newstrack.data.model.Article
import com.chandana.newstrack.data.remote.NetworkService
import com.chandana.newstrack.utils.AppConstant
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SearchRepository @Inject constructor(private val networkService: NetworkService) {

    fun getSearchResults(searchQuery: String): Flow<List<Article>> {
        return flow {
            emit(networkService.getSearchResults(query = searchQuery))
        }.map {
            it.articles
        }
    }

    fun getFilterSearchResults(q: String, language: String, sortBy: String): Flow<List<Article>> {
        return flow {
            emit(networkService.getFilterSearchResults(q = q, language = language, sortBy = sortBy))
        }.map {
            it.articles
        }
    }

    fun getFilterData(): Flow<List<String>> {
        return flow {
            emit(AppConstant.FILTERS)
        }
    }
}