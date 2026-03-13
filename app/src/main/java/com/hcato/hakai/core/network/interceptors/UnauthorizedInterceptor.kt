package com.hcato.hakai.core.network.interceptors

import com.hcato.hakai.feature.login.domain.repositories.SessionRepository
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class UnauthorizedInterceptor @Inject constructor(
    private val sessionRepository: SessionRepository
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val response = chain.proceed(request)

        // Si el servidor nos dice que el token es inválido o expiró
        if (response.code == 401) {
            // Borramos la sesión de DataStore.
            // runBlocking es seguro aquí porque OkHttp ya está en un hilo secundario.
            runBlocking {
                sessionRepository.clearSession()
            }
        }

        return response
    }
}