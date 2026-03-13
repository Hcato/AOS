package com.hcato.hakai.feature.home.presentation.screens

import android.content.pm.ActivityInfo
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.hcato.hakai.R
import com.hcato.hakai.feature.home.presentation.components.ActionButton
import com.hcato.hakai.feature.home.presentation.components.HomeHeroSection
import com.hcato.hakai.feature.home.presentation.components.HomeSeriesInfo
import com.hcato.hakai.feature.home.presentation.viewmodels.HomeViewModel
import com.hcato.hakai.feature.principal.presentation.components.LockScreenOrientation

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    onClickPrincipal: () -> Unit,
    onNavigateToLogin: () -> Unit
) {
    LockScreenOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
    // Dejamos solo un colector de estado, como platicamos
    val uiState by viewModel.state.collectAsState()

    // Estado para controlar si el menú hamburguesa está abierto o cerrado
    var expanded by remember { mutableStateOf(false) }

    LaunchedEffect(uiState.isLoggedOut) {
        if (uiState.isLoggedOut) {
            onNavigateToLogin()
        }
    }

    Scaffold(
        // Agregamos la barra superior transparente
        topBar = {
            TopAppBar(
                title = { Text("") }, // Sin título
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent, // Fondo transparente
                    actionIconContentColor = Color.White // Ícono blanco
                ),
                actions = {
                    // Botón de Hamburguesa
                    IconButton(onClick = { expanded = true }) {
                        Icon(Icons.Default.Menu, contentDescription = "Menú")
                    }

                    // El Menú Desplegable
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                        modifier = Modifier.background(Color(0xFF1E1E1E)) // Fondo oscuro para el menú
                    ) {
                        // Elemento 1: Mostrar el correo (No es clickeable)
                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = uiState.userEmail,
                                    color = Color.LightGray,
                                    fontWeight = FontWeight.Bold
                                )
                            },
                            onClick = { /* No hace nada al tocarlo */ },
                            leadingIcon = {
                                Icon(Icons.Default.Person, contentDescription = null, tint = Color.LightGray)
                            }
                        )

                        // Elemento 2: Botón de Cerrar Sesión Manual
                        DropdownMenuItem(
                            text = { Text("Cerrar Sesión", color = Color(0xFF00BCD4)) },
                            onClick = {
                                expanded = false
                                viewModel.logout() // Llamamos a la función de tu ViewModel
                            }
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        // Contenido principal de la pantalla
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF00080B))
                // Aplicamos el padding del Scaffold para que la barra no tape el contenido de arriba
                // Aunque en este diseño transparente, puedes quitar el top de innerPadding si quieres que
                // la imagen llegue hasta el borde superior de la pantalla.
                .padding(bottom = innerPadding.calculateBottomPadding())
        ) {
            // 1. Capa de Arte (Fondo + Degradado)
            // IMPORTANTE: Para que la imagen quede detrás de la TopAppBar transparente,
            // no le aplicamos el innerPadding.calculateTopPadding() a esta Box principal.
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
}