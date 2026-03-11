package com.hcato.hakai.feature.login.domain.usecases

import com.hcato.hakai.feature.login.domain.entities.AuthToken
import com.hcato.hakai.feature.login.domain.repositories.LoginRepository
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val repository: LoginRepository
) {
    suspend operator fun invoke(email: String, password: String): Result<AuthToken> {
        // Aquí podrías agregar validaciones extra (ej. formato de email) antes de ir al repo
        if (email.isBlank() || password.isBlank()) {
            return Result.failure(Exception("Los campos no pueden estar vacíos"))
        }
        return repository.login(email, password)
    }
}