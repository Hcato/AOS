package com.hcato.hakai.core.repositories

import com.hcato.hakai.feature.principal.data.datasource.remote.api.StreamingApi
import com.hcato.hakai.feature.principal.data.model.LikeRequest
import com.hcato.hakai.feature.video.data.datasource.remote.api.VideoRepository
import io.socket.client.Socket
import io.socket.emitter.Emitter
import javax.inject.Inject
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import org.json.JSONObject


class VideoRepositoryImpl @Inject constructor(
    private val socket: Socket,
    private val apiService: StreamingApi
) : VideoRepository {

    // Convierte los eventos de "espectadores" en un Flow
    override val viewersFlow: Flow<Int> = callbackFlow {
        val listener = Emitter.Listener { args ->
            val count = args[0].toString().toDouble().toInt()
            trySend(count) // Envía el dato al Flow
        }
        socket.on("update_viewers", listener)
        awaitClose { socket.off("update_viewers", listener) }
    }

    // Convierte los eventos de "likes" en un Flow
    override val likesFlow: Flow<Int> = callbackFlow {
        val listener = Emitter.Listener { args ->
            val total = args[0].toString().toDouble().toInt()
            trySend(total)
        }
        socket.on("new_like", listener)
        awaitClose { socket.off("new_like", listener) }
    }

    override fun connect() { socket.connect() }
    override fun disconnect() { socket.disconnect() }

    override suspend fun joinStream(videoId: String, androidId: String) {
        if (!socket.connected()) {
            // Espera pequeña para asegurar conexión si es necesario
            delay(500)
        }
        val data = JSONObject().apply {
            put("videoId", videoId)
            put("androidId", androidId)
        }
        socket.emit("join_estreno", data)
    }

    override suspend fun sendLike(videoId: String, androidId: String): Boolean {
        return try {
            val response = apiService.darLike(videoId, LikeRequest(androidId))
            response.success
        } catch (e: Exception) {
            false
        }
    }

    override suspend fun getInitialLikes(videoId: String): Int {
        return try {
            apiService.getLikes(videoId).totalLikes
        } catch (e: Exception) {
            0
        }
    }
}