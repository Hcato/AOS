package com.hcato.hakai.core.hardware

import android.content.Context
import android.hardware.camera2.CameraManager
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

interface FlashlightManager {
    suspend fun blinkFlash(times: Int = 3, delayMs: Long = 300)
}

@Singleton
class FlashlightManagerImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : FlashlightManager {

    override suspend fun blinkFlash(times: Int, delayMs: Long) {
        val cameraManager = context.getSystemService(Context.CAMERA_SERVICE) as CameraManager

        try {
            // Buscamos la primera cámara que tenga flash (usualmente la trasera, id "0")
            val cameraId = cameraManager.cameraIdList.firstOrNull { id ->
                cameraManager.getCameraCharacteristics(id)
                    .get(android.hardware.camera2.CameraCharacteristics.FLASH_INFO_AVAILABLE) == true
            } ?: return // Si no hay flash (ej. algunas tablets), salimos

            // Hacemos el parpadeo en un hilo de fondo para no trabar la app
            withContext(Dispatchers.IO) {
                repeat(times) {
                    cameraManager.setTorchMode(cameraId, true) // Enciende
                    delay(delayMs)
                    cameraManager.setTorchMode(cameraId, false) // Apaga
                    delay(delayMs)
                }
            }
        } catch (e: Exception) {
            // Ignoramos silenciosamente si otra app tiene bloqueada la cámara
            e.printStackTrace()
        }
    }
}