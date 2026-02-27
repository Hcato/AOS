package com.hcato.hakai.feature.principal.data.datasource.remote.api

import com.hcato.hakai.feature.principal.data.model.LikeRequest
import com.hcato.hakai.feature.principal.data.model.LikeResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface StreamingApi {

    /**
     * Envía un Like al servidor.
     * El servidor responderá si fue exitoso y el nuevo conteo.
     */
    @POST("/api/estreno/{videoId}/like")
    suspend fun darLike(
        @Path("videoId") videoId: String,
        @Body request: LikeRequest
    ): LikeResponse

    /**
     * Obtiene el estado inicial de los likes cuando el usuario entra al stream.
     */
    @GET("/api/estreno/{videoId}/likes")
    suspend fun getLikes(
        @Path("videoId") videoId: String
    ): LikeResponse
}