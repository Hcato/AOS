package com.hcato.hakai.feature.login.data.repositories

import com.hcato.hakai.core.repositories.UserRepository
import com.hcato.hakai.feature.login.data.datasource.remote.api.AuthApi
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val authApi: AuthApi // Inyectamos tu API donde pusiste el @GET("/users/me")
) : UserRepository {

    override suspend fun getUserEmail(): Result<String> {
        return try {
            val response = authApi.getUserProfile()
            if (response.isSuccessful) {
                // Si la API responde bien, sacamos el email del DTO
                val email = response.body()?.email_del_usuario ?: "Usuario Desconocido"
                Result.success(email)
            } else {
                // Si la API da error (ej. 401), devolvemos failure
                Result.failure(Exception("Error HTTP: ${response.code()}"))
            }
        } catch (e: Exception) {
            // Si no hay internet o el servidor está caído
            Result.failure(e)
        }
    }
}