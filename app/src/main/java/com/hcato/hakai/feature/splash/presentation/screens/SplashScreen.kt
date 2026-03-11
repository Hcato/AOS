package com.hcato.hakai.feature.splash.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.hcato.hakai.feature.splash.presentation.viewmodels.SplashViewModel

@Composable
fun SplashScreen(
    viewModel: SplashViewModel = hiltViewModel(),
    onNavigate: (Any) -> Unit
) {
    // Escuchamos el evento de navegación del ViewModel
    LaunchedEffect(Unit) {
        viewModel.navigationEvent.collect { destination ->
            onNavigate(destination)
        }
    }

    // UI del Splash (puedes agregar tu logo aquí si lo deseas)
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF00080B)) // Mismo color de fondo de tu app
    )
}