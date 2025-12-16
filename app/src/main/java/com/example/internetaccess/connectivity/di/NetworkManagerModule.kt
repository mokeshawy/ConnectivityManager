package com.example.internetaccess.connectivity.di

import com.example.internetaccess.connectivity.connectivity_manager.ConnectivityHelper
import com.example.internetaccess.connectivity.connectivity_manager.ConnectivityManager
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class NetworkManagerModule {

    @Binds
    @Singleton
    abstract fun provideNetworkManager(connectivityManager: ConnectivityManager): ConnectivityHelper
}