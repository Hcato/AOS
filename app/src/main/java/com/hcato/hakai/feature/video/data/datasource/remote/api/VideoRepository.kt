package com.hcato.hakai.feature.video.data.datasource.remote.api

import kotlinx.coroutines.flow.Flow

interface VideoRepository {
    val viewersFlow: Flow<Int>
    val likesFlow: Flow<Int>

    fun connect()
    fun disconnect()
    suspend fun joinStream(videoId: String, androidId: String)
    suspend fun sendLike(videoId: String, androidId: String): Boolean
    suspend fun getInitialLikes(videoId: String): Int
}