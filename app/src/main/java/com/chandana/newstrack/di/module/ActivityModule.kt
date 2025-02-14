package com.chandana.newstrack.di.module

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import com.chandana.newstrack.di.ActivityContext
import dagger.Module
import dagger.Provides

@Module
class ActivityModule(private val activity: AppCompatActivity) {

    @ActivityContext
    @Provides
    fun provideContext(): Context {
        return activity
    }
}