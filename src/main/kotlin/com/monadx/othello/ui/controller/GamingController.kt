package com.monadx.othello.ui.controller

import com.monadx.othello.chess.ChessColor
import com.monadx.othello.chess.Game
import com.monadx.othello.chess.GameStatus
import com.monadx.othello.chess.Utils.POSITION_LIST
import com.monadx.othello.ui.components.board.GameBoardState
import com.monadx.othello.ui.components.board.GameStatusState
import com.monadx.othello.ui.components.board.PlayerState

abstract class GamingController: Controller() {
    abstract val gameBoard: GameBoardState

    abstract val gameStatus: GameStatusState

    abstract val game: Game

    abstract fun onClick(x: Int, y: Int)

    abstract fun undo()

    abstract fun restart()

    fun syncAll() {
        syncBoardColor()
        syncStatus()
        setBoardPlaceable()
    }

    fun syncBoardColor() {
        POSITION_LIST.forEach { (x, y) ->
            if (gameBoard.at(x, y).color.value != game.board.board[x][y]) {
                gameBoard.at(x, y).color.value = game.board.board[x][y]
            }
        }
    }

    fun syncStatus() {
        var black = 0
        var white = 0

        POSITION_LIST.forEach { (x, y) ->
            if (game.board.board[x][y] == ChessColor.BLACK) {
                black++
            } else if (game.board.board[x][y] == ChessColor.WHITE) {
                white++
            }
        }

        gameStatus.black.score.value = black
        gameStatus.white.score.value = white

        if (game.status == GameStatus.PLAYING) {
            if (game.currentPlayer == ChessColor.BLACK) {
                gameStatus.black.status.value = PlayerState.Status.PLAYING
                gameStatus.white.status.value = PlayerState.Status.IDLE
            } else {
                gameStatus.black.status.value = PlayerState.Status.IDLE
                gameStatus.white.status.value = PlayerState.Status.PLAYING
            }
        } else {
            if (game.winner == ChessColor.BLACK) {
                gameStatus.black.status.value = PlayerState.Status.WIN
                gameStatus.white.status.value = PlayerState.Status.IDLE
            } else if (game.winner == ChessColor.WHITE) {
                gameStatus.black.status.value = PlayerState.Status.IDLE
                gameStatus.white.status.value = PlayerState.Status.WIN
            } else {
                gameStatus.black.status.value = PlayerState.Status.IDLE
                gameStatus.white.status.value = PlayerState.Status.IDLE
            }
        }
    }

    fun setBoardPlaceable() {
        POSITION_LIST.forEach { coordinate ->
            val (x, y) = coordinate

            gameBoard.at(x, y).canMove.value = game.checkPlaceable(coordinate)
        }
    }
}
