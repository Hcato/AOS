package com.hcato.hakai.feature.principal.presentation.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
fun DetailsModal(show: Boolean, description: String, onDismiss: () -> Unit) {
    if (show) {
        AlertDialog(
            onDismissRequest = onDismiss,
            containerColor = Color(0xFF1A1A1A),
            title = { Text("Sinopsis Completa", color = Color.White) },
            text = { Text(text = description, color = Color.LightGray) },
            confirmButton = {
                TextButton(onClick = onDismiss) {
                    Text("CERRAR", color = Color(0xFFFF6400))
                }
            }
        )
    }
}