package com.monadx.othello.ui.controller

import androidx.compose.foundation.layout.Column
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable

import com.monadx.othello.ui.AppState

class MenuController(appState: AppState): Controller(appState) {
    @Composable
    override fun view() {
        MaterialTheme {
            Column {
                Button(onClick = { appState.setController(VersusController(appState)) }) {
                    Text("Versus Mode")
                }
                Button(onClick = { appState.setController(AiController(appState)) }) {
                    Text("AI Mode")
                }
            }
        }
    }
}
