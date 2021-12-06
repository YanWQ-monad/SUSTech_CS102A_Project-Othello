package com.monadx.othello.ui.components.menu

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import com.monadx.othello.ui.components.DialogContent

@Composable
fun ServerListeningDialog(onCloseRequest: () -> Unit) {
    DialogContent(onCloseRequest = onCloseRequest) {
        Text("Waiting for joining...")
    }
}
