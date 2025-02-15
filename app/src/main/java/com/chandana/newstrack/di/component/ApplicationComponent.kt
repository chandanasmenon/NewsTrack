package com.chandana.newstrack.di.component

import android.content.Context
import com.chandana.newstrack.NewsApplication
import com.chandana.newstrack.data.remote.NetworkService
import com.chandana.newstrack.di.ApplicationContext
import com.chandana.newstrack.di.module.ApplicationModule
import com.chandana.newstrack.utils.DispatcherProvider
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
}
