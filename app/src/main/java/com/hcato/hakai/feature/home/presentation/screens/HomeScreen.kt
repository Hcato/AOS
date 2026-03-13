package com.hcato.hakai.feature.home.presentation.screens

import android.content.pm.ActivityInfo
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.outlined.AccountBox
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
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
import com.hcato.hakai.feature.home.presentation.components.HomeHeroSection
import com.hcato.hakai.feature.home.presentation.components.HomeSeriesInfo
import com.hcato.hakai.feature.home.presentation.viewmodels.HomeViewModel
import com.hcato.hakai.feature.principal.presentation.components.LockScreenOrientation

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    onClickPrincipal: () -> Unit,
    onNavigateToLogin: () -> Unit
) {
    LockScreenOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
    val uiState by viewModel.state.collectAsState()
    val state by viewModel.state.collectAsState()

    LaunchedEffect(state.isLoggedOut) {
        if (state.isLoggedOut) {
            onNavigateToLogin()
        }
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF00080B))
    ) {
        // 1. Capa de Arte (Fondo + Degradado)
        HomeHeroSection(R.drawable.jujutsu)

        // 2. Capa de Contenido
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // Empujamos el contenido hacia abajo
            Spacer(modifier = Modifier.fillMaxHeight(0.55f).aspectRatio(0.75f))

            // 3. Información de la serie segmentada
            HomeSeriesInfo(
                releaseInfo = uiState.releaseInfo,
                tags = uiState.tags,
                description = uiState.description
            )

            Spacer(modifier = Modifier.height(30.dp))

            // 4. Botón de Acción
            ActionButton(
                text = "Comenzar a ver",
                onClick = onClickPrincipal,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(120.dp))
        }
    }
}