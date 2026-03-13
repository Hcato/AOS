package com.hcato.hakai.core.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController

@Composable
fun NavigationWrapper(
    navGraphs: List<FeatureNavGraph>,
    currentToken: String? // Recibimos el estado de la sesión como parámetro
) {
    val navController = rememberNavController()

    // La magia reactiva SIN ViewModels
    LaunchedEffect(currentToken) {
        // Si el interceptor borra el token, el flujo emitirá null y esto se ejecutará
        if (currentToken == null) {
            navController.navigate(LoginRoute) {
                // Borramos todo el historial de pantallas para que no pueda volver atrás
                popUpTo(0) { inclusive = true }
                launchSingleTop = true
            }
        }
    }

    NavHost(
        navController = navController,
        startDestination = SplashRoute // Seguimos iniciando en el Splash
    ) {
        navGraphs.forEach { featureGraph ->
            featureGraph.registerGraph(this, navController)
        }
    }
}