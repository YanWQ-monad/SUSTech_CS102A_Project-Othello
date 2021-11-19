package com.monadx.othello.ui.controller

import com.monadx.othello.chess.Board
import com.monadx.othello.chess.Utils.POSITION_LIST
import com.monadx.othello.ui.components.board.GameBoard
import com.monadx.othello.ui.components.board.GameBoardPiece

abstract class GamingController: Controller() {
    abstract val gameBoard: GameBoard

    abstract val board: Board

    abstract fun onClick(piece: GameBoardPiece, x: Int, y: Int)

    abstract fun isTurn(): Boolean

    fun syncBoardColor() {
        POSITION_LIST.forEach { (x, y) ->
            if (gameBoard.pieceAt(x, y).color.value != board.board[x][y]) {
                gameBoard.pieceAt(x, y).color.value = board.board[x][y]
            }
        }
    }

    fun setBoardPlaceable() {
        POSITION_LIST.forEach { coordinate ->
            val (x, y) = coordinate

            gameBoard.pieceAt(x, y).canMove.value = board.checkPlaceable(coordinate)
        }
    }
}
