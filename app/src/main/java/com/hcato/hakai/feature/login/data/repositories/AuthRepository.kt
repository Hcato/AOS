package com.hcato.hakai.feature.login.data.repositories

import com.hcato.hakai.feature.login.data.datasource.remote.api.AuthApi
import com.hcato.hakai.feature.login.data.datasource.remote.api.RegisterRequest
import com.hcato.hakai.feature.login.domain.entities.AuthToken
import com.hcato.hakai.feature.login.domain.repositories.LoginRepository
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val api: AuthApi
) : LoginRepository {
    override suspend fun login(email: String, password: String): Result<AuthToken> {
        return try {
            val response = api.login(email, password)
            Result.success(AuthToken(response.access_token, response.token_type))
        } catch (e: Exception) {
            // Aquí puedes manejar excepciones específicas de Retrofit (HttpException)
            Result.failure(e)
        }
    }

    override suspend fun register(email: String, password: String): Result<String> {
        return try {
            val response = api.register(RegisterRequest(email, password))
            Result.success(response.mensaje)
        } catch (e: Exception) {
            // Si el correo ya existe, FastAPI devuelve un 400.
            // Aquí puedes manejar errores específicos si lo deseas.
            Result.failure(e)
        }
    }

    override suspend fun getUserProfile(): Result<String> {
        return try {
            val response = api.getUserProfile()
            if (response.isSuccessful) {
                val email = response.body()?.email_del_usuario ?: "Usuario"
                Result.success(email)
            } else {
                // Si es 401, el interceptor actuará antes de que este código termine,
                // pero igual devolvemos failure por buena práctica.
                Result.failure(Exception("Error al obtener perfil: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
