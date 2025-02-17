package com.chandana.newstrack.data.repository.topheadlinesources

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.chandana.newstrack.data.model.ApiSource
import com.chandana.newstrack.data.remote.NetworkService
import com.chandana.newstrack.utils.AppConstant.INITIAL_PAGE
import com.chandana.newstrack.utils.AppConstant.PAGE_SIZE

class TopHeadlinePagingSource(private val networkService: NetworkService) :
    PagingSource<Int, ApiSource>() {
    override fun getRefreshKey(state: PagingState<Int, ApiSource>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ApiSource> {
        return try {
            val page = params.key ?: INITIAL_PAGE
            val response = networkService.getTopHeadlinesSourcesPagination(
                page = page,
                pageSize = PAGE_SIZE
            )
            LoadResult.Page(
                data = response.sources,
                prevKey = if (page == INITIAL_PAGE) null else page.minus(1),
                nextKey = if (response.sources.isEmpty()) null else page.plus(1),
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

}