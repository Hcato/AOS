package com.hcato.hakai.feature.login.data.repositories

import com.google.firebase.auth.FirebaseAuth
import com.hcato.hakai.core.repositories.UserRepository
import com.hcato.hakai.feature.login.data.datasource.remote.api.AuthApi
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val authApi: AuthApi, // Inyectamos tu API donde pusiste el @GET("/users/me")
    private val firebaseAuth: FirebaseAuth
) : UserRepository {

    override suspend fun getUserEmail(): Result<String> {
        val firebaseEmail = firebaseAuth.currentUser?.email

        return if (firebaseEmail != null) {
            Result.success(firebaseEmail)
        } else {
            // Si Firebase no tiene sesión, intentamos con la API como respaldo
            try {
                val response = authApi.getUserProfile()
                if (response.isSuccessful) {
                    Result.success(response.body()?.email_del_usuario ?: "Usuario")
                } else {
                    Result.failure(Exception("Sesión no encontrada"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
}