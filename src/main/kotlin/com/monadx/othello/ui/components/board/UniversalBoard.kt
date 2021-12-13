package com.monadx.othello.ui.components.board

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

import com.monadx.othello.ui.Config
import com.monadx.othello.ui.controller.GamingController

class GameState {
    val board = GameBoardState()
    val status = GameStatusState()
}

object PaneConfig {
    val PANE_PADDING = 12.dp
    val PANE_DIVIDE = 3.dp
}

@Composable
fun UniversalBoard(controller: GamingController) {
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
            controller.state.status.placable.value,
            onClick = { x, y -> controller.onClick(x, y) },
        )

        Spacer(Modifier.height(PaneConfig.PANE_DIVIDE))

        Row {
            TextButton(
                onClick = { controller.undo() }
            ) {
                Text(
                    text = "Undo",
                    color = MaterialTheme.colorScheme.onSurface,
                )
            }

            TextButton(
                onClick = { controller.restart() }
            ) {
                Text(
                    text = "Restart",
                    color = MaterialTheme.colorScheme.onSurface,
                )
            }

            TextButton(
                onClick = { controller.save() }
            ) {
                Text(
                    text = "Save",
                    color = MaterialTheme.colorScheme.onSurface,
                )
            }

            TextButton(
                onClick = { controller.load() }
            ) {
                Text(
                    text = "Load",
                    color = MaterialTheme.colorScheme.onSurface,
                )
            }
        }
    }
}
