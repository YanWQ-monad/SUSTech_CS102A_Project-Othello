package com.monadx.othello.ui.controller

import com.monadx.othello.chess.Board
import com.monadx.othello.chess.ChessColor
import com.monadx.othello.chess.Utils.POSITION_LIST
import com.monadx.othello.ui.components.board.GameBoardState
import com.monadx.othello.ui.components.board.GameStatusState
import com.monadx.othello.ui.components.board.PlayerState

abstract class GamingController: Controller() {
    abstract val gameBoard: GameBoardState

    abstract val gameStatus: GameStatusState

    abstract val board: Board

    abstract fun onClick(x: Int, y: Int)

    fun syncBoardColor() {
        POSITION_LIST.forEach { (x, y) ->
            if (gameBoard.at(x, y).color.value != board.board[x][y]) {
                gameBoard.at(x, y).color.value = board.board[x][y]
            }
        }
    }

    fun syncStatus() {
        var black = 0
        var white = 0

        POSITION_LIST.forEach { (x, y) ->
            if (board.board[x][y] == ChessColor.BLACK) {
                black++
            } else if (board.board[x][y] == ChessColor.WHITE) {
                white++
            }
        }

        gameStatus.black.score.value = black
        gameStatus.white.score.value = white

        if (board.currentPlayer == ChessColor.BLACK) {
            gameStatus.black.status.value = PlayerState.Status.PLAYING
            gameStatus.white.status.value = PlayerState.Status.IDLE
        } else {
            gameStatus.black.status.value = PlayerState.Status.IDLE
            gameStatus.white.status.value = PlayerState.Status.PLAYING
        }
    }

    fun setBoardPlaceable() {
        POSITION_LIST.forEach { coordinate ->
            val (x, y) = coordinate

            gameBoard.at(x, y).canMove.value = board.checkPlaceable(coordinate)
        }
    }
}
