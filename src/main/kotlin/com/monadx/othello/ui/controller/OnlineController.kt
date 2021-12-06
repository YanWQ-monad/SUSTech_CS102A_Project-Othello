package com.monadx.othello.ui.controller

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import org.apache.logging.log4j.LogManager

import com.monadx.othello.chess.ChessColor
import com.monadx.othello.chess.Coordinate
import com.monadx.othello.chess.Game
import com.monadx.othello.network.connection.Server
import com.monadx.othello.network.connection.connection.GameConnection
import com.monadx.othello.network.packet.game.ChessPlacePacket
import com.monadx.othello.network.packet.game.GamePacketListener
import com.monadx.othello.network.packet.game.GameStartPacket
import com.monadx.othello.ui.AppState
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

    init {
        state.status.placable.value = false

        if (isServer) {
            connection.send(GameStartPacket(playerColor!!.opposite, getBoardHash()))
            stage = Stage.PLAYING
            syncAll()
            nextStep()
        }

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
        MaterialTheme {
            UniversalBoard(this)
        }
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
        TODO("Not yet implemented")
    }

    override fun restart() {
        TODO("Not yet implemented")
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

    enum class Stage {
        PREPARE,
        PLAYING,
        ENDED,
    }
}