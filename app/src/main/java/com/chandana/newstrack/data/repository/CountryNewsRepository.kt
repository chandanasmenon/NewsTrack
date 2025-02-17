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
class CountryNewsRepository @Inject constructor(private val networkService: NetworkService) {
    fun getCountriesList(): Flow<List<Code>> {
        return flow {
            emit(AppConstant.COUNTRIES)
        }
    }

    fun getCountryNews(country: String): Flow<List<ApiSource>> {
        return flow {
            emit(networkService.getCountryBasedNews(country))
        }.map {
            it.sources
        }
    }
}