package com.hcato.hakai.feature.video.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.hcato.hakai.core.navigation.FeatureNavGraph
import com.hcato.hakai.core.navigation.Video
import com.hcato.hakai.feature.video.presentation.screens.VideoScreen
class VideoNavGraph: FeatureNavGraph {
    override fun registerGraph(navGraphBuilder: NavGraphBuilder, navController: NavHostController) {
        navGraphBuilder.composable<Video> { backStackEntry ->
            // Extraemos de forma segura los argumentos
            val videoRoute = backStackEntry.toRoute<Video>()

            VideoScreen(
                videoId = videoRoute.id,
                onClickBack = { navController.popBackStack() }
            )
        }
    }
}