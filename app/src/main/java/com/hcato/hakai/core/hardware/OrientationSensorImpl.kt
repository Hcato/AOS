package com.hcato.hakai.core.hardware

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import com.google.firebase.crashlytics.FirebaseCrashlytics
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject
import javax.inject.Singleton
import java.lang.Math.toDegrees

@Singleton
class OrientationSensorImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : OrientationSensor, SensorEventListener {

    private val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    // Usamos Game Rotation Vector porque ignora la brújula, evitando saltos bruscos si hay metal cerca
    private val rotationSensor = sensorManager.getDefaultSensor(Sensor.TYPE_GAME_ROTATION_VECTOR)

    // Usamos SharedFlow para emitir los eventos rápidamente sin saturar la memoria
    private val _viewpointFlow = MutableSharedFlow<ViewpointData>(
        replay = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    override val viewpointFlow = _viewpointFlow.asSharedFlow()

    private val rotationMatrix = FloatArray(9)
    private val orientationAngles = FloatArray(3)

    override fun startListening() {
        val crashlytics = FirebaseCrashlytics.getInstance()

        if (rotationSensor == null) {
            // Reportamos que este dispositivo no tiene el sensor necesario para el 360
            crashlytics.log("HW_ERROR: Sensor GAME_ROTATION_VECTOR no disponible")
            crashlytics.setCustomKey("sensor_360_disponible", false)
            return
        }

        val supported = sensorManager.registerListener(this, rotationSensor, SensorManager.SENSOR_DELAY_GAME)

        if (!supported) {
            crashlytics.log("HW_ERROR: Fallo al registrar listener de rotación")
            crashlytics.setCustomKey("sensor_registration_status", "failed")
        } else {
            crashlytics.setCustomKey("sensor_360_disponible", true)
        }
    }

    override fun stopListening() {
        sensorManager.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event?.sensor?.type == Sensor.TYPE_GAME_ROTATION_VECTOR) {
            // 1. Convertimos el vector a una matriz de rotación
            SensorManager.getRotationMatrixFromVector(rotationMatrix, event.values)

            // 2. Extraemos los ángulos en radianes
            SensorManager.getOrientation(rotationMatrix, orientationAngles)

            // 3. Convertimos a grados para VLC
            // VLC espera: Yaw (0 a 360), Pitch (-90 a 90), Roll (-180 a 180)
            var yaw = Math.toDegrees(orientationAngles[0].toDouble()).toFloat()
            if (yaw < 0) yaw += 360f // Normalizamos el Yaw de 0 a 360

            val pitch = Math.toDegrees(orientationAngles[1].toDouble()).toFloat()
            val roll = Math.toDegrees(orientationAngles[2].toDouble()).toFloat()

            // 4. Emitimos los datos
            _viewpointFlow.tryEmit(ViewpointData(yaw, pitch, roll))
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // No lo necesitamos para este caso
    }
}