package com.hcato.hakai.feature.video.data.di


import com.hcato.hakai.core.repositories.VideoRepositoryImpl
import com.hcato.hakai.feature.video.data.datasource.remote.api.VideoRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
abstract class VideoModule {

    @Binds
    @Singleton
    abstract fun bindVideoRepository(
        videoRepositoryImpl: VideoRepositoryImpl
    ): VideoRepository
}