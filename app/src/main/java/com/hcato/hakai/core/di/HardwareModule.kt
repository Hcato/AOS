package com.hcato.hakai.core.di

import com.hcato.hakai.core.hardware.OrientationSensor
import com.hcato.hakai.core.hardware.OrientationSensorImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class HardwareModule {

    @Binds
    @Singleton
    abstract fun bindOrientationSensor(
        impl: OrientationSensorImpl
    ): OrientationSensor
}