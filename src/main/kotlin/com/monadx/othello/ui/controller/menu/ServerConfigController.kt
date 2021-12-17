package com.monadx.othello.ui.controller.menu

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf

import com.monadx.othello.chess.ChessColor
import com.monadx.othello.ui.AppState
import com.monadx.othello.ui.components.menu.ServerConfigDialog
import com.monadx.othello.ui.controller.Controller
import com.monadx.othello.ui.controller.MenuController

class ServerConfigController(appState: AppState) : Controller(appState) {
    val serverName = mutableStateOf("")
    val color = mutableStateOf(ChessColor.BLACK)

    @Composable
    override fun view() {
        ServerConfigDialog(
            serverName.value,
            onServerNameChange = { newName -> serverName.value = newName },
            color = color.value,
            onColorChange = { newColor -> color.value = newColor },
            onConfirm = {
                (appState.getController() as MenuController).setDialog(ServerListeningController(appState, serverName.value, color.value))
            },
            onCloseRequest = { (appState.getController() as MenuController).closeDialog() }
        )
    }
}
