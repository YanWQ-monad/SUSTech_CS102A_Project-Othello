package com.monadx.othello.ui.controller

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

import com.monadx.othello.ui.AppState
import com.monadx.othello.ui.controller.menu.ChessChooseController

class MenuController(appState: AppState): Controller(appState) {
    var dialogController: MutableState<Controller?> = mutableStateOf(null)

    @Composable
    override fun view() {
        MaterialTheme {
            Box(
                Modifier
                    .width(160.dp)
                    .height(320.dp)
            ) {
                Column {
                    Button(onClick = { appState.setController(VersusController(appState)) }) {
                        Text("Versus Mode")
                    }
                    Button(onClick = { dialogController.value = ChessChooseController(appState) }) {
                        Text("AI Mode")
                    }
                }
                dialogController.value?.view()
            }
        }
    }

    fun closeDialog() {
        dialogController.value = null
    }
}
