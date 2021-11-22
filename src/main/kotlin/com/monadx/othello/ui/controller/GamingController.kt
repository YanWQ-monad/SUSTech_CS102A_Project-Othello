package com.monadx.othello.ui.controller

import com.monadx.othello.chess.ChessColor
import com.monadx.othello.chess.Game
import com.monadx.othello.chess.GameStatus
import com.monadx.othello.chess.Utils.POSITION_LIST
import com.monadx.othello.ui.components.board.GameState
import com.monadx.othello.ui.components.board.PlayerState

abstract class GamingController: Controller() {
    abstract val state: GameState

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
            if (state.board.at(x, y).color.value != game.board.board[x][y]) {
                state.board.at(x, y).color.value = game.board.board[x][y]
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

        state.status.black.score.value = black
        state.status.white.score.value = white

        if (game.status == GameStatus.PLAYING) {
            state.status.black.status.value = if (game.currentPlayer == ChessColor.BLACK) PlayerState.Status.PLAYING else PlayerState.Status.IDLE
            state.status.white.status.value = if (game.currentPlayer == ChessColor.WHITE) PlayerState.Status.PLAYING else PlayerState.Status.IDLE
        } else {
            state.status.black.status.value = if (game.winner == ChessColor.BLACK) PlayerState.Status.WIN else PlayerState.Status.IDLE
            state.status.white.status.value = if (game.winner == ChessColor.WHITE) PlayerState.Status.WIN else PlayerState.Status.IDLE
        }
    }

    fun setBoardPlaceable() {
        POSITION_LIST.forEach { coordinate ->
            val (x, y) = coordinate

            state.board.at(x, y).canMove.value = game.checkPlaceable(coordinate)
        }
    }
}
