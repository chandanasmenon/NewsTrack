package com.chandana.newstrack.di.component

import android.content.Context
import com.chandana.newstrack.NewsApplication
import com.chandana.newstrack.data.local.DatabaseService
import com.chandana.newstrack.data.remote.NetworkService
import com.chandana.newstrack.di.ApplicationContext
import com.chandana.newstrack.di.module.ApplicationModule
import com.chandana.newstrack.utils.DispatcherProvider
import com.chandana.newstrack.utils.NetworkHelper
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [ApplicationModule::class])
interface ApplicationComponent {

    fun inject(application: NewsApplication)

    @ApplicationContext
    fun getContext(): Context

    fun getNetworkService(): NetworkService

    fun getDispatcherProvider(): DispatcherProvider

    fun getNetworkHelper(): NetworkHelper

    fun getDatabaseService(): DatabaseService
}
