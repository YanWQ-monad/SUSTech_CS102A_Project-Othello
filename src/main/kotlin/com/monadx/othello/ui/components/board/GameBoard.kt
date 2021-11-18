package com.monadx.othello.ui.components.board

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable

import com.monadx.othello.ui.controller.GamingController

class GameBoard(val controller: GamingController) {
    val cells = Array(8) { x -> GameBoardCell(controller, x) }

    fun pieceAt(x: Int, y: Int) = cells[x].pieceAt(y)

    @Composable
    fun render() {
        Surface {
            Column {
                cells.forEach { cell ->
                    cell.render()
                }
            }
        }
    }
}
