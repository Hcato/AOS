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
                    description = "Sumérgete en una experiencia sensorial única con la idol virtual más famosa del mundo. Disfruta de un concierto inmersivo donde el escenario cobra vida gracias al giroscopio de tu dispositivo y la vibración rítmica.",
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