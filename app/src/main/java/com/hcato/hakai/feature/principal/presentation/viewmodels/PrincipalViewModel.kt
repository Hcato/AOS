package com.hcato.hakai.feature.principal.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hcato.hakai.BuildConfig
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

@HiltViewModel
class PrincipalViewModel @Inject constructor() : ViewModel() {

    private val _state = MutableStateFlow(PrincipalUiState())
    val state = _state.asStateFlow()
    private val streamUrl = BuildConfig.BASE_URL_STREAMING

    fun toggleFavorite() {
        _state.update { it.copy(isFavorite = !it.isFavorite) }
    }
    private var pollingJob: Job? = null

    init {
        startStreamingCheck()
    }

    private fun startStreamingCheck() {
        // Cancelamos cualquier job previo por seguridad
        pollingJob?.cancel()

        pollingJob = viewModelScope.launch {
            // Este bucle se ejecutará mientras el ViewModel esté vivo
            while (isActive) {
                val isAvailable = checkUrl()

                _state.update { it.copy(isVideoAvailable = isAvailable) }

                // Si ya encontramos el directo, podríamos detener el polling
                // o seguir preguntando por si se cae.
                // Aquí esperamos 10 segundos antes de la siguiente consulta.
                delay(10000)
            }
        }
    }

    private suspend fun checkUrl(): Boolean = withContext(Dispatchers.IO) {
        try {
            val url = URL(streamUrl)
            val connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "HEAD"
            connection.connectTimeout = 3000
            connection.readTimeout = 3000

            val responseCode = connection.responseCode
            responseCode == HttpURLConnection.HTTP_OK
        } catch (e: Exception) {
            false
        }
    }

    override fun onCleared() {
        super.onCleared()
        pollingJob?.cancel() // Limpieza final
    }
}