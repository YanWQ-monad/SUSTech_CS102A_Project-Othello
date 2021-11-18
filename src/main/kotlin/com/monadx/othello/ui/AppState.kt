package com.monadx.othello.ui

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf

import com.monadx.othello.ui.controller.Controller

class AppState {
    private var controller: MutableState<Controller> = mutableStateOf(Stage.MENU.getController())

    fun getController(): Controller {
        return controller.value
    }

    fun setStage(stage: Stage) {
        controller.value = stage.getController()
    }
}
