package com.hcato.hakai.feature.video.presentation.screens

import android.app.Activity
import android.content.pm.ActivityInfo
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.hcato.hakai.feature.video.presentation.components.LiveBadge
import com.hcato.hakai.feature.video.presentation.components.LockScreenOrientation
import com.hcato.hakai.feature.video.presentation.components.VideoLoadingScreen
import com.hcato.hakai.feature.video.presentation.viemodels.VideoViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.videolan.libvlc.LibVLC
import org.videolan.libvlc.Media
import org.videolan.libvlc.MediaPlayer
import org.videolan.libvlc.util.VLCVideoLayout

@Composable
fun VideoScreen(
    viewModel: VideoViewModel = hiltViewModel(),
    onClickBack: () -> Unit
) {
    val context = LocalContext.current
    val uiState by viewModel.state.collectAsState()

    // Estado local para el loading del reproductor
    var isVideoLoading by remember { mutableStateOf(true) }

    // 1. Manejo de Orientación e Insets
    LockScreenOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE)

    // 2. Instancias de VLC persistentes
    val libVLC = remember { LibVLC(context, arrayListOf("-vvv")) }
    val mediaPlayer = remember { MediaPlayer(libVLC) }
    val videoLayout = remember { VLCVideoLayout(context) }

    // 3. Carga asíncrona y manejo de eventos
    LaunchedEffect(uiState.videoUrl) {
        withContext(Dispatchers.IO) {
            val media = Media(libVLC, Uri.parse(uiState.videoUrl)).apply {
                addOption(":network-caching=2000")
                addOption(":clock-jitter=0")
                setHWDecoderEnabled(true, false)
            }

            mediaPlayer.media = media

            // Escuchar cuando el video realmente empieza a reproducirse
            mediaPlayer.setEventListener { event ->
                if (event.type == MediaPlayer.Event.Playing) {
                    isVideoLoading = false
                }
            }

            withContext(Dispatchers.Main) {
                mediaPlayer.attachViews(videoLayout, null, true, false)
                mediaPlayer.play()
            }
        }
    }

    // 4. Limpieza al salir
    DisposableEffect(Unit) {
        onDispose {
            mediaPlayer.stop()
            mediaPlayer.detachViews()
            mediaPlayer.release()
            libVLC.release()
        }
    }

    // 5. Layout Final
    Box(modifier = Modifier.fillMaxSize().background(Color.Black)) {
        // El reproductor
        AndroidView(
            factory = { videoLayout },
            modifier = Modifier.fillMaxSize()
        )

        // Capa de carga
        if (isVideoLoading) {
            VideoLoadingScreen()
        }

        // Overlay: Botón cerrar
        IconButton(
            onClick = onClickBack,
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(16.dp)
                .background(Color.Black.copy(alpha = 0.4f), CircleShape)
        ) {
            Icon(Icons.Default.Close, null, tint = Color.White)
        }

        // Overlay: Badge En Vivo
        LiveBadge(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(16.dp)
        )
    }
}