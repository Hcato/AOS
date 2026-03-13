package com.hcato.hakai

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.hcato.hakai.core.navigation.NavigationWrapper
import com.hcato.hakai.core.ui.theme.HakaiTheme
import com.hcato.hakai.feature.home.navigation.HomeNavGraph
import com.hcato.hakai.core.repositories.SessionRepository
import com.hcato.hakai.feature.login.navigation.LoginNavGraph
import com.hcato.hakai.feature.principal.navigation.PrincipalNavGraph
import com.hcato.hakai.feature.splash.navigation.SplashNavGraph
import com.hcato.hakai.feature.video.navigation.VideoNavGraph
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    // Inyectamos directamente el repositorio para leer el flujo de la sesión
    @Inject
    lateinit var sessionRepository: SessionRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val navGraphs = listOf(
            SplashNavGraph(),
            LoginNavGraph(),
            HomeNavGraph(),
            PrincipalNavGraph(),
            VideoNavGraph()
        )

        enableEdgeToEdge()

        setContent {
            HakaiTheme {
                // Escuchamos el token en tiempo real en la capa más alta.
                // Usamos "LOADING" como valor inicial para evitar falsos positivos al abrir la app.
                val token by sessionRepository.getToken().collectAsState(initial = "LOADING")

                NavigationWrapper(
                    navGraphs = navGraphs,
                    currentToken = token // <-- Le pasamos el estado a la navegación
                )
            }
        }
    }
}