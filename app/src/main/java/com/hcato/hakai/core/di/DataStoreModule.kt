package com.hcato.hakai.core.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.hcato.hakai.feature.login.data.repositories.SessionRepositoryImpl

// 👇 AQUÍ ESTÁ LA MAGIA: Importamos la interfaz del Core 👇
import com.hcato.hakai.core.repositories.SessionRepository

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

// Extensión para crear el DataStore una sola vez en el contexto de la app
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "hakai_prefs")

@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {

    @Provides
    @Singleton
    fun provideDataStore(@ApplicationContext context: Context): DataStore<Preferences> {
        return context.dataStore
    }

    @Provides
    @Singleton
    fun provideSessionRepository(
        dataStore: DataStore<Preferences>,
        firebaseAuth: com.google.firebase.auth.FirebaseAuth // <-- Hilt lo sacará de tu FirebaseModule
    ): SessionRepository {
        // Ahora le pasamos ambos juguetes al constructor
        return SessionRepositoryImpl(dataStore, firebaseAuth)
    }
}