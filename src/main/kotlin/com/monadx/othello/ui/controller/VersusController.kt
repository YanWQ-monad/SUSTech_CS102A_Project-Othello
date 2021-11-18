package com.monadx.othello.ui.controller

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import com.monadx.othello.chess.Board

import com.monadx.othello.chess.ChessColor
import com.monadx.othello.chess.Coordinate
import com.monadx.othello.ui.AppState
import com.monadx.othello.ui.components.board.GameBoard
import com.monadx.othello.ui.components.board.GameBoardPiece

class VersusController: GamingController() {
    override val gameBoard = GameBoard(this)

    override val board = Board()

    init {
        super.syncBoardColor()
        super.setBoardPlaceable()
    }

    @Composable
    override fun view(state: AppState) {
        MaterialTheme {
            gameBoard.render()
        }
    }

    override fun onClick(piece: GameBoardPiece, x: Int, y: Int) {
        if (!board.place(Coordinate(x, y))) {
            return
        }

        super.syncBoardColor()
        super.setBoardPlaceable()
        println("VersusController.onClick($x, $y)")
    }

    override fun isTurn() = true
}
