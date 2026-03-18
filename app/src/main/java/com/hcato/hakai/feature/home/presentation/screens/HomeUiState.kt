package com.hcato.hakai.feature.home.presentation.screens

data class HomeUiState(
    val title: String = "HATSUNE MIKU: VIRTUAL LIVE 360",
    val description: String = "Sumérgete en una experiencia sensorial única con la idol virtual más famosa del mundo. Disfruta de un concierto inmersivo donde el escenario cobra vida gracias al giroscopio de tu dispositivo y la vibración rítmica.",
    val tags: String = "Live • 360° Inmersive • Vocaloid • J-Pop • Electronic",
    val releaseInfo: String = "Próximo show: Miércoles a las 1:00 PM (Hora Local)",
    val isLoading: Boolean = false,
    val error: String? = null,
    val userEmail: String = "Cargando...",
    val isLoggedOut: Boolean = false
)