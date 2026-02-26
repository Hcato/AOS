package com.hcato.hakai.feature.home.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun HomeSeriesInfo(releaseInfo: String, tags: String, description: String) {
    Column {
        // INFO DE ESTRENO
        Text(
            text = releaseInfo,
            color = Color(0xFF00E676),
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Tags
        Text(
            text = tags,
            color = Color.LightGray.copy(alpha = 0.8f),
            style = MaterialTheme.typography.bodySmall
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Descripción
        Text(
            text = description,
            color = Color.White,
            style = MaterialTheme.typography.bodyMedium,
            lineHeight = 22.sp,
            textAlign = TextAlign.Justify
        )
    }
}