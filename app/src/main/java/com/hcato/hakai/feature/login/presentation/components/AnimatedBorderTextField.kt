package com.hcato.hakai.feature.login.presentation.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp

@Composable
fun AnimatedBorderTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    placeholder: String,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    modifier: Modifier = Modifier
) {
    // --- LÓGICA DE ANIMACIÓN ---
    val infiniteTransition = rememberInfiniteTransition(label = "BorderGlow")
    val xOffset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 2000f,
        animationSpec = infiniteRepeatable(
            animation = tween(3500, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "xOffset"
    )

    // --- GRADIENTE Cyan CYBERCORE ---
    val animatedGradient = Brush.linearGradient(
        colors = listOf(
            Color(0xFF00BCD4), // Tu color base (Cian)
            Color(0xFF80DEEA), // Color más claro
            Color(0xFFFFFFFF), // El "brillo" blanco
            Color(0xFF80DEEA),
            Color(0xFF00BCD4)  // Vuelve al base
        ),
        start = Offset(xOffset - 500f, xOffset - 500f),
        end = Offset(xOffset, xOffset),
        tileMode = TileMode.Mirror
    )

    Column(modifier = modifier.fillMaxWidth()) {
        Text(label, color = Color.White, modifier = Modifier.padding(bottom = 8.dp))

        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = { Text(placeholder, color = Color.Gray) },
            singleLine = true,
            visualTransformation = visualTransformation,
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                // Aquí aplicamos el borde animado
                .border(2.dp, animatedGradient, RoundedCornerShape(12.dp)),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black,
                // Ocultamos el borde por defecto para que solo se vea nuestro gradiente
                focusedBorderColor = Color.Transparent,
                unfocusedBorderColor = Color.Transparent
            )
        )
    }
}