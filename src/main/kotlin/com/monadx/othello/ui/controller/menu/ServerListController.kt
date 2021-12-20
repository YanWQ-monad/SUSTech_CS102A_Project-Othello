package com.monadx.othello.ui.controller.menu

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import org.apache.logging.log4j.LogManager
import java.io.IOException

import com.monadx.othello.network.Constant
import com.monadx.othello.network.broadcast.MulticastClient
import com.monadx.othello.network.connection.Channel
import com.monadx.othello.network.connection.connection.ClientHandshakeConnection
import com.monadx.othello.network.connection.connection.GameConnection
import com.monadx.othello.network.packet.PacketStream
import com.monadx.othello.ui.AppState
import com.monadx.othello.ui.components.menu.ServerListDialog
import com.monadx.othello.ui.controller.Controller
import com.monadx.othello.ui.controller.MenuController
import com.monadx.othello.ui.controller.OnlineController

class ServerListController(appState: AppState) : Controller(appState) {
    companion object {
        private val LOGGER = LogManager.getLogger(ServerListController::class.java)
    }

    val multicastClient: MulticastClient
    val serverList = mutableStateListOf<MulticastClient.Packet>()

    init {
        multicastClient = MulticastClient(Constant.MULTICAST_ADDRESS, Constant.MULTICAST_PORT) { packet ->
            if (!serverList.contains(packet)) {
                LOGGER.debug("Received new packet: $packet")
                serverList.add(packet)
            }
        }
    }

    @Composable
    override fun view() {
        ServerListDialog(
            serverList,
            onChoose = { packet, password ->
                LOGGER.info("Connecting to ${packet.message.serverName}(${packet.sender.hostAddress})")

                try {
                    val channel = Channel.connect(packet.sender, packet.message.port).withCache()
                    val packetStream = PacketStream(channel)
                    val connection = ClientHandshakeConnection(packetStream)
                    val isSuccess = connection.runUntilComplete(password)
                    if (!isSuccess) {
                        LOGGER.error("Server handshake failed")
                        packetStream.close()
                        return@ServerListDialog
                    }

                    (appState.getController() as MenuController).closeDialog()
                    val gameConnection = GameConnection(connection.stream)
                    appState.setController(OnlineController(appState, null, gameConnection, null))
                } catch (e: IOException) {
                    LOGGER.error("Failed to connect to ${packet.message.serverName}(${packet.sender.hostAddress})", e)
                    serverList.remove(packet)
                }
            },
            onCloseRequest = { (appState.getController() as MenuController).closeDialog() },
        )
    }

    override fun onClose() {
        super.onClose()
        multicastClient.close()
    }
}
