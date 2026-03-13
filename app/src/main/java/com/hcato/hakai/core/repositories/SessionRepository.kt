package com.hcato.hakai.core.repositories

import kotlinx.coroutines.flow.Flow

// Esta interfaz ahora le pertenece a la app en general, no al Login
interface SessionRepository {
    suspend fun saveSession(token: String, email: String)
    fun getToken(): Flow<String?>
    fun getEmail(): Flow<String?>
    suspend fun clearSession()
}