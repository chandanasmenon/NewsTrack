package com.chandana.newstrack.di.component

import com.chandana.newstrack.di.ActivityScope
import com.chandana.newstrack.di.module.ActivityModule
import dagger.Component

@ActivityScope
@Component(dependencies = [ApplicationComponent::class], modules = [ActivityModule::class])
interface ActivityComponent {
}