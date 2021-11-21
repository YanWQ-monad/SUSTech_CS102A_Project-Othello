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

    open fun syncAll() {
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
            gameStatus.black.status.value = if (game.currentPlayer == ChessColor.BLACK) PlayerState.Status.PLAYING else PlayerState.Status.IDLE
            gameStatus.white.status.value = if (game.currentPlayer == ChessColor.WHITE) PlayerState.Status.PLAYING else PlayerState.Status.IDLE
        } else {
            gameStatus.black.status.value = if (game.winner == ChessColor.BLACK) PlayerState.Status.WIN else PlayerState.Status.IDLE
            gameStatus.white.status.value = if (game.winner == ChessColor.WHITE) PlayerState.Status.WIN else PlayerState.Status.IDLE
        }
    }

    fun setBoardPlaceable() {
        POSITION_LIST.forEach { coordinate ->
            val (x, y) = coordinate

            gameBoard.at(x, y).canMove.value = game.checkPlaceable(coordinate)
        }
    }
}
