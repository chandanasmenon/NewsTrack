package com.chandana.newstrack.di.module

import android.content.Context
import androidx.room.Room
import com.chandana.newstrack.data.local.AppDatabase
import com.chandana.newstrack.data.local.AppDatabaseService
import com.chandana.newstrack.data.local.DatabaseService
import com.chandana.newstrack.data.remote.NetworkService
import com.chandana.newstrack.di.BaseUrl
import com.chandana.newstrack.di.DatabaseName
import com.chandana.newstrack.di.NetworkApiKey
import com.chandana.newstrack.di.NetworkUserAgent
import com.chandana.newstrack.utils.AppConstant
import com.chandana.newstrack.utils.DefaultDispatcherProvider
import com.chandana.newstrack.utils.DefaultNetworkHelper
import com.chandana.newstrack.utils.DispatcherProvider
import com.chandana.newstrack.utils.HeaderInterceptor
import com.chandana.newstrack.utils.NetworkHelper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ApplicationModule {

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
    fun provideDispatcherProvider(): DispatcherProvider = DefaultDispatcherProvider()

    @Provides
    @Singleton
    fun provideNetworkHelper(@ApplicationContext context: Context): NetworkHelper =
        DefaultNetworkHelper(context)

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

    @DatabaseName
    @Provides
    fun provideDatabaseName(): String = "news-database"

    @Provides
    @Singleton
    fun provideAppDatabase(
        @ApplicationContext context: Context,
        @DatabaseName databaseName: String
    ): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            databaseName
        ).build()
    }

    @Provides
    @Singleton
    fun provideDatabaseService(appDatabase: AppDatabase): DatabaseService {
        return AppDatabaseService(appDatabase)
    }

}