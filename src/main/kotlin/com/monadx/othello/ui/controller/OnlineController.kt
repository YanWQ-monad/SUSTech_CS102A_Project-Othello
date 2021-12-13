package com.monadx.othello.ui.controller

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import org.apache.logging.log4j.LogManager

import com.monadx.othello.chess.ChessColor
import com.monadx.othello.chess.Coordinate
import com.monadx.othello.chess.Game
import com.monadx.othello.network.connection.Server
import com.monadx.othello.network.connection.connection.GameConnection
import com.monadx.othello.network.packet.game.ChessPlacePacket
import com.monadx.othello.network.packet.game.GamePacketListener
import com.monadx.othello.network.packet.game.GameStartPacket
import com.monadx.othello.network.request.RequestListener
import com.monadx.othello.network.request.RequestManager
import com.monadx.othello.ui.AppState
import com.monadx.othello.ui.components.ConfirmBox
import com.monadx.othello.ui.components.board.GameState
import com.monadx.othello.ui.components.board.UniversalBoard

class OnlineController(
    appState: AppState,
    val server: Server?,
    val connection: GameConnection,
    var playerColor: ChessColor?,
    ) : GamingController(appState)
{
    companion object {
        private val LOGGER = LogManager.getLogger(OnlineController::class.java)
    }

    override val state = GameState()
    override val game = Game()

    val isServer = server != null
    var stage = Stage.PREPARE
    val requestManager: RequestManager

    val confirmBoxMessage: MutableState<String?> = mutableStateOf(null)
    var confirmBoxCallback: ((Boolean) -> Unit)? = null

    init {
        state.status.placable.value = false

        doStart()

        requestManager = RequestManager(connection, object: RequestListener {
            override fun onUndoRequest(onResponse: RequestManager.RequestResultConsumer) {
                showConfirmBox("Opponent want to undo.") { result ->
                    onResponse.accept(result)
                    if (result) {
                        doUndo(playerColor!!.opposite)
                    }
                }
            }

            override fun onRestartRequest(onResponse: RequestManager.RequestResultConsumer) {
                showConfirmBox("Opponent want to restart.") { result ->
                    onResponse.accept(result)
                    if (result) {
                        doStart()
                    }
                }
            }
        })

        connection.listen(object: GamePacketListener {
            override fun handleGameStart(packet: GameStartPacket) {
                if (isServer) {
                    LOGGER.warn("GameStartPacket: should not be received by server")
                } else {
                    playerColor = packet.color
                    verifyBoardHash(packet.boardHash)
                    stage = Stage.PLAYING
                    syncAll()
                    nextStep()
                }
            }

            override fun handleChessPlace(packet: ChessPlacePacket) {
                if (game.currentPlayer != packet.color) {
                    LOGGER.warn("ChessPlacePacket: ${packet.color} is not current player")
                } else {
                    LOGGER.info("Remote ${packet.color} place: ${packet.x}, ${packet.y}")
                    game.place(Coordinate(packet.x, packet.y))
                    verifyBoardHash(packet.boardHash)
                    syncAll()
                    nextStep()
                }
            }
        })
    }

    @Composable
    override fun view() {
        confirmBoxMessage.value?.let {
            ConfirmBox(it) { result ->
                confirmBoxMessage.value = null
                confirmBoxCallback?.let { it(result) }
            }
        }
        UniversalBoard(this)
    }

    override fun onClick(x: Int, y: Int) {
        val color = game.currentPlayer
        if (!game.place(Coordinate(x, y))) {
            return
        }

        LOGGER.info("Local $color place: $x, $y")
        connection.send(ChessPlacePacket(x, y, color, getBoardHash()))

        syncAll()
        nextStep()
    }

    override fun undo() {
        requestManager.sendUndoRequest { result -> if (result) doUndo(playerColor!!) }
    }

    override fun restart() {
        requestManager.sendRestartRequest { result -> if (result) doStart() }
    }

    fun doUndo(color: ChessColor) {
        LOGGER.info("$color undo")
        game.undo()
        while (game.currentPlayer != color && game.placedCount > 4) {
            game.undo()
        }
        syncAll()
        nextStep()
    }

    fun doStart() {
        game.reset()
        state.status.placable.value = false
        stage = Stage.PREPARE

        if (isServer) {
            connection.send(GameStartPacket(playerColor!!.opposite, getBoardHash()))
            stage = Stage.PLAYING
            syncAll()
            nextStep()
        } else {
            syncAll()
        }
    }

    fun verifyBoardHash(boardHash: Int) {
        if (getBoardHash() != boardHash) {
            LOGGER.warn("Board hash is not correct: ${getBoardHash()} != $boardHash")
        }
    }

    fun getBoardHash() = game.board.hashCode()

    fun nextStep() {
        if (game.status == Game.Status.ENDED) {
            state.status.placable.value = false
            return
        }

        state.status.placable.value = game.currentPlayer == playerColor
    }

    fun showConfirmBox(message: String, callback: (Boolean) -> Unit) {
        confirmBoxMessage.value = message
        confirmBoxCallback = callback
    }

    enum class Stage {
        PREPARE,
        PLAYING,
        ENDED,
    }
}