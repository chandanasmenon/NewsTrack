package com.chandana.newstrack

import android.app.Application
import com.chandana.newstrack.di.component.ApplicationComponent
import com.chandana.newstrack.di.component.DaggerApplicationComponent
import com.chandana.newstrack.di.module.ApplicationModule

class NewsApplication : Application() {
    lateinit var applicationComponent: ApplicationComponent

    override fun onCreate() {
        super.onCreate()
        injectDependencies()
    }

    private fun injectDependencies() {
        applicationComponent = DaggerApplicationComponent
            .builder()
            .applicationModule(ApplicationModule(this))
            .build()
    }
}