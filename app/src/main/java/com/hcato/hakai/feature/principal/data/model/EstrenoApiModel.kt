package com.hcato.hakai.feature.principal.data.model

import com.google.gson.annotations.SerializedName

data class LikeRequest(
    @SerializedName("androidId") val androidId: String
)

/**
 * Lo que el servidor nos responde (POST y GET)
 */
data class LikeResponse(
    @SerializedName("success") val success: Boolean = false,
    @SerializedName("totalLikes") val totalLikes: Int = 0,
    @SerializedName("message") val message: String? = null
)