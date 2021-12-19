package com.monadx.othello.ui.controller.menu

import androidx.compose.runtime.Composable
import org.apache.logging.log4j.LogManager
import java.io.IOException
import kotlin.concurrent.thread
import kotlin.random.Random

import com.monadx.othello.chess.ChessColor
import com.monadx.othello.network.Constant
import com.monadx.othello.network.broadcast.MulticastMessage
import com.monadx.othello.network.broadcast.MulticastServer
import com.monadx.othello.network.connection.Server
import com.monadx.othello.network.connection.connection.GameConnection
import com.monadx.othello.network.connection.connection.ServerHandshakeConnection
import com.monadx.othello.network.packet.PacketStream
import com.monadx.othello.ui.AppState
import com.monadx.othello.ui.components.menu.ServerListeningDialog
import com.monadx.othello.ui.controller.Controller
import com.monadx.othello.ui.controller.MenuController
import com.monadx.othello.ui.controller.OnlineController

class ServerListeningController(appState: AppState, val serverName: String, val chessColor: ChessColor) : Controller(appState) {
    companion object {
        private val LOGGER = LogManager.getLogger(ServerListeningController::class.java)
    }

    val multicastServer: MulticastServer
    val server: Server

    var skipServerClose = false

    init {
        val serverPort = Random.nextInt(10000, 40000)
        val message = MulticastMessage(serverPort, serverName, chessColor)
        multicastServer = MulticastServer(Constant.MULTICAST_ADDRESS, Constant.MULTICAST_PORT, message)
        server = Server(serverPort)

        thread(isDaemon = true) {
            while (!server.isClosed) {
                try {
                    val channel = server.waitForConnection()?.withCache() ?: continue
                    val packetStream = PacketStream(channel)
                    val connection = ServerHandshakeConnection(packetStream)
                    val isSuccess = connection.runUntilComplete("")
                    if (!isSuccess) {
                        LOGGER.error("Server handshake failed")
                        continue
                    }

                    skipServerClose = true
                    (appState.getController() as MenuController).closeDialog()
                    val gameConnection = GameConnection(connection.stream)
                    appState.setController(OnlineController(appState, server, gameConnection, chessColor))
                } catch (e: IOException) {
                    LOGGER.error("Error while waiting for connection", e)
                    continue
                }
                break
            }
        }
    }

    @Composable
    override fun view() {
        ServerListeningDialog(onCloseRequest = { (appState.getController() as MenuController).closeDialog() })
    }

    override fun onClose() {
        super.onClose()
        multicastServer.close()

        if (!skipServerClose) {
            server.close()
        }
    }
}
