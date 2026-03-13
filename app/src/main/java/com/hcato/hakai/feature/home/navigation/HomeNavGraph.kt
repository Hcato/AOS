package com.hcato.hakai.feature.home.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.hcato.hakai.core.navigation.FeatureNavGraph
import com.hcato.hakai.core.navigation.Home
import com.hcato.hakai.core.navigation.Principal
import com.hcato.hakai.feature.home.presentation.screens.HomeScreen

import com.hcato.hakai.core.navigation.LoginRoute

class HomeNavGraph: FeatureNavGraph {
    override fun registerGraph(navGraphBuilder: NavGraphBuilder, navController: NavHostController) {
        navGraphBuilder.composable<Home> {
            HomeScreen (
                onClickPrincipal = { navController.navigate(Principal) },

                onNavigateToLogin = {
                    navController.navigate(LoginRoute) {
                        popUpTo(navController.graph.id) {
                            inclusive = true
                        }
                    }
                }
            )
        }
    }
}