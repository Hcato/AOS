package com.hcato.hakai.feature.principal.presentation.screens

import com.hcato.hakai.feature.home.presentation.screens.HomeUiState

data class PrincipalUiState(
    val baseInfo: HomeUiState = HomeUiState(),
    val rating: Double = 4.9,
    val reviewsCount: String = "707K",
    val nextEpisodeDate: String = "18/3",
    val isFavorite: Boolean = false,
    val isVideoAvailable: Boolean = false,
    val fullDescription: String = "Miku Hatsune conocida mayormente como Hatsune Miku o simplemente Miku, es una cantante virtual para el sintetizador desarrollado por Yamaha, VOCALOID2, VOCALOID3 y VOCALOID4, así como para el programa desarrollado por Crypton Future Media, Piapro Studio. Su imagen, de una chica de antropomorfismo moe de 16 años, llega a ser considerada y personificada como una de las más famosas idol virtuales japonesas a nivel mundial."
)