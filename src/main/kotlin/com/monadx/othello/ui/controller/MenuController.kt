package com.monadx.othello.ui.controller

import androidx.compose.foundation.layout.Column
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable

import com.monadx.othello.ui.AppState
import com.monadx.othello.ui.Stage

class MenuController: Controller() {
    @Composable
    override fun view(state: AppState) {
        MaterialTheme {
            Column {
                Button(onClick = { state.setStage(Stage.VERSUS) }) {
                    Text("Versus Mode")
                }
            }
        }
    }
}
