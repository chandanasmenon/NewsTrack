package com.chandana.newstrack.di.component

import com.chandana.newstrack.di.ActivityScope
import com.chandana.newstrack.di.module.ActivityModule
import com.chandana.newstrack.ui.pagingtopheadlinesources.TopHeadlinesPagingActivity
import com.chandana.newstrack.ui.topheadlinesources.TopHeadlineSourcesActivity
import dagger.Component

@ActivityScope
@Component(dependencies = [ApplicationComponent::class], modules = [ActivityModule::class])
interface ActivityComponent {
    fun inject(activity: TopHeadlineSourcesActivity)
    fun inject(activity: TopHeadlinesPagingActivity)
}