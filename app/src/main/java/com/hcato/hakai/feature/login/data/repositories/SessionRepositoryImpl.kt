package com.hcato.hakai.feature.login.data.repositories

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.hcato.hakai.core.repositories.SessionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SessionRepositoryImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : SessionRepository {

    // Definimos las "llaves" para guardar nuestros datos
    private val tokenKey = stringPreferencesKey("jwt_token")
    private val emailKey = stringPreferencesKey("user_email")

    override suspend fun saveSession(token: String, email: String) {
        dataStore.edit { preferences ->
            preferences[tokenKey] = token
            preferences[emailKey] = email
        }
    }

    override fun getToken(): Flow<String?> {
        return dataStore.data.map { preferences ->
            preferences[tokenKey]
        }
    }

    override fun getEmail(): Flow<String?> {
        return dataStore.data.map { preferences ->
            preferences[emailKey]
        }
    }

    override suspend fun clearSession() {
        dataStore.edit { preferences ->
            preferences.remove(tokenKey)
            preferences.remove(emailKey)
        }
    }
}