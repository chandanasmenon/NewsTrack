package com.chandana.newstrack.di.module

import com.chandana.newstrack.ui.categorynews.CategoryNewsAdapter
import com.chandana.newstrack.ui.countrynews.CountryNewsAdapter
import com.chandana.newstrack.ui.languagenews.LanguageNewsAdapter
import com.chandana.newstrack.ui.offlinetopheadlines.OfflineTopHeadlinesAdapter
import com.chandana.newstrack.ui.pagingtopheadlinesources.TopHeadlinesPagingAdapter
import com.chandana.newstrack.ui.searchnews.FilterDataAdapter
import com.chandana.newstrack.ui.searchnews.SearchNewsAdapter
import com.chandana.newstrack.ui.topheadlinesources.TopHeadlineSourcesAdapter
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.scopes.ActivityScoped

@Module
@InstallIn(ActivityComponent::class)
class ActivityModule {

    @ActivityScoped
    @Provides
    fun provideTopHeadlineSourcesAdapter() = TopHeadlineSourcesAdapter(ArrayList())

    @ActivityScoped
    @Provides
    fun provideTopHeadlinesPagingAdapter() = TopHeadlinesPagingAdapter()

    @ActivityScoped
    @Provides
    fun provideOfflineTopHeadlinesAdapter() = OfflineTopHeadlinesAdapter(ArrayList())

    @ActivityScoped
    @Provides
    fun provideCategoryNewsAdapter() = CategoryNewsAdapter(ArrayList())

    @ActivityScoped
    @Provides
    fun provideCountryNewsAdapter() = CountryNewsAdapter(ArrayList())

    @ActivityScoped
    @Provides
    fun provideLanguageNewsAdapter() = LanguageNewsAdapter(ArrayList())

    @ActivityScoped
    @Provides
    fun provideFilterDataAdapter() = FilterDataAdapter(ArrayList())

    @ActivityScoped
    @Provides
    fun provideSearchNewsAdapter() = SearchNewsAdapter(ArrayList())

}