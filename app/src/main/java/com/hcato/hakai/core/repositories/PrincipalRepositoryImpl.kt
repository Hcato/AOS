package com.hcato.hakai.core.repositories

import com.hcato.hakai.BuildConfig
import com.hcato.hakai.feature.principal.data.datasource.remote.api.PrincipalRepository
import com.hcato.hakai.feature.principal.data.datasource.remote.api.StreamingApi
import jakarta.inject.Inject

class PrincipalRepositoryImpl @Inject constructor(
    private val api: StreamingApi
) : PrincipalRepository {

    override suspend fun isStreamAvailable(): Boolean {
        return try {
            val response = api.checkStreamAvailability(BuildConfig.BASE_URL_STREAMING)
            response.isSuccessful
        } catch (e: Exception) {
            false
        }
    }
}