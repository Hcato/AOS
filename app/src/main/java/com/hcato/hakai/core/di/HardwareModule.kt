package com.hcato.hakai.core.di

import com.hcato.hakai.core.hardware.FlashlightManager
import com.hcato.hakai.core.hardware.FlashlightManagerImpl
import com.hcato.hakai.core.hardware.OrientationSensor
import com.hcato.hakai.core.hardware.OrientationSensorImpl
import com.hcato.hakai.core.hardware.VibrationManager
import com.hcato.hakai.core.hardware.VibrationManagerImpl
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

    @Binds
    @Singleton
    abstract fun bindVibrationManager(
        impl: VibrationManagerImpl
    ): VibrationManager

    @Binds
    @Singleton
    abstract fun bindFlashlightManager(
        impl: FlashlightManagerImpl
    ): FlashlightManager
}