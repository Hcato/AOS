package com.hcato.hakai.feature.video.presentation.screens

import android.content.pm.ActivityInfo
import androidx.annotation.OptIn
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.video.spherical.SphericalGLSurfaceView
import com.hcato.hakai.feature.video.presentation.components.LiveBadge
import com.hcato.hakai.feature.video.presentation.components.LockScreenOrientation
import com.hcato.hakai.feature.video.presentation.components.VideoLoadingScreen
import com.hcato.hakai.feature.video.presentation.viemodels.VideoViewModel

@OptIn(UnstableApi::class)
@Composable
fun VideoScreen(
    viewModel: VideoViewModel = hiltViewModel(),
    videoId: String = "estreno_principal",
    onClickBack: () -> Unit
) {
    val context = LocalContext.current
    val uiState by viewModel.state.collectAsState()

    // Estado local para el loading del reproductor
    var isVideoLoading by remember { mutableStateOf(true) }

    // --- 1. ACTIVACIÓN DE RED ---
    LaunchedEffect(Unit) {
        viewModel.joinStream(videoId)
    }

    // 2. Manejo de Orientación
    LockScreenOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE)

    // 3. Instancia de ExoPlayer
    val player = remember { ExoPlayer.Builder(context).build() }

    // 4. Carga de video y manejo de estados del reproductor
    LaunchedEffect(uiState.videoUrl) {
        if (uiState.videoUrl.isNotEmpty()) {
            val mediaItem = MediaItem.fromUri(uiState.videoUrl)
            player.setMediaItem(mediaItem)
            player.prepare()
            player.play()
        }
    }

    // Escuchamos cuando el video empieza a reproducirse para quitar el loading
    DisposableEffect(player) {
        val listener = object : Player.Listener {
            override fun onIsPlayingChanged(isPlaying: Boolean) {
                if (isPlaying) {
                    isVideoLoading = false
                    viewModel.onVideoPlaying()
                }
            }
        }
        player.addListener(listener)

        // 5. Limpieza al salir
        onDispose {
            player.removeListener(listener)
            player.release()
        }
    }

    // 6. Layout Final con Overlays
    Box(modifier = Modifier.fillMaxSize().background(Color.Black)) {

        // --- LA MAGIA: Conectando el hardware (Giroscopio) ---
        val lifecycleOwner = LocalLifecycleOwner.current

        // 1. Creamos la esfera y la recordamos
        val sphericalView = remember {
            SphericalGLSurfaceView(context).apply {
                setDefaultStereoMode(C.STEREO_MODE_MONO)
            }
        }

        // 2. Le decimos a la esfera que encienda/apague el giroscopio según si la app está abierta
        DisposableEffect(lifecycleOwner) {
            val observer = LifecycleEventObserver { _, event ->
                if (event == Lifecycle.Event.ON_RESUME) {
                    sphericalView.onResume() // ¡ENCIENDE EL GIROSCOPIO Y EL MOTOR 3D!
                } else if (event == Lifecycle.Event.ON_PAUSE) {
                    sphericalView.onPause()  // Apaga el hardware si el usuario minimiza la app
                }
            }
            lifecycleOwner.lifecycle.addObserver(observer)

            onDispose {
                lifecycleOwner.lifecycle.removeObserver(observer)
            }
        }

        // 3. Pintamos la esfera en pantalla y le conectamos el video
        AndroidView(
            factory = {
                player.setVideoSurfaceView(sphericalView)
                sphericalView
            },
            modifier = Modifier.fillMaxSize()
        )

        // Capa de carga
        if (isVideoLoading) {
            VideoLoadingScreen()
        }

        // --- OVERLAY SUPERIOR ---
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onClickBack,
                modifier = Modifier.background(Color.Black.copy(alpha = 0.4f), CircleShape)
            ) {
                Icon(Icons.Default.Close, null, tint = Color.White)
            }

            LiveBadge()
        }

        // --- OVERLAY INFERIOR (Interacción en tiempo real) ---
        Box(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .fillMaxWidth()
                .background(
                    androidx.compose.ui.graphics.Brush.verticalGradient(
                        colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.7f))
                    )
                )
                .padding(horizontal = 24.dp, vertical = 20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Contador de Espectadores
                Text(
                    text = "👥 ${uiState.viewers}",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.bodyLarge
                )

                Spacer(modifier = Modifier.weight(1f))

                // Botón de Like con contador
                Button(
                    onClick = { viewModel.sendLike(videoId) },
                    enabled = !uiState.isLikeSending && !uiState.hasLiked,
                    shape = RoundedCornerShape(20.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (uiState.hasLiked) Color.Gray else Color.Red.copy(alpha = 0.8f),
                        disabledContainerColor = if (uiState.hasLiked) Color.Gray.copy(alpha = 0.5f) else Color.Red.copy(alpha = 0.4f)
                    )
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = if (uiState.hasLiked) Icons.Default.Check else Icons.Default.Favorite,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "${uiState.totalLikes}",
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                }
            }
        }
    }
}