package com.hcato.hakai.feature.login.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hcato.hakai.feature.login.domain.usecases.RegisterUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val registerUseCase: RegisterUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(RegisterUiState())
    val uiState = _uiState.asStateFlow()

    fun onEmailChanged(email: String) {
        _uiState.update { it.copy(email = email, errorMessage = null) }
    }

    fun onPasswordChanged(password: String) {
        _uiState.update { it.copy(password = password, errorMessage = null) }
    }

    // Nuevo método para la confirmación
    fun onConfirmPasswordChanged(confirmPassword: String) {
        _uiState.update { it.copy(confirmPassword = confirmPassword, errorMessage = null) }
    }

    fun register() {
        val currentState = _uiState.value

        // 1. Validaciones locales
        if (currentState.email.isBlank() || currentState.password.isBlank()) {
            _uiState.update { it.copy(errorMessage = "Completa todos los campos") }
            return
        }

        if (currentState.password != currentState.confirmPassword) {
            _uiState.update { it.copy(errorMessage = "Las contraseñas no coinciden") }
            return
        }

        _uiState.update { it.copy(isLoading = true, errorMessage = null) }

        viewModelScope.launch {
            val result = registerUseCase(currentState.email, currentState.password)
            result.onSuccess { mensaje ->
                _uiState.update { it.copy(isLoading = false, isSuccess = true, successMessage = mensaje) }
            }.onFailure { error ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = mapFirebaseRegisterError(error)
                    )
                }
            }
        }
    }

    private fun mapFirebaseRegisterError(error: Throwable): String {
        return when (error) {
            is com.google.firebase.auth.FirebaseAuthUserCollisionException -> "Este correo ya está registrado."
            is com.google.firebase.auth.FirebaseAuthWeakPasswordException -> "La contraseña es muy débil (mínimo 6 caracteres)."
            is com.google.firebase.auth.FirebaseAuthInvalidCredentialsException -> "El formato del correo es inválido."
            else -> error.message ?: "Error al crear la cuenta"
        }
    }
}

// Agregamos el campo confirmPassword al estado
data class RegisterUiState(
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "", // <-- Nuevo campo
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val successMessage: String? = null,
    val errorMessage: String? = null
)