package com.hcato.hakai.feature.principal.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
@HiltViewModel
class PrincipalViewModel @Inject constructor(
    private val repository: PrincipalRepository,
    private val flashlightManager: FlashlightManager
) : ViewModel() {

    private val _state = MutableStateFlow(PrincipalUiState())
    val state = _state.asStateFlow()
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
}