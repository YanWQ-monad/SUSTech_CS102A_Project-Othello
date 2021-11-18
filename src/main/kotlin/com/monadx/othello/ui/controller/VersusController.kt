package com.monadx.othello.ui.controller

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf

import com.monadx.othello.chess.ChessColor
import com.monadx.othello.ui.AppState
import com.monadx.othello.ui.components.board.GameBoard
import com.monadx.othello.ui.components.board.GameBoardPiece

class VersusController: GamingController() {
    val gameBoard = GameBoard(this)

    val isTurn = mutableStateOf(false)

    @Composable
    override fun view(state: AppState) {
        MaterialTheme {
            gameBoard.render()
        }
    }

    override fun onClick(piece: GameBoardPiece, x: Int, y: Int) {
        piece.color.value = if (piece.color.value == ChessColor.EMPTY) ChessColor.BLACK else piece.color.value.opposite
        println("VersusController.onClick($x, $y)")
    }

    override fun isTurn() = isTurn.value
}
