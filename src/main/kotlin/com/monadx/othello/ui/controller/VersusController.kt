package com.monadx.othello.ui.controller

import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf

import com.monadx.othello.chess.Coordinate
import com.monadx.othello.chess.Game
import com.monadx.othello.ui.AppState
import com.monadx.othello.ui.components.board.GameState
import com.monadx.othello.ui.components.board.UniversalBoard

class VersusController(appState: AppState): GamingController(appState) {
    override val state = GameState()

    override val game = Game()

    val isAlreadyCheated = mutableStateOf(false)

    init {
        super.syncAll()
        state.status.placeable.value = true
    }

    @Composable
    override fun view() {
        UniversalBoard(this, enableSaving = !isAlreadyCheated.value) {
            TextButton(
                onClick = {
                    state.board.isCheatMode.value = !state.board.isCheatMode.value
                    isAlreadyCheated.value = true
                },
                colors = when (state.board.isCheatMode.value) {
                    true -> ButtonDefaults.textButtonColors(
                        backgroundColor = MaterialTheme.colors.error,
                        contentColor = MaterialTheme.colors.onError,
                    )
                    false -> ButtonDefaults.textButtonColors(
                        contentColor = MaterialTheme.colors.onSurface
                    )
                },
            ) {
                Text(if (state.board.isCheatMode.value) "Cheating" else "Cheat")
            }
        }
    }

    override fun onClick(x: Int, y: Int) {
        if (state.board.isCheatMode.value) {
            game.board.forceFlip(Coordinate(x, y))
            println("Cheating flip: $x, $y")
        } else {
            if (!game.place(Coordinate(x, y))) {
                return
            }
            println("VersusController.onClick($x, $y)")
        }

        super.syncAll()
    }

    override fun undo() {
        game.undo()
        super.syncAll()
    }

    override fun restart() {
        isAlreadyCheated.value = false
        game.reset()
        super.syncAll()
    }
}
