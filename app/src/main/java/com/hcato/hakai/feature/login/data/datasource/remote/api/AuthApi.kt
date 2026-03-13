package com.hcato.hakai.feature.login.data.datasource.remote.api

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

data class RegisterRequest(val email: String, val password: String)
data class RegisterResponse(val mensaje: String)
interface AuthApi {
    // FastAPI con OAuth2PasswordRequestForm requiere FormUrlEncoded
    @FormUrlEncoded
    @POST("/login")
    suspend fun login(
        @Field("username") username: String,
        @Field("password") password: String
    ): TokenResponse

    @POST("/register")
    suspend fun register(@Body request: RegisterRequest): RegisterResponse

    @GET("/users/me")
    suspend fun getUserProfile(): Response<UserProfileDto>
}

data class TokenResponse(
    val access_token: String,
    val token_type: String
)

data class UserProfileDto(
    val mensaje: String,
    val email_del_usuario: String
)