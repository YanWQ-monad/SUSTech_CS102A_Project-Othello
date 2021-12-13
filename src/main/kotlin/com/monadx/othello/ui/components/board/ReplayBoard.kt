package com.monadx.othello.ui.components.board

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

import com.monadx.othello.ui.Config
import com.monadx.othello.ui.controller.ReplayController

@Composable
fun ReplayBoard(controller: ReplayController) {
    Column(
        Modifier
            .background(MaterialTheme.colorScheme.surface)
            .padding(PaneConfig.PANE_PADDING)
            .width(Config.CELL_SIZE * 8)
    ) {
        GameStatus(controller.state.status)

        Spacer(Modifier.height(PaneConfig.PANE_DIVIDE))

        GameBoard(
            controller.state.board,
            false,
            onClick = { _, _ -> },
        )

        Spacer(Modifier.height(PaneConfig.PANE_DIVIDE))

        Row {
            TextButton(
                onClick = { controller.appState.goBackMenu() },
                colors = ButtonDefaults.textButtonColors(
                    backgroundColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                ),
            ) {
                Text("Back")
            }

            TextButton(
                onClick = { controller.previousStep() }
            ) {
                Text(
                    text = "Previous",
                    color = MaterialTheme.colorScheme.onSurface,
                )
            }

            TextButton(
                onClick = { controller.nextStep() }
            ) {
                Text(
                    text = "Next",
                    color = MaterialTheme.colorScheme.onSurface,
                )
            }
        }
    }
}
