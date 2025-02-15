package com.chandana.newstrack.data.remote

import com.chandana.newstrack.data.model.TopHeadlineSources
import retrofit2.http.GET
import javax.inject.Singleton

@Singleton
interface NetworkService {
    @GET("top-headlines/sources")
    suspend fun getTopHeadlineSources(): TopHeadlineSources
}
