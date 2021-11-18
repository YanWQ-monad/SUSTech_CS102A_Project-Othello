package com.monadx.othello.app

import androidx.compose.material.Text
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

class App {
    fun start() {
        application {
            Window(onCloseRequest = ::exitApplication) {
                Text("Hello, World!")
            }
        }
    }
}
