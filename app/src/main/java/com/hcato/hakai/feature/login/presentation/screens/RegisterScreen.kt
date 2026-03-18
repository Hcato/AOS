package com.hcato.hakai.feature.login.presentation.screens

import android.content.pm.ActivityInfo
import android.widget.Toast
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.hcato.hakai.feature.login.presentation.components.AnimatedBorderTextField
import com.hcato.hakai.feature.login.presentation.viewmodels.RegisterViewModel
import com.hcato.hakai.feature.principal.presentation.components.LockScreenOrientation

@Composable
fun RegisterScreen(
    viewModel: RegisterViewModel = hiltViewModel(),
    onNavigateBackToLogin: () -> Unit
) {
    LockScreenOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(uiState.isSuccess) {
        if (uiState.isSuccess) {
            Toast.makeText(context, uiState.successMessage, Toast.LENGTH_SHORT).show()
            onNavigateBackToLogin()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1E1E1E))
            .padding(horizontal = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Crear Cuenta",
            color = Color.White,
            fontSize = 36.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 64.dp)
        )

        // Email Field con borde animado
        AnimatedBorderTextField(
            value = uiState.email,
            onValueChange = viewModel::onEmailChanged,
            label = "Email",
            placeholder = "ejemplo@correo.com"
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Password Field con borde animado
        AnimatedBorderTextField(
            value = uiState.password,
            onValueChange = viewModel::onPasswordChanged,
            label = "Password",
            placeholder = "••••••••",
            visualTransformation = PasswordVisualTransformation()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Confirm Password Field con borde animado
        AnimatedBorderTextField(
            value = uiState.confirmPassword,
            onValueChange = viewModel::onConfirmPasswordChanged,
            label = "Confirm Password",
            placeholder = "••••••••",
            visualTransformation = PasswordVisualTransformation()
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Reutilizamos también la lógica de gradiente en el botón si lo deseas,
        // pero aquí mantengo tu estructura de botón oscuro para variar:
        Button(
            onClick = viewModel::register,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF333333),
                disabledContainerColor = Color.DarkGray
            ),
            enabled = !uiState.isLoading
        ) {
            if (uiState.isLoading) {
                CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
            } else {
                Text("Sign Up", color = Color.White, fontSize = 16.sp)
            }
        }

        uiState.errorMessage?.let { error ->
            Text(
                text = error,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(top = 16.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        TextButton(onClick = onNavigateBackToLogin) {
            Text("¿Ya tienes cuenta? Inicia sesión", color = Color.White)
        }
    }
}