package com.hcato.hakai.feature.login.domain.repositories

import com.hcato.hakai.feature.login.domain.entities.AuthToken

interface LoginRepository {
    suspend fun login(email: String, password: String): Result<AuthToken>
    suspend fun register(email: String, password: String): Result<String>
}