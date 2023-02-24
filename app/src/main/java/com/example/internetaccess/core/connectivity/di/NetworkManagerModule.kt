package com.example.internetaccess.core.connectivity.di

import android.app.Activity
import com.example.internetaccess.core.connectivity.connectivity_manager.ConnectivityManager
import com.example.internetaccess.core.connectivity.internet_access_observer.InternetAccessObserver
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@Module
@InstallIn(ActivityComponent::class)
class NetworkManagerModule {

    @Provides
    fun provideNetworkManager(activity: Activity, internetAccessObserver: InternetAccessObserver) =
        ConnectivityManager(activity, internetAccessObserver)

    @Provides
    fun provideInternetAccessObserver(activity: Activity) = InternetAccessObserver(activity)
}