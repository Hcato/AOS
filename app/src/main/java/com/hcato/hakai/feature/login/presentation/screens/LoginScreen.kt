package com.hcato.hakai.feature.login.presentation.screens

import android.content.pm.ActivityInfo
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.hcato.hakai.feature.login.presentation.components.AnimatedBorderTextField
import com.hcato.hakai.feature.login.presentation.viewmodels.LoginViewModel
import com.hcato.hakai.feature.principal.presentation.components.LockScreenOrientation

@Composable
fun LoginScreen(
    viewModel: LoginViewModel = hiltViewModel(),
    onLoginSuccess: () -> Unit,
    onNavigateToRegister: () -> Unit
) {
    LockScreenOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
    val uiState by viewModel.uiState.collectAsState()

    // Observar si el login fue exitoso para navegar
    LaunchedEffect(uiState.isSuccess) {
        if (uiState.isSuccess) {
            onLoginSuccess()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1E1E1E)) // Ajusta al gris oscuro de tu imagen
            .padding(horizontal = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "AOS",
            color = Color.White,
            fontSize = 48.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 64.dp)
        )

        // Email Field
        Column(modifier = Modifier.fillMaxWidth()) {
            AnimatedBorderTextField(
                value = uiState.email,
                onValueChange = viewModel::onEmailChanged,
                label = "Email",
                placeholder = "ejemplo@correo.com"
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Password Field
        Column(modifier = Modifier.fillMaxWidth()) {
            AnimatedBorderTextField(
                value = uiState.password,
                onValueChange = viewModel::onPasswordChanged,
                label = "Password",
                placeholder = "••••••••",
                visualTransformation = PasswordVisualTransformation()
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Sign In Button
        Button(
            onClick = viewModel::login,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF333333)),
            enabled = !uiState.isLoading
        ) {
            if (uiState.isLoading) {
                CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
            } else {
                Text("Sign In", color = Color.White, fontSize = 16.sp)
            }
        }

        // Mostrar error si lo hay
        uiState.errorMessage?.let { error ->
            Text(
                text = error,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(top = 16.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        TextButton(onClick = { /* TODO: Forgot Password */ }) {
            Text("Forgot password?", color = Color.White)
        }

        TextButton(
            onClick = onNavigateToRegister // Llamamos a la función al hacer click
        ) {
            Text(
                text = "¿No tienes cuenta? Regístrate aquí",
                color = Color.Gray // O el color que mejor quede con tu diseño oscuro
            )
        }

    }
}