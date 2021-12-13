package com.monadx.othello.ui.controller

import androidx.compose.runtime.Composable
import kotlin.concurrent.thread

import com.monadx.othello.ai.evaluate.HeuristicEvaluator
import com.monadx.othello.ai.Searcher
import com.monadx.othello.chess.ChessColor
import com.monadx.othello.chess.Coordinate
import com.monadx.othello.chess.Game
import com.monadx.othello.ui.AppState
import com.monadx.othello.ui.components.board.GameState
import com.monadx.othello.ui.components.board.UniversalBoard

class AiController(appState: AppState, val playerColor: ChessColor): GamingController(appState) {
    override val state = GameState()

    override val game = Game()

    var AiThread: Thread? = null

    val searcher = Searcher(HeuristicEvaluator())

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
            state.status.placable.value = false
            return
        }

        if (game.currentPlayer == playerColor) {
            state.status.placable.value = true
        } else {
            state.status.placable.value = false

            AiThread = thread {
                val oldTime = System.currentTimeMillis()

                val collector = searcher.search(game.board, game.currentPlayer, game.placedCount)
                val move = collector.best

                val timePassBy = System.currentTimeMillis() - oldTime
                if (timePassBy < 1000) {
                    Thread.sleep(1000 - timePassBy)
                }

                synchronized(game) {
                    if (AiThread!!.isInterrupted) {
                        return@thread;
                    }
                    game.place(move)
                    println("  AI move: $move")
                    println("  expected value: ${collector.score}")
                    syncAll()
                    nextStep()
                }
            }
        }
    }

    override fun undo() {
        synchronized(game) {
            AiThread?.interrupt()
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
            AiThread?.interrupt()
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
            AiThread?.interrupt()
            super.load()
            nextStep()
        }
    }
}
