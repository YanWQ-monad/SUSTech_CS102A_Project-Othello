package com.monadx.othello.ui

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp

object Config {
    val CELL_SIZE = 52.dp
    val PIECE_SIZE = 42.dp

    val STATUS_ICON_SIZE = 32.dp
    val STATUS_ICON_BORDER_WIDTH = 2.5.dp
    val STATUS_SCORE_SIZE = 1.8.em
    val STATUS_SCORE_BAR_HEIGHT = 5.dp

    val MOVABLE_BORDER_COLOR = Color.Black
    val WHITE_CHESS_COLOR = Color.White
    val BLACK_CHESS_COLOR = Color.Black
    val GAME_BOARD_BACKGROUND_COLOR = Color(63, 142, 106)

    val DIALOG_BACKGROUND_COLOR = Color(238, 238, 238)
    val DIALOG_SHADOW_ELEVATION = 5.dp
    val DIALOG_ROUNDED_CORNER_SIZE = 5.dp
    val DIALOG_PADDING = 16.dp

    val DIALOG_WIDTH = 240.dp
    val DIALOG_TITLE_SIZE = 20.sp
    val DIALOG_LINE_SPACER_BIG = 12.dp
    val DIALOG_LINE_SPACER_SMALL = 8.dp
}
