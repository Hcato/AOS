package com.hcato.hakai.feature.video.presentation.screens

import com.hcato.hakai.BuildConfig

data class VideoUiState(
    val videoUrl: String = BuildConfig.BASE_URL_STREAMING,
    val isPlaying: Boolean = false,
    val isLoading: Boolean = true,
    val viewers: Int = 0,
    val totalLikes: Int = 0,
    val isLikeSending: Boolean = false,
    val hasLiked: Boolean = false,
    val viewpoint: com.hcato.hakai.core.hardware.ViewpointData = com.hcato.hakai.core.hardware.ViewpointData(),
    val fov: Float = 80f // Field of View (Zoom inicial)
)
