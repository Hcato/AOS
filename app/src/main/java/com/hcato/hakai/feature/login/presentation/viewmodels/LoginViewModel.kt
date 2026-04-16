package com.hcato.hakai.feature.login.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hcato.hakai.core.repositories.SessionRepository
import com.hcato.hakai.feature.login.domain.usecases.LoginUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val sessionRepository: SessionRepository,
    private val firebaseAuth: com.google.firebase.auth.FirebaseAuth
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState = _uiState.asStateFlow()

    init {
        // Auto-login: Si ya hay un usuario en Firebase, saltamos el login
        if (firebaseAuth.currentUser != null) {
            _uiState.update { it.copy(isSuccess = true) }
        }
    }
    fun onEmailChanged(email: String) {
        _uiState.update { it.copy(email = email, errorMessage = null) }
    }

    fun onPasswordChanged(password: String) {
        _uiState.update { it.copy(password = password, errorMessage = null) }
    }

    fun login() {
        val currentState = _uiState.value
        if (currentState.email.isBlank() || currentState.password.isBlank()) {
            _uiState.update { it.copy(errorMessage = "Por favor, llena todos los campos") }
            return
        }

        _uiState.update { it.copy(isLoading = true, errorMessage = null) }

        viewModelScope.launch {
            val result = loginUseCase(currentState.email, currentState.password)
            result.onSuccess { token ->
                // Guardamos el token de Firebase y el email en DataStore
                sessionRepository.saveSession(
                    token = token.accessToken,
                    email = currentState.email
                )
                _uiState.update { it.copy(isLoading = false, isSuccess = true) }
            }.onFailure { error ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = mapFirebaseError(error) // Mapeo de errores amigables
                    )
                }
            }
        }
    }

    // Función auxiliar para que el usuario entienda qué pasó
    private fun mapFirebaseError(error: Throwable): String {
        return when (error) {
            is com.google.firebase.auth.FirebaseAuthInvalidCredentialsException -> "Contraseña incorrecta o correo mal escrito."
            is com.google.firebase.auth.FirebaseAuthInvalidUserException -> "Este usuario no existe."
            else -> error.message ?: "Error de conexión"
        }
    }
}

data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val errorMessage: String? = null
)