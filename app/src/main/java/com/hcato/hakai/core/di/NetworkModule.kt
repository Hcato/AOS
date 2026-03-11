package com.hcato.hakai.core.di

import android.content.Context
import android.provider.Settings
import com.hcato.hakai.BuildConfig
import com.hcato.hakai.core.network.interceptors.LogInterceptor
import com.hcato.hakai.feature.login.data.datasource.remote.api.AuthApi
import com.hcato.hakai.feature.principal.data.datasource.remote.api.StreamingApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.socket.client.IO
import io.socket.client.Socket
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideSocket(@ApplicationContext context: Context): Socket {
        val androidId = Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)

        val options = IO.Options.builder()
            // Enviamos el ID en el handshake inicial
            .setAuth(mapOf("token" to androidId))
            .setReconnection(true)
            .build()

        return IO.socket(BuildConfig.BASE_URL, options)
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(authInterceptor: LogInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .build()
    }

    @Provides
    @Singleton
    @Named("AuthRetrofit")
    fun provideAuthRetrofit(okHttpClient: OkHttpClient): Retrofit = Retrofit.Builder()
        .baseUrl(BuildConfig.AUTH_BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit = Retrofit.Builder()
        .baseUrl(BuildConfig.BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): StreamingApi =
        retrofit.create(StreamingApi::class.java)
    @Provides
    @Named("androidId")
    @Singleton
    fun provideAndroidId(@ApplicationContext context: Context): String {
        return Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
    }

    @Provides
    @Singleton
    fun provideAuthApi(@Named("AuthRetrofit") retrofit: Retrofit): AuthApi =
        retrofit.create(AuthApi::class.java)
}
