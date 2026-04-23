package com.hcato.hakai.feature.principal.presentation.viewmodels

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.google.firebase.Firebase
import com.google.firebase.messaging.messaging
import com.hcato.hakai.BuildConfig
import com.hcato.hakai.feature.principal.data.datasource.remote.api.PrincipalRepository
import com.hcato.hakai.feature.principal.presentation.screens.PrincipalUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.videolan.libvlc.interfaces.IMedia.Meta.URL
import java.net.HttpURLConnection
import java.net.URL
import com.hcato.hakai.core.hardware.FlashlightManager
import com.hcato.hakai.core.network.interceptors.ReminderWorker
import com.hcato.hakai.feature.principal.data.datasource.local.ReminderDao
import com.hcato.hakai.feature.principal.data.datasource.local.ReminderEntity
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.concurrent.TimeUnit

@HiltViewModel
class PrincipalViewModel @Inject constructor(
    private val repository: PrincipalRepository,
    private val flashlightManager: FlashlightManager,
    private val reminderDao: ReminderDao, // <-- INYECTAMOS EL DAO
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _state = MutableStateFlow(PrincipalUiState())
    val state = _state.asStateFlow()

    fun setReminder(videoId: String, title: String) {
        viewModelScope.launch {
            // 1. Guardamos en Room (Base de datos local)
            val timeToRing = System.currentTimeMillis() + 15000 // 15 segundos en el futuro (DEMO)
            reminderDao.insertReminder(ReminderEntity(videoId, title, timeToRing))

            // Actualizamos la UI
            _state.update { it.copy(isFavorite = true) } // Usamos isFavorite como flag temporal de "Recordatorio activo"

            // 2. Programamos el WorkManager
            val inputData = Data.Builder().putString("TITLE", title).build()

            val workRequest = OneTimeWorkRequestBuilder<ReminderWorker>()
                .setInitialDelay(15, TimeUnit.SECONDS) // ¡ESPERA 15 SEGUNDOS!
                .setInputData(inputData)
                .build()

            WorkManager.getInstance(context).enqueue(workRequest)
        }
    }

    private val streamUrl = BuildConfig.BASE_URL_STREAMING

    fun toggleFavorite() {
        _state.update { it.copy(isFavorite = !it.isFavorite) }
    }
    private var pollingJob: Job? = null

    private var wasVideoAvailable = false

    init {
        startStreamingCheck()
    }

    private fun startStreamingCheck() {
        pollingJob?.cancel()
        pollingJob = viewModelScope.launch {
            while (isActive) {
                val isAvailable = repository.isStreamAvailable()

                // SI EL VIDEO NO ESTABA DISPONIBLE Y AHORA SÍ LO ESTÁ...
                if (!wasVideoAvailable && isAvailable) {
                    // ¡Hacemos parpadear el flash 3 veces!
                    flashlightManager.blinkFlash(times = 3, delayMs = 250)
                }

                // Guardamos el estado para la próxima vuelta
                wasVideoAvailable = isAvailable

                _state.update { it.copy(isVideoAvailable = isAvailable) }
                delay(10000)
            }
        }
    }


    override fun onCleared() {
        super.onCleared()
        pollingJob?.cancel() // Limpieza final
    }

    init {
        // 1. Suscribirse al tema de Firebase para notificaciones push
        subscribeToLiveNotifications()

        // 2. Iniciar el chequeo de streaming que ya tenías
        startStreamingCheck()
    }

    private fun subscribeToLiveNotifications() {
        Firebase.messaging.subscribeToTopic("estrenos_en_vivo")
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("HAKAI_FCM", "Suscrito con éxito al tema: estrenos_en_vivo")
                } else {
                    Log.e("HAKAI_FCM", "Error al suscribirse", task.exception)
                }
            }
    }
}