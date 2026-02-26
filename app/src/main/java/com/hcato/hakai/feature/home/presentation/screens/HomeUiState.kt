package com.hcato.hakai.feature.home.presentation.screens

data class HomeUiState(
    val title: String = "JUJUTSU KAISEN",
    val description: String = "JUJUTSU KAISEN es un manga con historia y dibujo de Gege Akutami...",
    val tags: String = "16+ Sub • Supernatural, Action, Drama, Fantasy, Shounen",
    val releaseInfo: String = "Nuevos episodios los jueves a las 10:00 AM",
    val isLoading: Boolean = false,
    val error: String? = null
)