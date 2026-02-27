package com.hcato.hakai.feature.video.presentation.screens

import android.app.Activity
import android.content.pm.ActivityInfo
import android.net.Uri
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
    videoId: String = "estreno_principal", // Puedes recibir esto desde la NavArgs
    onClickBack: () -> Unit
) {
    val context = LocalContext.current
    val uiState by viewModel.state.collectAsState()

    // Estado local para el loading del reproductor
    var isVideoLoading by remember { mutableStateOf(true) }

    // --- 1. ACTIVACIÓN DE RED ---
    // Al entrar a la pantalla, nos unimos al stream (Socket + Carga inicial de likes)
    LaunchedEffect(Unit) {
        viewModel.joinStream(videoId)
    }

    // 2. Manejo de Orientación
    LockScreenOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE)

    // 3. Instancias de VLC persistentes
    val libVLC = remember { LibVLC(context, arrayListOf("-vvv")) }
    val mediaPlayer = remember { MediaPlayer(libVLC) }
    val videoLayout = remember { VLCVideoLayout(context) }

    // 4. Carga asíncrona y manejo de eventos
    LaunchedEffect(uiState.videoUrl) {
        withContext(Dispatchers.IO) {
            val media = Media(libVLC, Uri.parse(uiState.videoUrl)).apply {
                addOption(":network-caching=2000")
                addOption(":clock-jitter=0")
                setHWDecoderEnabled(true, false)
            }
            mediaPlayer.media = media
            mediaPlayer.setEventListener { event ->
                if (event.type == MediaPlayer.Event.Playing) {
                    isVideoLoading = false
                    viewModel.onVideoPlaying() // Sincroniza el estado del VM
                }
            }
            withContext(Dispatchers.Main) {
                mediaPlayer.attachViews(videoLayout, null, true, false)
                mediaPlayer.play()
            }
        }
    }

    // 5. Limpieza al salir
    DisposableEffect(Unit) {
        onDispose {
            mediaPlayer.stop()
            mediaPlayer.detachViews()
            mediaPlayer.release()
            libVLC.release()
        }
    }

    // 6. Layout Final con Overlays
    Box(modifier = Modifier.fillMaxSize().background(Color.Black)) {
        // El reproductor VLC
        AndroidView(
            factory = { videoLayout },
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
        // Ponemos un degradado para que el texto sea legible siempre
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
                // Dentro de tu Row en VideoScreen
                Button(
                    onClick = { viewModel.sendLike(videoId) },
                    // El botón se deshabilita si está enviando O si ya dio like
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