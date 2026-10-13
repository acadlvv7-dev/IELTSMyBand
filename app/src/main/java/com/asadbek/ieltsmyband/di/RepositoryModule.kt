package com.asadbek.ieltsmyband.di

import com.asadbek.ieltsmyband.data.BandScoreRepositoryImpl
import com.asadbek.ieltsmyband.domain.repository.BandScoreRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent


@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindBandScoreRepository(
        impl: BandScoreRepositoryImpl
    ): BandScoreRepository
}