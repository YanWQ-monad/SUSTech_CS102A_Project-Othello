package com.monadx.othello.ui.controller

import androidx.compose.runtime.Composable

import com.monadx.othello.chess.Coordinate
import com.monadx.othello.chess.Game
import com.monadx.othello.ui.AppState
import com.monadx.othello.ui.components.board.GameState
import com.monadx.othello.ui.components.board.UniversalBoard

class VersusController(appState: AppState): GamingController(appState) {
    override val state = GameState()

    override val game = Game()

    init {
        super.syncAll()
        state.status.placable.value = true
    }

    @Composable
    override fun view() {
        UniversalBoard(this)
    }

    override fun onClick(x: Int, y: Int) {
        if (!game.place(Coordinate(x, y))) {
            return
        }

        super.syncAll()
        println("VersusController.onClick($x, $y)")
    }

    override fun undo() {
        game.undo()
        super.syncAll()
    }

    override fun restart() {
        game.reset()
        super.syncAll()
    }
}
