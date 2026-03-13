package com.hcato.hakai.feature.video.presentation.viemodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hcato.hakai.core.hardware.OrientationSensor
import com.hcato.hakai.feature.video.data.datasource.remote.api.VideoRepository
import com.hcato.hakai.feature.video.presentation.screens.VideoUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import javax.inject.Named
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class VideoViewModel @Inject constructor(
    private val repository: VideoRepository,
    private val orientationSensor: OrientationSensor,
    @Named("androidId") private val androidId: String // Inyectado desde un módulo
) : ViewModel() {

    private val _state = MutableStateFlow(VideoUiState())
    val state = _state.asStateFlow()

    init {
        repository.connect()
        observeData()
    }

    private fun observeData() {
        viewModelScope.launch {
            // Escuchamos los flujos de datos que vienen del repositorio
            launch { repository.viewersFlow.collect { count -> _state.update { it.copy(viewers = count) } } }
            launch { repository.likesFlow.collect { total -> _state.update { it.copy(totalLikes = total) } } }

            // CORRECCIÓN: Este launch ahora está dentro del viewModelScope principal
            launch {
                orientationSensor.viewpointFlow.collect { newViewpoint ->
                    _state.update { it.copy(viewpoint = newViewpoint) }
                }
            }
        }
    }

    // CORRECCIÓN: Agregamos las funciones faltantes para controlar el giroscopio
    fun startSensors() {
        orientationSensor.startListening()
    }

    fun stopSensors() {
        orientationSensor.stopListening()
    }

    fun joinStream(videoId: String) {
        viewModelScope.launch {
            repository.joinStream(videoId, androidId)
            val initialLikes = repository.getInitialLikes(videoId)
            _state.update { it.copy(totalLikes = initialLikes) }
        }
    }

    fun sendLike(videoId: String) {
        if (_state.value.isLikeSending || _state.value.hasLiked) return
        viewModelScope.launch {
            _state.update { it.copy(isLikeSending = true) }
            val success = repository.sendLike(videoId, androidId)
            _state.update { it.copy(hasLiked = success, isLikeSending = false) }
        }
    }

    override fun onCleared() {
        repository.disconnect()
        stopSensors() // Ahora esta función sí existe y no dará error
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