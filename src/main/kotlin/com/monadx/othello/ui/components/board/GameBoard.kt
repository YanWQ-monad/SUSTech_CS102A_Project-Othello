package com.monadx.othello.ui.components.board

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

import com.monadx.othello.chess.ChessColor
import com.monadx.othello.ui.Config.BLACK_CHESS_COLOR
import com.monadx.othello.ui.Config.CELL_SIZE
import com.monadx.othello.ui.Config.GAME_BOARD_BACKGROUND_COLOR
import com.monadx.othello.ui.Config.MOVABLE_BORDER_COLOR
import com.monadx.othello.ui.Config.PIECE_SIZE
import com.monadx.othello.ui.Config.WHITE_CHESS_COLOR

object BoardConfig {
    val PLACEABLE_BORDER_WIDTH = 0.5.dp
    val CELL_BORDER_WIDTH = 1.dp
}

class GameBoardState {
    val rows = Array(8) {
        x -> Array(8) { y -> Piece(x, y) }
    }

    val isCheatMode = mutableStateOf(false)

    fun at(x: Int, y: Int) = rows[x][y]

    class Piece(val x: Int, val y: Int) {
        // What chess piece on the board
        var color = mutableStateOf(ChessColor.EMPTY)

        // Whether the position can place a chess piece
        var canMove = mutableStateOf(false)
    }
}

@Composable
fun GameBoard(
    state: GameBoardState,
    isTurn: Boolean,
    onClick: (Int, Int) -> Unit,
) {
    Surface {
        Column {
            state.rows.forEach { row ->
                GameBoardRow(row, isTurn, state.isCheatMode.value, onClick)
            }
        }
    }
}

@Composable
fun GameBoardRow(
    row: Array<GameBoardState.Piece>,
    isTurn: Boolean,
    isCheatMode: Boolean,
    onClick: (Int, Int) -> Unit,
) {
    Row {
        row.forEach { cell ->
            Box (
                Modifier
                    // The border between the cells is 1 dp, thus, the width here is 0.5 dp
                    .size(CELL_SIZE)
                    .border(BoardConfig.CELL_BORDER_WIDTH / 2, Color.Black)
                    .background(GAME_BOARD_BACKGROUND_COLOR)
            ) {
                GameBoardPiece(cell, isTurn, isCheatMode, onClick)
            }
        }
    }
}

@Composable
fun GameBoardPiece(
    piece: GameBoardState.Piece,
    isTurn: Boolean,
    isCheatMode: Boolean,
    onClick: (Int, Int) -> Unit,
) {
    // to make the chess piece in the center
    val offset = (CELL_SIZE - PIECE_SIZE) / 2

    // if the position can place a chess piece, show the hollow circle
    val borderModifier = if (piece.canMove.value) {
        Modifier.border(
            width = BoardConfig.PLACEABLE_BORDER_WIDTH,
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
                .background(piece.color.value.graphicsColor())
                .clickable(
                    // enabled only if the position can place a chess piece, and now it is the user's turn
                    enabled = isCheatMode || (piece.canMove.value && isTurn),
                    onClick = { onClick(piece.x, piece.y) }
                )
        )
    }
}

// Map `ChessColor` to `Color`
fun (ChessColor).graphicsColor(): Color {
    return when (this) {
        ChessColor.BLACK -> BLACK_CHESS_COLOR
        ChessColor.WHITE -> WHITE_CHESS_COLOR
        ChessColor.EMPTY -> Color.Transparent
    }
}
