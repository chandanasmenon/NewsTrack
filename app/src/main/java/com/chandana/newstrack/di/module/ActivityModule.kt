package com.chandana.newstrack.di.module

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.chandana.newstrack.data.repository.CategoryNewsRepository
import com.chandana.newstrack.data.repository.CountryNewsRepository
import com.chandana.newstrack.data.repository.LanguageNewsRepository
import com.chandana.newstrack.data.repository.topheadlinesources.OfflineTopHeadlinesRepository
import com.chandana.newstrack.data.repository.topheadlinesources.TopHeadlinePagingRepository
import com.chandana.newstrack.data.repository.topheadlinesources.TopHeadlineSourcesRepository
import com.chandana.newstrack.di.ActivityContext
import com.chandana.newstrack.ui.base.ViewModelProviderFactory
import com.chandana.newstrack.ui.categorynews.CategoryNewsAdapter
import com.chandana.newstrack.ui.categorynews.CategoryNewsViewModel
import com.chandana.newstrack.ui.countrynews.CountryNewsAdapter
import com.chandana.newstrack.ui.countrynews.CountryNewsViewModel
import com.chandana.newstrack.ui.languagenews.LanguageNewsAdapter
import com.chandana.newstrack.ui.languagenews.LanguageNewsViewModel
import com.chandana.newstrack.ui.offlinetopheadlines.OfflineTopHeadlinesAdapter
import com.chandana.newstrack.ui.offlinetopheadlines.OfflineTopHeadlinesViewModel
import com.chandana.newstrack.ui.pagingtopheadlinesources.TopHeadlinesPagingAdapter
import com.chandana.newstrack.ui.pagingtopheadlinesources.TopHeadlinesPagingViewModel
import com.chandana.newstrack.ui.topheadlinesources.TopHeadlineSourcesAdapter
import com.chandana.newstrack.ui.topheadlinesources.TopHeadlineViewModel
import com.chandana.newstrack.utils.DispatcherProvider
import com.chandana.newstrack.utils.NetworkHelper
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
    fun provideTopHeadlinesPagingViewModel(
        repository: TopHeadlinePagingRepository,
        dispatcherProvider: DispatcherProvider
    ): TopHeadlinesPagingViewModel {
        return ViewModelProvider(
            activity,
            ViewModelProviderFactory(TopHeadlinesPagingViewModel::class) {
                TopHeadlinesPagingViewModel(repository, dispatcherProvider)
            })[TopHeadlinesPagingViewModel::class.java]
    }

    @Provides
    fun provideOfflineTopHeadlinesViewModel(
        networkHelper: NetworkHelper,
        dispatcherProvider: DispatcherProvider,
        repository: OfflineTopHeadlinesRepository
    ): OfflineTopHeadlinesViewModel {
        return ViewModelProvider(
            activity,
            ViewModelProviderFactory(OfflineTopHeadlinesViewModel::class) {
                OfflineTopHeadlinesViewModel(
                    networkHelper = networkHelper,
                    dispatcherProvider = dispatcherProvider,
                    offlineTopHeadlinesRepository = repository
                )
            })[OfflineTopHeadlinesViewModel::class.java]
    }

    @Provides
    fun provideCategoryNewsViewModel(
        repository: CategoryNewsRepository,
        dispatcherProvider: DispatcherProvider
    ): CategoryNewsViewModel {
        return ViewModelProvider(activity, ViewModelProviderFactory(CategoryNewsViewModel::class) {
            CategoryNewsViewModel(repository = repository, dispatcherProvider = dispatcherProvider)
        })[CategoryNewsViewModel::class.java]
    }

    @Provides
    fun provideCountryNewsViewModel(
        repository: CountryNewsRepository,
        dispatcherProvider: DispatcherProvider
    ): CountryNewsViewModel {
        return ViewModelProvider(
            activity, ViewModelProviderFactory(CountryNewsViewModel::class) {
                CountryNewsViewModel(
                    repository = repository,
                    dispatcherProvider = dispatcherProvider
                )
            }
        )[CountryNewsViewModel::class.java]
    }

    @Provides
    fun provideLanguageNewsViewModel(
        repository: LanguageNewsRepository,
        dispatcherProvider: DispatcherProvider
    ): LanguageNewsViewModel {
        return ViewModelProvider(activity, ViewModelProviderFactory(LanguageNewsViewModel::class) {
            LanguageNewsViewModel(repository = repository, dispatcherProvider = dispatcherProvider)
        })[LanguageNewsViewModel::class.java]
    }

    @Provides
    fun provideTopHeadlineSourcesAdapter() = TopHeadlineSourcesAdapter(ArrayList())

    @Provides
    fun provideTopHeadlinesPagingAdapter() = TopHeadlinesPagingAdapter()

    @Provides
    fun provideOfflineTopHeadlinesAdapter() = OfflineTopHeadlinesAdapter(ArrayList())

    @Provides
    fun provideCategoryNewsAdapter() = CategoryNewsAdapter(ArrayList())

    @Provides
    fun provideCountryNewsAdapter() = CountryNewsAdapter(ArrayList())

    @Provides
    fun provideLanguageNewsAdapter() = LanguageNewsAdapter(ArrayList())

}