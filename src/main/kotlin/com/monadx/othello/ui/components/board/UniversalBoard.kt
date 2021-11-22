package com.monadx.othello.ui.components.board

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable

import com.monadx.othello.ui.controller.GamingController

@Composable
fun UniversalBoard(controller: GamingController) {
    Column {
        GamePane(
            controller.state,
            onClick = { x, y -> controller.onClick(x, y) },
        )

        Row {
            TextButton(
                onClick = { controller.undo() }
            ) {
                Text("Undo")
            }

            TextButton(
                onClick = { controller.restart() }
            ) {
                Text("Restart")
            }

            TextButton(
                onClick = { controller.save() }
            ) {
                Text("Save")
            }

            TextButton(
                onClick = { controller.load() }
            ) {
                Text("Load")
            }
        }
    }
}
