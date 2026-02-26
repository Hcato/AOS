package com.hcato.hakai.feature.principal.presentation.components

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.platform.LocalContext

@Composable
fun LockScreenOrientation(orientation: Int) {
    val context = LocalContext.current
    DisposableEffect(Unit) {
        val activity = context as? Activity ?: return@DisposableEffect onDispose {}
        val originalOrientation = activity.requestedOrientation

        // Forzar la orientación deseada
        activity.requestedOrientation = orientation

        onDispose {
            // Al salir de esta pantalla, restauramos la orientación original
            activity.requestedOrientation = originalOrientation
        }
    }
}