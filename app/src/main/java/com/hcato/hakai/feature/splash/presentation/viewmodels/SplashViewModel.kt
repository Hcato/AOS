package com.hcato.hakai.feature.splash.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hcato.hakai.core.navigation.Home
import com.hcato.hakai.core.navigation.LoginRoute
import com.hcato.hakai.feature.login.domain.repositories.SessionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val sessionRepository: SessionRepository
) : ViewModel() {

    // SharedFlow es ideal para eventos únicos como la navegación
    private val _navigationEvent = MutableSharedFlow<Any>()
    val navigationEvent = _navigationEvent.asSharedFlow()

    init {
        checkSession()
    }

    private fun checkSession() {
        viewModelScope.launch {
            // firstOrNull() lee el primer valor que emite el DataStore y termina la recolección
            val token = sessionRepository.getToken().firstOrNull()

            if (token.isNullOrEmpty()) {
                _navigationEvent.emit(LoginRoute)
            } else {
                _navigationEvent.emit(Home)
            }
        }
    }
}