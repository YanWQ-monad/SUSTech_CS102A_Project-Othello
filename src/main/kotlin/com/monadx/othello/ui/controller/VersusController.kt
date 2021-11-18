package com.monadx.othello.ui.controller

import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable

import com.monadx.othello.ui.AppState

class VersusController: Controller() {
    @Composable
    override fun view(state: AppState) {
        MaterialTheme {
            Text("Versus Mode")
        }
    }
}
