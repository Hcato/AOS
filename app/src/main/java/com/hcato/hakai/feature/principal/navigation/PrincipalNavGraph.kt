package com.hcato.hakai.feature.principal.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.hcato.hakai.core.navigation.FeatureNavGraph
import com.hcato.hakai.core.navigation.Principal
import com.hcato.hakai.core.navigation.Video
import com.hcato.hakai.feature.principal.presentation.screens.PrincipalScreen

class PrincipalNavGraph: FeatureNavGraph {
    override fun registerGraph(navGraphBuilder: NavGraphBuilder, navController: NavHostController) {
        navGraphBuilder.composable<Principal> {
            PrincipalScreen(
                onClickVideo = { navController.navigate(Video) },
                onClickBack = { navController.popBackStack() }
            )
        }
    }
}