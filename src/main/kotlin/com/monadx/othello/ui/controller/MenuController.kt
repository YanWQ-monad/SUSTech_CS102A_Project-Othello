package com.monadx.othello.ui.controller

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.apache.logging.log4j.LogManager

import com.monadx.othello.ui.AppState
import com.monadx.othello.ui.controller.menu.ChessChooseController
import com.monadx.othello.ui.controller.menu.ServerConfigController
import com.monadx.othello.ui.controller.menu.ServerListController

class MenuController(appState: AppState): Controller(appState) {
    companion object {
        private val LOGGER = LogManager.getLogger(MenuController::class.java)
    }

    var dialogController: MutableState<Controller?> = mutableStateOf(null)

    @Composable
    override fun view() {
        MaterialTheme {
            Box(
                Modifier
                    .width(160.dp)
                    .height(320.dp)
            ) {
                Column {
                    Button(onClick = { appState.setController(VersusController(appState)) }) {
                        Text("Versus Mode")
                    }
                    Button(onClick = { setDialog(ChessChooseController(appState)) }) {
                        Text("AI Mode")
                    }
                    Button(onClick = { setDialog(ServerConfigController(appState)) }) {
                        Text("Start Server")
                    }
                    Button(onClick = { setDialog(ServerListController(appState)) }) {
                        Text("Join Server")
                    }
                }
                dialogController.value?.view()
            }
        }
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
