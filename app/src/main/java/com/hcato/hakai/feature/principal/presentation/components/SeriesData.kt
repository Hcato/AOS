package com.hcato.hakai.feature.principal.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun SeriesMetadata(rating: Double, reviews: String, tags: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = tags, color = Color.Gray, style = MaterialTheme.typography.bodySmall)
        Row(
            modifier = Modifier.padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            repeat(5) { Icon(Icons.Default.Star, null, tint = Color.White, modifier = Modifier.size(14.dp)) }
            Text(
                text = " Media: $rating ($reviews)",
                color = Color.Gray,
                style = MaterialTheme.typography.bodySmall
            )
            Icon(Icons.Default.ArrowDropDown, null, tint = Color.Gray)
        }
    }
}