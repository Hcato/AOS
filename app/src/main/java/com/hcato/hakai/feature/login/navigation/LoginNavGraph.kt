package com.hcato.hakai.feature.login.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.hcato.hakai.core.navigation.FeatureNavGraph
import com.hcato.hakai.core.navigation.Home
import com.hcato.hakai.core.navigation.LoginRoute
import com.hcato.hakai.core.navigation.RegisterRoute
import com.hcato.hakai.feature.login.presentation.screens.LoginScreen
import com.hcato.hakai.feature.login.presentation.screens.RegisterScreen

class LoginNavGraph : FeatureNavGraph {
    override fun registerGraph(
        navGraphBuilder: NavGraphBuilder,
        navController: NavHostController
    ) {
        navGraphBuilder.composable<LoginRoute> {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(Home) {
                        popUpTo(LoginRoute) { inclusive = true }
                    }
                },
                onNavigateToRegister = {
                    // Navegamos a la pantalla de registro
                    navController.navigate(RegisterRoute)
                }
            )
        }

        navGraphBuilder.composable<RegisterRoute> {
            RegisterScreen(
                onNavigateBackToLogin = {
                    // Volvemos atrás (al Login)
                    navController.popBackStack()
                }
            )
        }
    }
}