package com.hcato.hakai.core.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController

@Composable
fun NavigationWrapper(navGraphs: List<FeatureNavGraph>) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = SplashRoute
    ) {
        navGraphs.forEach { featureGraph ->
            featureGraph.registerGraph(this, navController)
        }
    }
}