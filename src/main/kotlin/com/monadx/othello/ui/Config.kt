package com.monadx.othello.ui

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em

object Config {
    val CELL_SIZE = 48.dp
    val PIECE_SIZE = 40.dp

    val STATUS_ICON_SIZE = 32.dp
    val STATUS_ICON_BORDER_WIDTH = 2.5.dp
    val STATUS_SCORE_SIZE = 1.8.em
    val STATUS_SCORE_BAR_HEIGHT = 5.dp

    val MOVABLE_BORDER_COLOR = Color.Gray
    val WHITE_CHESS_COLOR = Color.White
    val BLACK_CHESS_COLOR = Color.Black
    val GAME_BOARD_BACKGROUND_COLOR = Color(255, 150, 50)
}
