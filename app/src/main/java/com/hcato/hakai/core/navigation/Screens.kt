package com.hcato.hakai.core.navigation

import kotlinx.serialization.Serializable

@Serializable
object SplashRoute
@Serializable
object Principal
@Serializable
object Home
@Serializable
data class Video(
    val id: String
)

@Serializable
object LoginRoute

@Serializable
object RegisterRoute