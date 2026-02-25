package com.hcato.hakai.feature.principal.presentation.screens

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun PrincipalScreen(
    onClickVideo : () -> Unit,
    onClickBack : () -> Unit
){
    Button(
        onClick = onClickBack,
    ) {
        Text(text = "Voltar")
    }
    Button(
        onClick = onClickVideo
    ) {
        Text(text = "Ver video")
    }
}
