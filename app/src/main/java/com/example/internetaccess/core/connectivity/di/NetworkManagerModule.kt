package com.example.internetaccess.core.connectivity.di

import android.app.Activity
import com.example.internetaccess.core.connectivity.connectivity_manager.NetworkManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@Module
@InstallIn(ActivityComponent::class)
class NetworkManagerModule {

    @Provides
    fun provideNetworkManager(activity: Activity) = NetworkManager(activity)
}