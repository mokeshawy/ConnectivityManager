package com.example.internetaccess.core.connectivity.internet_access.di

import android.app.Activity
import com.example.internetaccess.core.connectivity.internet_access.internet_access_manager.InternetAccessManager
import com.example.internetaccess.core.connectivity.internet_access.internet_access_observer.InternetAccessObserver
import com.example.internetaccess.core.connectivity.internet_access.internet_access_observer.InternetAccessObserverImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@Module
@InstallIn(ActivityComponent::class)
class InternetAccessModule {

    @Provides
    fun internetAccessManager(activity: Activity) = InternetAccessManager(activity)

    @Provides
    fun provideInternetAccessObserve() : InternetAccessObserver = InternetAccessObserverImpl()
}