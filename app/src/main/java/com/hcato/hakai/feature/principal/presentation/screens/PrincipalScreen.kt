package com.hcato.hakai.feature.principal.presentation.screens

import android.content.pm.ActivityInfo
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.hcato.hakai.R
import com.hcato.hakai.feature.home.presentation.components.ActionButton
import com.hcato.hakai.feature.principal.presentation.components.InfoColumn
import com.hcato.hakai.feature.principal.presentation.components.BackgroundHeader
import com.hcato.hakai.feature.principal.presentation.components.DetailsModal
import com.hcato.hakai.feature.principal.presentation.components.LockScreenOrientation
import com.hcato.hakai.feature.principal.presentation.components.SeriesMetadata
import com.hcato.hakai.feature.principal.presentation.components.TopBarActions
import com.hcato.hakai.feature.principal.presentation.viewmodels.PrincipalViewModel

@Composable
fun PrincipalScreen(
    viewModel: PrincipalViewModel = hiltViewModel(),
    onClickVideo: () -> Unit,
    onClickBack: () -> Unit
) {
    LockScreenOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
    val uiState by viewModel.state.collectAsState()
    val base = uiState.baseInfo
    var showDetails by remember { mutableStateOf(false) }

    Scaffold(
        bottomBar = {
            Column(modifier = Modifier.background(Color(0xFF00080B)).padding(50.dp)) {
                ActionButton(
                    text = if (uiState.isVideoAvailable) "¡ESTRENO DISPONIBLE!" else "Esperando el estreno...",
                    onClick = { if (uiState.isVideoAvailable) onClickVideo() },
                    enabled = uiState.isVideoAvailable
                )
            }
        },
        containerColor = Color(0xFF00080B)
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize().padding(paddingValues)) {

            BackgroundHeader(R.drawable.mikuexpo2024)

            Column(
                modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                TopBarActions(onClickBack)

                Spacer(modifier = Modifier.height(200.dp))

                Image(
                    painter = painterResource(id = R.drawable.mikulogo),
                    contentDescription = null,
                    modifier = Modifier.height(120.dp).padding(horizontal = 80.dp)
                )

                Text("THE CONCERT 360°", color = Color.White, style = MaterialTheme.typography.labelSmall)

                Spacer(modifier = Modifier.height(24.dp))

                Text("Disponible el ${uiState.nextEpisodeDate}", color = Color.White)

                Spacer(modifier = Modifier.height(12.dp))

                SeriesMetadata(uiState.rating, uiState.reviewsCount, base.tags)

                Text(
                    text = base.description,
                    color = Color.White,
                    modifier = Modifier.padding(horizontal = 16.dp),
                    maxLines = 3,
                    textAlign = TextAlign.Center
                )

                Text(
                    text = "Detalles de la artista",
                    color = Color(0xFF00BCD4),
                    modifier = Modifier.padding(vertical = 16.dp).clickable { showDetails = true }
                )

                Row(modifier = Modifier.fillMaxWidth().padding(16.dp), horizontalArrangement = Arrangement.SpaceEvenly) {
                    InfoColumn("Estudio", "Yamaha")
                    InfoColumn("Audio", "English")
                    InfoColumn("Clasif.", "16+")
                }

                Spacer(modifier = Modifier.height(120.dp))
            }

            DetailsModal(showDetails, uiState.fullDescription) { showDetails = false }
        }
    }
}