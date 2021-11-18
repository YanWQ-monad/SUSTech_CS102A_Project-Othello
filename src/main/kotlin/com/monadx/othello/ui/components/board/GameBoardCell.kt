package com.monadx.othello.ui.components.board

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

import com.monadx.othello.ui.Config.CELL_SIZE
import com.monadx.othello.ui.Config.GAME_BOARD_BACKGROUND_COLOR
import com.monadx.othello.ui.controller.GamingController

class GameBoardCell(val controller: GamingController, val x: Int) {
    val pieces = Array(8) { y -> GameBoardPiece(controller, x, y) }

    // access the piece object at the specified position
    fun pieceAt(index: Int) = pieces[index]

    @Composable
    fun render() {
        Row(
            Modifier
                .height(CELL_SIZE)
        ) {
            pieces.forEach { piece ->
                Box (
                    Modifier
                        // The border between the cells is 1 dp, thus, the width here is 0.5 dp
                        .border(0.5.dp, Color.Black)
                        .size(CELL_SIZE)
                        .background(GAME_BOARD_BACKGROUND_COLOR)
                ) {
                    piece.render()
                }
            }
        }
    }
}
