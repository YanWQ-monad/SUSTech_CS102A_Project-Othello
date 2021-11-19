package com.monadx.othello.ui.controller

import com.monadx.othello.chess.Board
import com.monadx.othello.chess.Utils.POSITION_LIST
import com.monadx.othello.ui.components.board.GameBoardState

abstract class GamingController: Controller() {
    abstract val gameBoard: GameBoardState

    abstract val board: Board

    abstract fun onClick(x: Int, y: Int)

    fun syncBoardColor() {
        POSITION_LIST.forEach { (x, y) ->
            if (gameBoard.at(x, y).color.value != board.board[x][y]) {
                gameBoard.at(x, y).color.value = board.board[x][y]
            }
        }
    }

    fun setBoardPlaceable() {
        POSITION_LIST.forEach { coordinate ->
            val (x, y) = coordinate

            gameBoard.at(x, y).canMove.value = board.checkPlaceable(coordinate)
        }
    }
}
