package com.hcato.hakai.feature.home.presentation.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ActionButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    // --- 1. LÓGICA DE LA ANIMACIÓN ---
    val infiniteTransition = rememberInfiniteTransition(label = "MikuGlow")

    // Animamos un valor de 0 a 1000 para desplazar el gradiente
    val xOffset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1500f,
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "XOffset"
    )

    // --- 2. DEFINICIÓN DEL GRADIENTE ---
    // Si está habilitado, mostramos el brillo. Si no, un gris estático.
    val mikuBrush = if (enabled) {
        Brush.linearGradient(
            colors = listOf(
                Color(0xFF00BCD4), // Tu color base (Cian)
                Color(0xFF6FDDEE), // Color más claro
                Color(0xFF90DDEC), // El "brillo" blanco
                Color(0xFF6FDDEE),
                Color(0xFF00BCD4)  // Vuelve al base
            ),
            start = Offset(xOffset - 500f, xOffset - 500f),
            end = Offset(xOffset, xOffset),
            tileMode = TileMode.Mirror
        )
    } else {
        Brush.linearGradient(listOf(Color.DarkGray, Color.Gray))
    }

    // --- 3. EL BOTÓN ---
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier
            .fillMaxWidth()
            .height(52.dp)
            // Aplicamos el gradiente mediante el background del modifier
            .background(mikuBrush, shape = RoundedCornerShape(4.dp)),
        colors = ButtonDefaults.buttonColors(
            // Ponemos transparente el contenedor para que se vea nuestro gradiente
            containerColor = Color.Transparent,
            disabledContainerColor = Color.Transparent,
            contentColor = Color.Black,
            disabledContentColor = Color.LightGray
        ),
        shape = RoundedCornerShape(4.dp),
        contentPadding = PaddingValues(0.dp) // Para que el gradiente llegue a los bordes
    ) {
        if (!enabled) {
            CircularProgressIndicator(
                modifier = Modifier.size(20.dp),
                color = Color.LightGray,
                strokeWidth = 2.dp
            )
            Spacer(Modifier.width(8.dp))
        } else {
            Icon(Icons.Default.PlayArrow, contentDescription = null)
            Spacer(Modifier.width(8.dp))
        }

        Text(
            text = text.uppercase(),
            fontWeight = FontWeight.ExtraBold, // Un poco más de peso para que resalte en el brillo
            letterSpacing = 1.sp
        )
    }
}