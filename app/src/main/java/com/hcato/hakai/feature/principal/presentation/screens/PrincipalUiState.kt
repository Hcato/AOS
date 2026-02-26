package com.hcato.hakai.feature.principal.presentation.screens

import com.hcato.hakai.feature.home.presentation.screens.HomeUiState

data class PrincipalUiState(
    val baseInfo: HomeUiState = HomeUiState(),
    val rating: Double = 4.9,
    val reviewsCount: String = "707K",
    val nextEpisodeDate: String = "26/2",
    val isFavorite: Boolean = false,
    val isVideoAvailable: Boolean = false,
    val fullDescription: String = "JUJUTSU KAISEN es un manga con historia y dibujo de Gege Akutami que se publica en la Weekly Shonen Jump. Poco después de su debut se estrenaba la adaptación animada, producida por Studio MAPPA. Actualmente hay varias temporadas del anime, comenzando con la primera (24 episodios), siguiéndole la aclamada película precuela JUJUTSU KAISEN 0, y posteriormente la segunda temporada en julio de 2023. La historia sigue a Yuji Itadori, un estudiante con una fuerza física increíble que termina consumiendo una maldición milenaria para salvar a sus amigos."
)