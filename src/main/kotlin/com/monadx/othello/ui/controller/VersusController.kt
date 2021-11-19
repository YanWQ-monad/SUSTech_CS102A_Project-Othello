package com.monadx.othello.ui.controller

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable

import com.monadx.othello.chess.Board
import com.monadx.othello.chess.Coordinate
import com.monadx.othello.ui.AppState
import com.monadx.othello.ui.components.board.GameBoardState
import com.monadx.othello.ui.components.board.GamePane
import com.monadx.othello.ui.components.board.GameStatusState

class VersusController: GamingController() {
    override val gameBoard = GameBoardState()

    override val gameStatus = GameStatusState()

    override val board = Board()

    init {
        super.syncBoardColor()
        super.syncStatus()
        super.setBoardPlaceable()
    }

    @Composable
    override fun view(state: AppState) {
        MaterialTheme {
            GamePane(
                gameBoard,
                gameStatus,
                true
            ) { x, y -> onClick(x, y) }
        }
    }

    override fun onClick(x: Int, y: Int) {
        if (!board.place(Coordinate(x, y))) {
            return
        }

        super.syncBoardColor()
        super.syncStatus()
        super.setBoardPlaceable()
        println("VersusController.onClick($x, $y)")
    }
}
