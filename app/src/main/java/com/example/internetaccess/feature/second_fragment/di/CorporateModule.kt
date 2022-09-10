package com.example.internetaccess.feature.second_fragment.di

import com.example.internetaccess.services.InternetServices
import com.example.internetaccess.feature.second_fragment.data.repository.CorporateRepositoryImpl
import com.example.internetaccess.feature.second_fragment.domain.repository.CorporateRepository
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