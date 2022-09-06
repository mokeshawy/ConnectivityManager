package com.example.internetaccess.feature.mainactivity.di

import com.example.internetaccess.services.InternetServices
import com.example.internetaccess.feature.mainactivity.data.repository.CorporateRepositoryImpl
import com.example.internetaccess.feature.mainactivity.domain.repository.CorporateRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
class CorporateModule {

    @Provides
    fun provideCorporateRepositoryImpl(internetServices: InternetServices): CorporateRepository =
        CorporateRepositoryImpl(internetServices)
}