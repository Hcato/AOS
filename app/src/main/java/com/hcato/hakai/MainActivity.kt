package com.hcato.hakai

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.hcato.hakai.core.navigation.NavigationWrapper
import com.hcato.hakai.core.ui.theme.HakaiTheme
import com.hcato.hakai.feature.home.navigation.HomeNavGraph
import com.hcato.hakai.feature.principal.navigation.PrincipalNavGraph
import com.hcato.hakai.feature.video.navigation.VideoNavGraph

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val navGraphs = listOf(
            HomeNavGraph(),
            PrincipalNavGraph(),
            VideoNavGraph()
        )
        enableEdgeToEdge()
        setContent {
            HakaiTheme {
                NavigationWrapper(navGraphs)
            }
        }
    }
}