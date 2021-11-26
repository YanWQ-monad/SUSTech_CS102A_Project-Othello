package com.monadx.othello.ui.controller

import com.monadx.othello.chess.ChessColor
import com.monadx.othello.chess.Game
import com.monadx.othello.chess.Utils.POSITION_LIST
import com.monadx.othello.save.RecordLoader
import com.monadx.othello.save.RecordSaver
import com.monadx.othello.save.SaveException
import com.monadx.othello.save.FileChooser
import com.monadx.othello.ui.AppState
import com.monadx.othello.ui.components.board.GameState
import com.monadx.othello.ui.components.board.PlayerState

abstract class GamingController(appState: AppState): Controller(appState) {
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

        if (game.status == Game.Status.PLAYING) {
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

    open fun save() {
        val filename = FileChooser(FileChooser.Action.SAVE, ".dat").choose()
        println(filename)
        if (filename === null) {
            return
        }

        val saver = RecordSaver(filename)
        saver.save(game)
    }

    open fun load() {
        val filename = FileChooser(FileChooser.Action.OPEN, ".dat").choose()
        println(filename)
        if (filename === null) {
            return
        }

        val loader = RecordLoader(filename)
        try {
            val game = loader.load()
            this.game.loadFrom(game)
            syncAll()
        } catch (e: SaveException) {
            println(e.message)
        }
    }
}
