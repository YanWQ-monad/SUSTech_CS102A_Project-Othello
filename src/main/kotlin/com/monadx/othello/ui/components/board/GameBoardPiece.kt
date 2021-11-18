package com.monadx.othello.ui.components.board

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

import com.monadx.othello.chess.ChessColor
import com.monadx.othello.ui.Config.BLACK_CHESS_COLOR
import com.monadx.othello.ui.Config.PIECE_SIZE
import com.monadx.othello.ui.Config.CELL_SIZE
import com.monadx.othello.ui.Config.MOVABLE_BORDER_COLOR
import com.monadx.othello.ui.Config.WHITE_CHESS_COLOR
import com.monadx.othello.ui.controller.GamingController

class GameBoardPiece(val controller: GamingController, val x: Int, val y: Int) {
    // What chess piece on the board
    var color = mutableStateOf(ChessColor.EMPTY)

    // Whether the position can place a chess piece
    var canMove = mutableStateOf(false)

    @Composable
    fun render() {
        // to make the chess piece in the center
        val offset = (CELL_SIZE - PIECE_SIZE) / 2

        // if the position can place a chess piece, show the hollow circle
        val borderModifier = if (canMove.value) {
            Modifier.border(
                width = if (canMove.value) 1.dp else 0.dp,
                color = MOVABLE_BORDER_COLOR,
                shape = CircleShape,
            )
        } else {
            Modifier
        }

        Box (
            Modifier
                .offset(offset, offset)
                .clip(CircleShape)
                .then(borderModifier)
        ) {
            Box(
                Modifier
                    .size(PIECE_SIZE)
                    .background(graphicsColor())
                    .clickable(
                        // enabled only if the position can place a chess piece, and now it is the user's turn
                        enabled = canMove.value && controller.isTurn(),
                        onClick = { controller.onClick(this@GameBoardPiece, x, y) }
                    )
            )
        }
    }

    private fun graphicsColor(): Color = when (color.value) {
        ChessColor.BLACK -> BLACK_CHESS_COLOR
        ChessColor.WHITE -> WHITE_CHESS_COLOR
        ChessColor.EMPTY -> Color.Transparent
    }
}
