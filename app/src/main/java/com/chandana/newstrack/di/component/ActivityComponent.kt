package com.chandana.newstrack.di.component

import com.chandana.newstrack.di.ActivityScope
import com.chandana.newstrack.di.module.ActivityModule
import com.chandana.newstrack.ui.categorynews.CategoryDisplayActivity
import com.chandana.newstrack.ui.categorynews.CategoryNewsActivity
import com.chandana.newstrack.ui.countrynews.CountryDisplayActivity
import com.chandana.newstrack.ui.countrynews.CountryNewsActivity
import com.chandana.newstrack.ui.offlinetopheadlines.OfflineTopHeadlinesActivity
import com.chandana.newstrack.ui.pagingtopheadlinesources.TopHeadlinesPagingActivity
import com.chandana.newstrack.ui.topheadlinesources.TopHeadlineSourcesActivity
import dagger.Component

@ActivityScope
@Component(dependencies = [ApplicationComponent::class], modules = [ActivityModule::class])
interface ActivityComponent {
    fun inject(activity: TopHeadlineSourcesActivity)
    fun inject(activity: TopHeadlinesPagingActivity)
    fun inject(activity: OfflineTopHeadlinesActivity)
    fun inject(activity: CategoryNewsActivity)
    fun inject(activity: CategoryDisplayActivity)
    fun inject(activity: CountryDisplayActivity)
    fun inject(activity: CountryNewsActivity)
}