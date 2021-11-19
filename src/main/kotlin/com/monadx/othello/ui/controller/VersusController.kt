package com.monadx.othello.ui.controller

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable

import com.monadx.othello.chess.Board
import com.monadx.othello.chess.Coordinate
import com.monadx.othello.chess.Game
import com.monadx.othello.ui.AppState
import com.monadx.othello.ui.components.board.GameBoardState
import com.monadx.othello.ui.components.board.GamePane
import com.monadx.othello.ui.components.board.GameStatusState

class VersusController: GamingController() {
    override val gameBoard = GameBoardState()

    override val gameStatus = GameStatusState()

    override val game = Game()

    init {
        super.syncAll()
    }

    @Composable
    override fun view(state: AppState) {
        MaterialTheme {
            Column {
                GamePane(
                    gameBoard,
                    gameStatus,
                    true
                ) { x, y -> onClick(x, y) }

                Row {
                    TextButton(
                        onClick = { undo() }
                    ) {
                        Text("Undo")
                    }

                    TextButton(
                        onClick = { restart() }
                    ) {
                        Text("Restart")
                    }
                }
            }
        }
    }

    override fun onClick(x: Int, y: Int) {
        if (!game.place(Coordinate(x, y))) {
            return
        }

        super.syncAll()
        println("VersusController.onClick($x, $y)")
    }

    fun undo() {
        game.undo()
        super.syncAll()
    }

    fun restart() {
        game.reset()
        super.syncAll()
    }
}
