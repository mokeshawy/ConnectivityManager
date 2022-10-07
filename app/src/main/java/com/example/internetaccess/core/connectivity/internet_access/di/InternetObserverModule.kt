package com.example.internetaccess.core.connectivity.internet_access.di

import android.app.Activity
import com.example.internetaccess.core.connectivity.internet_access.internet_access_observer.InternetAccessObserver
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@Module
@InstallIn(ActivityComponent::class)
class InternetObserverModule {

    @Provides
    fun provideInternetAccessObserver(activity: Activity) = InternetAccessObserver(activity)
}