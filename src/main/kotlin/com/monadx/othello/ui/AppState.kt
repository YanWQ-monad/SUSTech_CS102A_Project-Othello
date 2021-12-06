package com.monadx.othello.ui

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import org.apache.logging.log4j.LogManager

import com.monadx.othello.ui.controller.Controller
import com.monadx.othello.ui.controller.MenuController

class AppState {
    companion object {
        private val LOGGER = LogManager.getLogger(AppState::class.java)
    }

    private var controller: MutableState<Controller> = mutableStateOf(MenuController(this))

    fun getController(): Controller {
        return controller.value
    }

    fun setController(controller: Controller) {
        this.controller.value.onClose()
        LOGGER.info("Switch to controller: ${controller.javaClass.simpleName}")
        this.controller.value = controller
    }

    fun goBackMenu() {
        setController(MenuController(this))
    }
}
