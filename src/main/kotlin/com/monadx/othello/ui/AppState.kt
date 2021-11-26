package com.monadx.othello.ui

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf

import com.monadx.othello.ui.controller.Controller
import com.monadx.othello.ui.controller.MenuController

class AppState {
    private var controller: MutableState<Controller> = mutableStateOf(MenuController(this))

    fun getController(): Controller {
        return controller.value
    }

    fun setController(controller: Controller) {
        this.controller.value = controller
    }

    fun goBackMenu() {
        setController(MenuController(this))
    }
}
