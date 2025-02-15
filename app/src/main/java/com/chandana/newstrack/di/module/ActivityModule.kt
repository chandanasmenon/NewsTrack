package com.chandana.newstrack.di.module

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.chandana.newstrack.data.repository.TopHeadlineSourcesRepository
import com.chandana.newstrack.di.ActivityContext
import com.chandana.newstrack.ui.base.ViewModelProviderFactory
import com.chandana.newstrack.ui.topheadlinesources.TopHeadlineSourcesAdapter
import com.chandana.newstrack.ui.topheadlinesources.TopHeadlineViewModel
import com.chandana.newstrack.utils.DispatcherProvider
import dagger.Module
import dagger.Provides

@Module
class ActivityModule(private val activity: AppCompatActivity) {

    @ActivityContext
    @Provides
    fun provideContext(): Context {
        return activity
    }

    @Provides
    fun provideTopHeadlineViewModel(
        repository: TopHeadlineSourcesRepository,
        dispatcherProvider: DispatcherProvider
    ): TopHeadlineViewModel {
        return ViewModelProvider(activity, ViewModelProviderFactory(TopHeadlineViewModel::class) {
            TopHeadlineViewModel(repository, dispatcherProvider)
        })[TopHeadlineViewModel::class.java]
    }

    @Provides
    fun provideTopHeadlineSourcesAdapter() = TopHeadlineSourcesAdapter(ArrayList())
}