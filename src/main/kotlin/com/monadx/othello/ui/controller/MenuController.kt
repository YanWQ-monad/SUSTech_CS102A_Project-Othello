package com.monadx.othello.ui.controller

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.apache.logging.log4j.LogManager

import com.monadx.othello.ui.AppState
import com.monadx.othello.ui.controller.menu.ChessChooseController
import com.monadx.othello.ui.controller.menu.ServerConfigController
import com.monadx.othello.ui.controller.menu.ServerListController

object MenuViewConfig {
    val MENU_PADDING_TOP = 64.dp
    val MENU_TITLE_MARGIN_BOTTOM = 24.dp

    val BUTTON_WIDTH = 130.dp
}

class MenuController(appState: AppState): Controller(appState) {
    companion object {
        private val LOGGER = LogManager.getLogger(MenuController::class.java)
    }

    var dialogController: MutableState<Controller?> = mutableStateOf(null)

    @Composable
    override fun view() {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize().padding(top = MenuViewConfig.MENU_PADDING_TOP),
        ) {
            Text(
                text = "Othello",
                style = MaterialTheme.typography.displayLarge,
                color = MaterialTheme.colorScheme.onBackground,
            )

            Spacer(Modifier.height(MenuViewConfig.MENU_TITLE_MARGIN_BOTTOM))

            val buttonWidthModifier = Modifier.width(MenuViewConfig.BUTTON_WIDTH)

            Button(
                onClick = { appState.setController(VersusController(appState)) },
                modifier = buttonWidthModifier,
            ) {
                Text("Versus Mode")
            }

            Button(
                onClick = { setDialog(ChessChooseController(appState)) },
                modifier = buttonWidthModifier,
            ) {
                Text("AI Mode")
            }

            Button(
                onClick = { setDialog(ServerConfigController(appState)) },
                modifier = buttonWidthModifier,
            ) {
                Text("Start Server")
            }

            Button(
                onClick = { setDialog(ServerListController(appState)) },
                modifier = buttonWidthModifier,
            ) {
                Text("Join Server")
            }

            Button(
                onClick = {
                    val replayController = ReplayController(appState)
                    if (replayController.loaded) {
                        appState.setController(replayController)
                    }
                },
                modifier = buttonWidthModifier,
            ) {
                Text("Replay")
            }
        }
        dialogController.value?.view()
    }

    fun setDialog(controller: Controller?) {
        closeDialog()
        LOGGER.debug("Switch to menu dialog: ${controller?.javaClass?.simpleName}")
        dialogController.value = controller
    }

    fun closeDialog() {
        dialogController.value?.onClose()
        dialogController.value = null
    }

    override fun onClose() {
        dialogController.value?.onClose()
    }
}
