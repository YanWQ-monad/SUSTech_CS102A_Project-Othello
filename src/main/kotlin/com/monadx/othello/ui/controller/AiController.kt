package com.monadx.othello.ui.controller

import androidx.compose.runtime.Composable
import kotlin.concurrent.thread

import com.monadx.othello.ai.difficulty.Difficulty
import com.monadx.othello.chess.ChessColor
import com.monadx.othello.chess.Coordinate
import com.monadx.othello.chess.Game
import com.monadx.othello.ui.AppState
import com.monadx.othello.ui.components.board.GameState
import com.monadx.othello.ui.components.board.UniversalBoard

class AiController(appState: AppState, val playerColor: ChessColor, val difficulty: Difficulty): GamingController(appState) {
    override val state = GameState()

    override val game = Game()

    var aiThread: Thread? = null

    init {
        syncAll()
        nextStep()
    }

    @Composable
    override fun view() {
        UniversalBoard(this)
    }

    override fun onClick(x: Int, y: Int) {
        if (!game.place(Coordinate(x, y))) {
            return
        }

        syncAll()
        println("VersusController.onClick($x, $y)")

        nextStep()
    }

    fun nextStep() {
        if (game.status == Game.Status.ENDED) {
            state.status.placeable.value = false
            return
        }

        if (game.currentPlayer == playerColor) {
            state.status.placeable.value = true
        } else {
            state.status.placeable.value = false

            aiThread = thread {
                val oldTime = System.currentTimeMillis()

                val result = difficulty.searchBestMove(game.board, game.currentPlayer!!, game.placedCount)
                val move = result.best

                val timePassBy = System.currentTimeMillis() - oldTime
                if (timePassBy < 500) {
                    Thread.sleep(500 - timePassBy)
                }

                synchronized(game) {
                    if (aiThread!!.isInterrupted) {
                        return@thread
                    }
                    game.place(move)
                    println("  AI move: $move")
                    println("  expected value: ${result.score}")
                    syncAll()
                    nextStep()
                }
            }
        }
    }

    override fun undo() {
        synchronized(game) {
            aiThread?.interrupt()
            game.undo()
            while (game.currentPlayer != playerColor) {
                game.undo()
            }
            syncAll()
            nextStep()
        }
    }

    override fun restart() {
        synchronized(game) {
            aiThread?.interrupt()
            game.reset()
            syncAll()
            nextStep()
        }
    }

    override fun save() {
        synchronized(game) {
            super.save()
        }
    }

    override fun load() {
        synchronized(game) {
            aiThread?.interrupt()
            super.load()
            nextStep()
        }
    }
}
