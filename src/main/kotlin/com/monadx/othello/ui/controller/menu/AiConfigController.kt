package com.monadx.othello.ui.controller.menu

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import org.apache.logging.log4j.LogManager

import com.monadx.othello.chess.ChessColor
import com.monadx.othello.ui.AppState
import com.monadx.othello.ui.components.menu.AiConfigDialog
import com.monadx.othello.ui.controller.AiController
import com.monadx.othello.ui.controller.Controller
import com.monadx.othello.ui.controller.MenuController
import com.monadx.othello.ui.model.Difficulties

class AiConfigController(appState: AppState) : Controller(appState) {
    companion object {
        private val LOGGER = LogManager.getLogger(AiConfigController::class.java)
    }

    var color = mutableStateOf(ChessColor.BLACK)
    val difficulties: MutableState<Difficulties?> = mutableStateOf(null)

    @Composable
    override fun view() {
        AiConfigDialog(
            this,
            onConfirm = {
                LOGGER.info("Create AiController with difficulty: ${difficulties.value!!.displayText}")
                appState.setController(AiController(appState, color.value, difficulties.value!!.createDifficulty()))
            },
            onCloseRequest = { (appState.getController() as MenuController).closeDialog() }
        )
    }
}
