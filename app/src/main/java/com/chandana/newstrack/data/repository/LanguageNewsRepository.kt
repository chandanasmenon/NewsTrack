package com.chandana.newstrack.data.repository

import com.chandana.newstrack.data.model.ApiSource
import com.chandana.newstrack.data.model.Code
import com.chandana.newstrack.data.remote.NetworkService
import com.chandana.newstrack.di.ActivityScope
import com.chandana.newstrack.utils.AppConstant
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@ActivityScope
class LanguageNewsRepository @Inject constructor(private val networkService: NetworkService) {
    fun getLanguageList(): Flow<List<Code>> {
        return flow {
            emit(AppConstant.LANGUAGES)
        }
    }

    fun getLanguageBasedNews(language: String): Flow<List<ApiSource>> {
        return flow {
            emit(networkService.getLanguageBasedNews(language))
        }.map { it.sources }
    }
}