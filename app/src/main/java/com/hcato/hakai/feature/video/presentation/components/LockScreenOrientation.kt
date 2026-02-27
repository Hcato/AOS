package com.hcato.hakai.feature.video.presentation.components

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.platform.LocalContext
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat

@Composable
fun LockScreenOrientation(orientation: Int) {
    val context = LocalContext.current

    DisposableEffect(Unit) {
        val activity = context as? Activity ?: return@DisposableEffect onDispose {}

        // Guardar la orientación original
        val originalOrientation = activity.requestedOrientation
        activity.requestedOrientation = orientation

        // --- OCULTAR MENÚS Y BARRAS (Modo Inmersivo) ---
        val window = activity.window
        val controller = WindowCompat.getInsetsController(window, window.decorView)

        controller.apply {
            // Oculta tanto la barra de estado (notificaciones) como la de navegación (botones/gestos)
            hide(WindowInsetsCompat.Type.systemBars())
            // Permite que el usuario las recupere deslizando, pero se ocultan solas después
            systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }

        onDispose {
            // Al salir, restauramos la orientación y volvemos a mostrar las barras
            activity.requestedOrientation = originalOrientation
            controller.show(WindowInsetsCompat.Type.systemBars())
        }
    }
}