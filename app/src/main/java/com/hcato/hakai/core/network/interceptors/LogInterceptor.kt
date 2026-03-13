package com.hcato.hakai.core.network.interceptors

import com.hcato.hakai.core.repositories.SessionRepository
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class LogInterceptor @Inject constructor(
    private val sessionRepository: SessionRepository
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val requestBuilder = chain.request().newBuilder()

        // Leemos el token de DataStore de forma síncrona.
        // runBlocking es seguro aquí porque OkHttp ya está en un hilo secundario de I/O.
        val token = runBlocking {
            sessionRepository.getToken().firstOrNull()
        }

        // Si tenemos un token guardado, lo agregamos al Header de Autorización
        if (!token.isNullOrEmpty()) {
            requestBuilder.addHeader("Authorization", "Bearer $token")
        }

        // Dejamos que la petición continúe con el nuevo Header modificado
        return chain.proceed(requestBuilder.build())
    }
}