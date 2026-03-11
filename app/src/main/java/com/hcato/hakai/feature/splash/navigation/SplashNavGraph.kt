package com.hcato.hakai.feature.splash.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.hcato.hakai.core.navigation.FeatureNavGraph
import com.hcato.hakai.core.navigation.SplashRoute
import com.hcato.hakai.feature.splash.presentation.screens.SplashScreen

class SplashNavGraph : FeatureNavGraph {
    override fun registerGraph(
        navGraphBuilder: NavGraphBuilder,
        navController: NavHostController
    ) {
        navGraphBuilder.composable<SplashRoute> {
            SplashScreen(
                onNavigate = { destination ->
                    // Navegamos al destino (Home o LoginRoute)
                    navController.navigate(destination) {
                        // Importante: Destruimos el SplashRoute de la pila para
                        // que el usuario no pueda volver al Splash presionando "Atrás"
                        popUpTo(SplashRoute) { inclusive = true }
                    }
                }
            )
        }
    }
}