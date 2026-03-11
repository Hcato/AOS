package com.hcato.hakai.feature.login.domain.usecases

import com.hcato.hakai.feature.login.domain.repositories.LoginRepository
import javax.inject.Inject

class RegisterUseCase @Inject constructor(
    private val repository: LoginRepository
) {
    suspend operator fun invoke(email: String, password: String): Result<String> {
        if (email.isBlank() || password.isBlank()) {
            return Result.failure(Exception("Los campos no pueden estar vacíos"))
        }
        if (password.length < 6) {
            return Result.failure(Exception("La contraseña debe tener al menos 6 caracteres"))
        }
        return repository.register(email, password)
    }
}