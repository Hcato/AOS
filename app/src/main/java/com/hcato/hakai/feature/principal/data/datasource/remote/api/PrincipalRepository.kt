package com.hcato.hakai.feature.principal.data.datasource.remote.api

interface PrincipalRepository {
    suspend fun isStreamAvailable(): Boolean
}