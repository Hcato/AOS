package com.hcato.hakai.core.di

import android.content.Context
import android.provider.Settings
import com.hcato.hakai.feature.principal.data.datasource.remote.api.StreamingApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.socket.client.IO
import io.socket.client.Socket
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private const val BASE_URL = "http://44.193.188.243:3000"

    @Provides
    @Singleton
    fun provideSocket(@ApplicationContext context: Context): Socket {
        val androidId = Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)

        val options = IO.Options.builder()
            // Enviamos el ID en el handshake inicial
            .setAuth(mapOf("token" to androidId))
            .setReconnection(true)
            .build()

        return IO.socket(BASE_URL, options)
    }

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): StreamingApi =
        retrofit.create(StreamingApi::class.java)
}
