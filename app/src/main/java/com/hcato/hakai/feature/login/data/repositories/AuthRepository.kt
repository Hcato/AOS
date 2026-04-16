package com.hcato.hakai.feature.login.data.repositories

import com.google.firebase.auth.FirebaseAuth
import com.hcato.hakai.feature.login.data.datasource.remote.api.AuthApi
import com.hcato.hakai.feature.login.data.datasource.remote.api.RegisterRequest
import com.hcato.hakai.feature.login.domain.entities.AuthToken
import com.hcato.hakai.feature.login.domain.repositories.LoginRepository
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val api: AuthApi,
    private val firebaseAuth: FirebaseAuth
) : LoginRepository {
    override suspend fun login(email: String, password: String): Result<AuthToken> {
        return try {
            val authResult = firebaseAuth.signInWithEmailAndPassword(email, password).await()

            val token = authResult.user?.getIdToken(false)?.await()?.token
                ?: throw Exception("No se pudo obtener el token de Firebase")
            Result.success(AuthToken(token, "Bearer"))

        } catch (e: Exception) {
            // Aquí puedes manejar excepciones específicas de Retrofit (HttpException)
            Result.failure(e)
        }
    }

    override suspend fun register(email: String, password: String): Result<String> {
        return try {
            // 1. Registro prioritario en Firebase
            val authResult = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            val userEmail = authResult.user?.email ?: email

            // 2. Intento de registro en el backend propio (FastAPI)
            // Lo envolvemos en otro try-catch para que si la API falla,
            // NO rompa el éxito de Firebase.
            try {
                api.register(RegisterRequest(email, password))
            } catch (apiException: Exception) {
                // Logueamos el error pero no lo lanzamos, porque Firebase YA lo creó
                println("Error al sincronizar con FastAPI: ${apiException.message}")
            }

            Result.success("Usuario creado: $userEmail")

        } catch (e: Exception) {
            // Este catch solo se activa si FIREBASE falla (ej. correo duplicado o sin red)
            Result.failure(e)
        }
    }

    override suspend fun getUserProfile(): Result<String> {
        val currentUser = firebaseAuth.currentUser
        return if (currentUser != null) {
            Result.success(currentUser.email ?: "Usuario")
        } else {
            Result.failure(Exception("No hay sesión activa"))
        }
    }
}
