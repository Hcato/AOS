package com.hcato.hakai.core.di

import com.hcato.hakai.core.repositories.SessionRepository
import com.hcato.hakai.core.repositories.UserRepository
import com.hcato.hakai.feature.login.data.repositories.SessionRepositoryImpl
import com.hcato.hakai.feature.login.data.repositories.UserRepositoryImpl

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindUserRepository(
        userRepositoryImpl: UserRepositoryImpl // <-- AQUÍ debe ir la clase Impl, no la interfaz
    ): UserRepository
}