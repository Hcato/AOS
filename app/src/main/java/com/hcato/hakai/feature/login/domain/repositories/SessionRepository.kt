package com.hcato.hakai.feature.login.domain.repositories

import kotlinx.coroutines.flow.Flow

interface SessionRepository {
    suspend fun saveSession(token: String, email: String)
    fun getToken(): Flow<String?>
    fun getEmail(): Flow<String?>
    suspend fun clearSession()
}