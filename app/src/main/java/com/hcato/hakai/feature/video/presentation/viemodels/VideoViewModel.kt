package com.hcato.hakai.feature.video.presentation.viemodels

import androidx.lifecycle.ViewModel
import com.hcato.hakai.BuildConfig
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

data class VideoUiState(
    val videoUrl: String = BuildConfig.BASE_URL_STREAMING,
    val isPlaying: Boolean = false,
    val isLoading: Boolean = true
)

@HiltViewModel
class VideoViewModel @Inject constructor() : ViewModel() {
    private val _state = MutableStateFlow(VideoUiState())
    val state = _state.asStateFlow()

    val streamUrl = BuildConfig.BASE_URL_STREAMING
}