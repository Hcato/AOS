package com.hcato.hakai.feature.video.presentation.screens

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun VideoScreen(
    onClickBack : () -> Unit
){
    Button(
        onClick = onClickBack
    ) {
        Text(text = "Voltar")
    }
}