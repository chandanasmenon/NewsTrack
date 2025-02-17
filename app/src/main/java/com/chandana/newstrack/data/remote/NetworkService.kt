package com.chandana.newstrack.data.remote

import com.chandana.newstrack.data.model.NewsSourcesResponse
import com.chandana.newstrack.data.model.TopHeadlineSources
import retrofit2.http.GET
import retrofit2.http.Query
import javax.inject.Singleton

@Singleton
interface NetworkService {
    @GET("top-headlines/sources")
    suspend fun getTopHeadlineSources(): TopHeadlineSources

    @GET("top-headlines/sources")
    suspend fun getTopHeadlinesSourcesPagination(
        @Query("page") page: Int,
        @Query("pageSize") pageSize: Int
    ): TopHeadlineSources

    @GET("top-headlines/sources")
    suspend fun getCategoryBasedNews(@Query("category") category: String): NewsSourcesResponse

    @GET("top-headlines/sources")
    suspend fun getCountryBasedNews(@Query("country") country: String): NewsSourcesResponse
}