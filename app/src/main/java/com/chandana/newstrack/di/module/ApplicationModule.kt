package com.chandana.newstrack.di.module

import android.content.Context
import com.chandana.newstrack.NewsApplication
import com.chandana.newstrack.data.remote.NetworkService
import com.chandana.newstrack.di.ApplicationContext
import com.chandana.newstrack.di.BaseUrl
import com.chandana.newstrack.di.NetworkApiKey
import com.chandana.newstrack.di.NetworkUserAgent
import com.chandana.newstrack.utils.AppConstant
import com.chandana.newstrack.utils.HeaderInterceptor
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class ApplicationModule(private val application: NewsApplication) {

    @ApplicationContext
    @Provides
    fun provideContext(): Context {
        return application
    }

    @BaseUrl
    @Provides
    fun provideBaseUrl(): String = "https://newsapi.org/v2/"

    @Provides
    @NetworkApiKey
    fun provideApiKey(): String = AppConstant.API_KEY

    @Provides
    @NetworkUserAgent
    fun provideUserAgent(): String = AppConstant.USER_AGENT

    @Provides
    @Singleton
    fun provideGsonConverterFactory(): GsonConverterFactory = GsonConverterFactory.create()

    @Provides
    @Singleton
    fun provideHeaderInterceptor(
        @NetworkApiKey apiKey: String,
        @NetworkUserAgent userAgent: String
    ): HeaderInterceptor =
        HeaderInterceptor(apiKey, userAgent)

    @Provides
    @Singleton
    fun provideOkHttpClient(headerInterceptor: HeaderInterceptor): OkHttpClient {
        return OkHttpClient().newBuilder()
            .addInterceptor(headerInterceptor)
            .build()
    }

    @Provides
    @Singleton
    fun provideNetworkService(
        @BaseUrl baseUrl: String,
        gsonConverterFactory: GsonConverterFactory,
        okHttpClient: OkHttpClient
    ): NetworkService {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(gsonConverterFactory)
            .build()
            .create(NetworkService::class.java)
    }

}