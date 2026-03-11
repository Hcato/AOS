package com.hcato.hakai.feature.home.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hcato.hakai.feature.home.presentation.screens.HomeUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class HomeViewModel @Inject constructor(
    // Aquí irían tus UseCases si los necesitas para esta pantalla
) : ViewModel() {

    private val _state = MutableStateFlow(HomeUiState())
    val state = _state.asStateFlow()

    init {
        loadContent()
    }

    private fun loadContent() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            // Simulación de carga de datos
            _state.update {
                it.copy(
                    description = "JUJUTSU KAISEN es un manga con historia y dibujo de Gege Akutami que se publica en la Weekly Shonen Jump. Poco después de su debut se estrenaba la adaptación animada, producida por Studio MAPPA. Actualmente hay varias temporadas del anime, comenzando con la primera (24 episodios), siguiéndole la aclamada película precuela JUJUTSU KAISEN 0, y posteriormente la segunda temporada en julio de 2023.",
                    isLoading = false
                )
            }
        }
    }
}