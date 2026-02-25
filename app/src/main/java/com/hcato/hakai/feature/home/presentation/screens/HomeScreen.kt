package com.hcato.hakai.feature.home.presentation.screens

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun HomeScreen(
    onClickPrincipal : () -> Unit
) {
    Button(
        onClick = onClickPrincipal
    ) {
        Text(text = "Principal")
    }
}