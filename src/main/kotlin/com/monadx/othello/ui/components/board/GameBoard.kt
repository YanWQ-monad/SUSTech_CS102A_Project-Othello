package com.monadx.othello.ui.components.board

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp

import com.monadx.othello.chess.ChessColor
import com.monadx.othello.ui.Config.BLACK_CHESS_COLOR
import com.monadx.othello.ui.Config.CELL_SIZE
import com.monadx.othello.ui.Config.GAME_BOARD_BACKGROUND_COLOR
import com.monadx.othello.ui.Config.LAST_PLACED_INDICATOR_SIZE
import com.monadx.othello.ui.Config.MOVABLE_BORDER_COLOR
import com.monadx.othello.ui.Config.PIECE_SHADOW_ELEVATION
import com.monadx.othello.ui.Config.PIECE_SIZE
import com.monadx.othello.ui.Config.WHITE_CHESS_COLOR

object BoardConfig {
    val PLACEABLE_BORDER_WIDTH = 0.5.dp
    val CELL_BORDER_WIDTH = 1.dp
}

enum class CellState {
    None,
    CanPlace,
    LastPlaced,
    LastFlipped,
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

        var state = mutableStateOf(CellState.None)
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
    val borderModifier = if (piece.state.value == CellState.CanPlace) {
        Modifier.border(
            width = BoardConfig.PLACEABLE_BORDER_WIDTH,
            color = MOVABLE_BORDER_COLOR,
            shape = CircleShape,
        )
    } else {
        Modifier
    }

    val shadowModifier = if (piece.state.value in listOf(CellState.LastPlaced, CellState.LastFlipped)) {
        Modifier.drawBehind {
            val circleRadius = (PIECE_SIZE + PIECE_SHADOW_ELEVATION).value
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        piece.color.value.opposite.graphicsColor(),
                        piece.color.value.opposite.graphicsColor(),
                        piece.color.value.opposite.graphicsColor(),
                        piece.color.value.opposite.graphicsColor(),
                        Color.Transparent,
                    ),
                    radius = circleRadius + 1,
                ),
                radius = circleRadius,
            )
        }
    } else {
        Modifier
    }

    Box (
        Modifier
            .offset(offset, offset)
            .then(shadowModifier)
            .clip(CircleShape)
            .then(borderModifier)
    ) {
        Box(
            Modifier
                .size(PIECE_SIZE)
                .background(piece.color.value.graphicsColor())
                .clickable(
                    // enabled only if the position can place a chess piece, and now it is the user's turn
                    enabled = isCheatMode || (piece.state.value == CellState.CanPlace && isTurn),
                    onClick = { onClick(piece.x, piece.y) }
                )
        )

        if (piece.state.value == CellState.LastPlaced) {
            Box(
                Modifier
                    .size(LAST_PLACED_INDICATOR_SIZE)
                    .clip(CircleShape)
                    .background(Color.Red)
                    .align(Alignment.Center)
            )
        }
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
