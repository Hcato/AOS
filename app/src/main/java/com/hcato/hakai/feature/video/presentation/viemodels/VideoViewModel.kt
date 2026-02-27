package com.hcato.hakai.feature.video.presentation.viemodels

import android.content.Context
import android.provider.Settings
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hcato.hakai.BuildConfig
import com.hcato.hakai.feature.principal.data.datasource.remote.api.StreamingApi
import com.hcato.hakai.feature.principal.data.model.LikeRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import io.socket.client.Socket
import jakarta.inject.Inject
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.json.JSONObject

data class VideoUiState(
    val videoUrl: String = BuildConfig.BASE_URL_STREAMING,
    val isPlaying: Boolean = false,
    val isLoading: Boolean = true,
    val viewers: Int = 0,
    val totalLikes: Int = 0,
    val isLikeSending: Boolean = false,
    val hasLiked: Boolean = false
)

@HiltViewModel
class VideoViewModel @Inject constructor(
    private val socket: Socket,
    private val apiService: StreamingApi,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _state = MutableStateFlow(VideoUiState())
    val state = _state.asStateFlow()

    private val androidId = Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)

    init {
        setupSocketListeners()
    }

    private fun setupSocketListeners() {
        socket.connect()

        socket.on("update_viewers") { args ->
            val count = args[0].toString().toDouble().toInt()
            _state.update { it.copy(viewers = count) }
        }

        socket.on("new_like") { args ->
            val total = args[0].toString().toDouble().toInt()
            _state.update { it.copy(totalLikes = total) }
        }
    }

    fun joinStream(videoId: String) {
        viewModelScope.launch {
            // Esperar conexión
            while (!socket.connected()) { delay(500) }

            // Enviar datos que espera tu servidor
            val data = JSONObject().apply {
                put("videoId", videoId)
                put("androidId", androidId)
            }
            socket.emit("join_estreno", data)

            // Cargar likes iniciales
            try {
                val res = apiService.getLikes(videoId)
                _state.update { it.copy(totalLikes = res.totalLikes) }
            } catch (e: Exception) { /* log error */ }
        }
    }

    fun sendLike(videoId: String) {
        // Si ya está enviando o ya dio like, ignoramos el clic
        if (_state.value.isLikeSending || _state.value.hasLiked) return

        viewModelScope.launch {
            _state.update { it.copy(isLikeSending = true) }
            try {
                val response = apiService.darLike(videoId, LikeRequest(androidId))

                // Si el servidor responde con éxito (200 OK)
                if (response.success) {
                    _state.update { it.copy(hasLiked = true) }
                }
            } catch (e: Exception) {
                // Si el servidor responde 400 (ya dio like), Retrofit cae aquí.
                // Aun así, bloqueamos el botón para que el usuario no siga intentando.
                _state.update { it.copy(hasLiked = true) }
                println("HAKAI_LOG: El usuario ya había dado like o hubo error: ${e.message}")
            } finally {
                _state.update { it.copy(isLikeSending = false) }
            }
        }
    }

    override fun onCleared() {
        socket.off()
        socket.disconnect()
        super.onCleared()
    }
    fun onVideoPlaying() {
        _state.update {
            it.copy(
                isLoading = false,
                isPlaying = true
            )
        }
    }
}