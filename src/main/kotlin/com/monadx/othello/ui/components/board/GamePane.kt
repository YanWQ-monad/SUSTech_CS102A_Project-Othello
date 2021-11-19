package com.monadx.othello.ui.components.board

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

import com.monadx.othello.ui.Config.CELL_SIZE

object PaneConfig {
    val PANE_PADDING = 5.dp
    val PANE_DIVIDE = 3.dp
}

@Composable
fun GamePane(
    board: GameBoardState,
    status: GameStatusState,
    isTurn: Boolean,
    onClick: (Int, Int) -> Unit,
) {
    Column(
        Modifier
            .background(Color.Gray) // may be removed later
            .padding(PaneConfig.PANE_PADDING)
            .width(CELL_SIZE * 8)
    ) {
        GameStatus(status)
        Spacer(Modifier.height(PaneConfig.PANE_DIVIDE))
        GameBoard(board, isTurn, onClick)
    }
}
