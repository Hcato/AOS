package com.hcato.hakai.feature.principal.data.di

import com.hcato.hakai.core.repositories.PrincipalRepositoryImpl
import com.hcato.hakai.feature.principal.data.datasource.remote.api.PrincipalRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindPrincipalRepository(
        principalRepositoryImpl: PrincipalRepositoryImpl
    ): PrincipalRepository
}