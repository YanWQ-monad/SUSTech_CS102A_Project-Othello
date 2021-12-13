package com.monadx.othello.ui.controller

import androidx.compose.runtime.Composable
import org.apache.logging.log4j.LogManager

import com.monadx.othello.chess.Board
import com.monadx.othello.chess.ChessColor
import com.monadx.othello.chess.Game
import com.monadx.othello.chess.Snapshot
import com.monadx.othello.chess.Utils.POSITION_LIST
import com.monadx.othello.save.FileChooser
import com.monadx.othello.save.RecordLoader
import com.monadx.othello.save.SaveException
import com.monadx.othello.ui.AppState
import com.monadx.othello.ui.components.board.GameState
import com.monadx.othello.ui.components.board.PlayerState
import com.monadx.othello.ui.components.board.ReplayBoard

class ReplayController(appState: AppState) : Controller(appState) {
    companion object {
        private val LOGGER = LogManager.getLogger(ReplayController::class.java)
    }

    val state = GameState()

    private var snapshotListToLoad: MutableList<Snapshot>? = null
    val snapshotList by lazy { snapshotListToLoad!! }

    var chosenSnapshot: Snapshot? = null
    val board: Board get() = chosenSnapshot!!.board
    val currentPlayer: ChessColor? get() = chosenSnapshot!!.currentPlayer

    val loaded: Boolean get() = snapshotListToLoad != null

    init {
        val filename = FileChooser(FileChooser.Action.OPEN, ".dat").choose()
        LOGGER.info("Loading replay from $filename")
        if (filename !== null) {
            val loader = RecordLoader(filename)
            try {
                val game = loader.load()
                snapshotListToLoad = game.snapshotList
                snapshotListToLoad!!.add(Snapshot(game.board, game.currentPlayer, game.status))

                chosenSnapshot = snapshotList.first()
                syncAll()
            } catch (e: SaveException) {
                LOGGER.error(e.message, e)
            }
        }
    }

    @Composable
    override fun view() {
        ReplayBoard(this)
    }

    fun previousStep() {
        val index = snapshotList.indexOf(chosenSnapshot)
        if (index > 0) {
            chosenSnapshot = snapshotList[index - 1]
            syncAll()
        }
    }

    fun nextStep() {
        val index = snapshotList.indexOf(chosenSnapshot)
        if (index + 1 < snapshotList.size) {
            chosenSnapshot = snapshotList[index + 1]
            syncAll()
        }
    }

    fun syncAll() {
        POSITION_LIST.forEach { coordinate ->
            val (x, y) = coordinate
            currentPlayer?.let {
                state.board.at(x, y).canMove.value = board.checkPlaceable(coordinate, it)
            }
            if (state.board.at(x, y).color.value != board.board[x][y]) {
                state.board.at(x, y).color.value = board.board[x][y]
            }
        }

        var black = 0
        var white = 0

        POSITION_LIST.forEach { (x, y) ->
            when (board.board[x][y]!!) {
                ChessColor.BLACK -> black++
                ChessColor.WHITE -> white++
                ChessColor.EMPTY -> {}
            }
        }

        state.status.black.score.value = black
        state.status.white.score.value = white

        if (chosenSnapshot!!.status == Game.Status.PLAYING) {
            state.status.black.status.value = if (currentPlayer == ChessColor.BLACK) PlayerState.Status.PLAYING else PlayerState.Status.IDLE
            state.status.white.status.value = if (currentPlayer == ChessColor.WHITE) PlayerState.Status.PLAYING else PlayerState.Status.IDLE
        } else {
            val winner = if (black > white) ChessColor.BLACK else if (white > black) ChessColor.WHITE else null
            state.status.black.status.value = if (winner == ChessColor.BLACK) PlayerState.Status.WIN else PlayerState.Status.IDLE
            state.status.white.status.value = if (winner == ChessColor.WHITE) PlayerState.Status.WIN else PlayerState.Status.IDLE
        }
    }
}
