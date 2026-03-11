package com.hcato.hakai.feature.login.di

import com.hcato.hakai.feature.login.data.datasource.remote.api.AuthApi
import com.hcato.hakai.feature.login.data.repositories.AuthRepositoryImpl
import com.hcato.hakai.feature.login.domain.repositories.LoginRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LoginModule {

    @Provides
    @Singleton
    fun provideAuthRepository(api: AuthApi): LoginRepository =
        AuthRepositoryImpl(api)
}