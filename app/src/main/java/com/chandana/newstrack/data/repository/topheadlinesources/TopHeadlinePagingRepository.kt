package com.chandana.newstrack.data.repository.topheadlinesources

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.chandana.newstrack.data.model.ApiSource
import com.chandana.newstrack.data.remote.NetworkService
import com.chandana.newstrack.utils.AppConstant.PAGE_SIZE
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TopHeadlinePagingRepository @Inject constructor(private val networkService: NetworkService) {

    fun getTopHeadlines(): Flow<PagingData<ApiSource>> {
        return Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE
            ),
            pagingSourceFactory = {
                TopHeadlinePagingSource(networkService)
            }
        ).flow
    }

}