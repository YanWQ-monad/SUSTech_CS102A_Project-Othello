package com.monadx.othello.ui.controller.menu

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf

import com.monadx.othello.chess.ChessColor
import com.monadx.othello.ui.AppState
import com.monadx.othello.ui.components.menu.ChessChooseDialog
import com.monadx.othello.ui.controller.AiController
import com.monadx.othello.ui.controller.Controller
import com.monadx.othello.ui.controller.MenuController

class ChessChooseController(appState: AppState) : Controller(appState) {
    var color = mutableStateOf(ChessColor.BLACK)

    @Composable
    override fun view() {
        ChessChooseDialog(
            this,
            onConfirm = {
                appState.setController(AiController(appState, color.value))
            },
            onCloseRequest = { (appState.getController() as MenuController).closeDialog() }
        )
    }
}
