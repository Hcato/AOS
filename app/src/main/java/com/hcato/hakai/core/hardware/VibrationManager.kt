package com.hcato.hakai.core.hardware

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

// 1. La Interfaz (El contrato para el ViewModel)
interface VibrationManager {
    fun playHeartbeat()
}

// 2. La Implementación (La lógica real de Android)
@Singleton
class VibrationManagerImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : VibrationManager {

    override fun playHeartbeat() {
        // Obtenemos el servicio de vibración dependiendo de la versión de Android
        val vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val manager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
            manager.defaultVibrator
        } else {
            @Suppress("DEPRECATION")
            context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        }

        // Si el teléfono no tiene motor de vibración (ej. algunas tablets), salimos
        if (!vibrator.hasVibrator()) return

        // El patrón del latido: Espera 0ms, Vibra 150ms, Espera 100ms, Vibra 150ms
        val timings = longArrayOf(0, 150, 100, 150)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // En Android 8.0+ podemos controlar la fuerza de la vibración (0 a 255)
            val amplitudes = intArrayOf(0, 255, 0, 255) // Máxima fuerza
            val effect = VibrationEffect.createWaveform(timings, amplitudes, -1)
            vibrator.vibrate(effect)
        } else {
            // Para Android más antiguos
            @Suppress("DEPRECATION")
            vibrator.vibrate(timings, -1)
        }
    }
}