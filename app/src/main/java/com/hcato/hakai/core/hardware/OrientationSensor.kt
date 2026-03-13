package com.hcato.hakai.core.hardware

import kotlinx.coroutines.flow.Flow

data class ViewpointData(
    val yaw: Float = 0f,   // Eje X (Izquierda / Derecha)
    val pitch: Float = 0f, // Eje Y (Arriba / Abajo)
    val roll: Float = 0f   // Eje Z (Inclinación del cuello)
)

interface OrientationSensor {
    val viewpointFlow: Flow<ViewpointData>
    fun startListening()
    fun stopListening()
}