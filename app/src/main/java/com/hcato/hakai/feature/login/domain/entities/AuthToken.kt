package com.hcato.hakai.feature.login.domain.entities

data class AuthToken(
    val accessToken: String,
    val tokenType: String
)