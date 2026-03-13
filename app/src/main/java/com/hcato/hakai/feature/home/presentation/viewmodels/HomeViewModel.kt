package com.hcato.hakai.feature.home.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hcato.hakai.core.repositories.SessionRepository
import com.hcato.hakai.core.repositories.UserRepository
import com.hcato.hakai.feature.home.presentation.screens.HomeUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val userRepository: UserRepository, // Usamos la interfaz del Core
    private val sessionRepository: SessionRepository // Usamos la interfaz del Core
) : ViewModel() {

    private val _state = MutableStateFlow(HomeUiState())
    val state = _state.asStateFlow()

    init {
        loadContent()
        loadUserProfile()
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

    private fun loadUserProfile() {
        viewModelScope.launch {
            val result = userRepository.getUserEmail()
            result.onSuccess { email ->
                _state.update { it.copy(userEmail = email) }
            }.onFailure {
                // Si la API dice que el token ya no sirve (ej. 401), cerramos sesión automáticamente
                logout()
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            // Borramos el token "muerto" del DataStore
            sessionRepository.clearSession()
            // Le avisamos a la pantalla que nos tenemos que ir
            _state.update { it.copy(isLoggedOut = true) }
        }
    }
}