package com.monadx.othello.ui.controller

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import kotlin.concurrent.thread

import com.monadx.othello.ai.evaluate.GreedyEvaluator
import com.monadx.othello.ai.Searcher
import com.monadx.othello.chess.ChessColor
import com.monadx.othello.chess.Coordinate
import com.monadx.othello.chess.Game
import com.monadx.othello.ui.AppState
import com.monadx.othello.ui.components.board.GameBoardState
import com.monadx.othello.ui.components.board.GameStatusState
import com.monadx.othello.ui.components.board.UniversalBoard

class AiController: GamingController() {
    override val gameBoard = GameBoardState()

    override val gameStatus = GameStatusState()

    override val game = Game()

    var thread: Thread? = null

    val searcher = Searcher(GreedyEvaluator())

    init {
        syncAll()
    }

    @Composable
    override fun view(state: AppState) {
        MaterialTheme {
            UniversalBoard(this)
        }
    }

    override fun onClick(x: Int, y: Int) {
        if (!game.place(Coordinate(x, y))) {
            return
        }

        syncAll()
        println("VersusController.onClick($x, $y)")

        thread = thread {
            val oldTime = System.currentTimeMillis()

            val collector = searcher.search(game.board, game.currentPlayer, game.placedCount)
            val move = collector.best

            val timePassBy = System.currentTimeMillis() - oldTime
            if (timePassBy < 1000) {
                Thread.sleep(1000 - timePassBy)
            }

            synchronized(game) {
                if (thread!!.isInterrupted) {
                    return@thread;
                }
                game.place(move)
                println("  AI move: $move")
                println("  expected value: ${collector.score}")
                syncAll()
            }
        }
    }

    override fun syncAll() {
        super.syncAll()
        gameStatus.placable.value = (game.currentPlayer == ChessColor.BLACK)

//        val result = searcher.evaluator.evaluate(game.board)
//        println("Current evaluate: $result")

//        println("AiController.syncAll():")
//        println("current player: " + game.currentPlayer + ", placable: " + gameStatus.placable.value)
    }

    override fun undo() {
        synchronized(game) {
            thread?.interrupt()
            game.undo()
            if (game.currentPlayer != ChessColor.BLACK) {
                game.undo()
            }
            syncAll()
        }
    }

    override fun restart() {
        synchronized(game) {
            thread?.interrupt()
            game.reset()
            syncAll()
        }
    }
}
