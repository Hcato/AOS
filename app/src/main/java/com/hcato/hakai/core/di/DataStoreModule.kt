package com.hcato.hakai.core.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.hcato.hakai.feature.login.data.local.SessionRepositoryImpl
import com.hcato.hakai.feature.login.domain.repositories.SessionRepository
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
    fun provideSessionRepository(dataStore: DataStore<Preferences>): SessionRepository {
        return SessionRepositoryImpl(dataStore)
    }
}