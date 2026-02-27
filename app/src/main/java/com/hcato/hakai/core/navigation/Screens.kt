package com.hcato.hakai.core.navigation

import kotlinx.serialization.Serializable

@Serializable
object Principal
@Serializable
object Home
@Serializable
data class Video(
    val id: String
)