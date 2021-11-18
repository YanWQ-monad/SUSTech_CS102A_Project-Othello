package com.monadx.othello

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.monadx.othello.ui.AppState

class App {
    private val state by mutableStateOf(AppState())

    fun start() {
        application {
            Window(onCloseRequest = ::exitApplication) {
                state.getController().view(state)
            }
        }
    }
}
