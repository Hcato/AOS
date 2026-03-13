package com.hcato.hakai.core.repositories

// Interfaz transversal para obtener datos del usuario logueado
interface UserRepository {
    suspend fun getUserEmail(): Result<String>
}