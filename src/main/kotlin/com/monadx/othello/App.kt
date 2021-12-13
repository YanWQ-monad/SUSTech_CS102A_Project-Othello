package com.monadx.othello

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application

import com.monadx.othello.ui.AppState

class App {
    private val state by mutableStateOf(AppState())

    fun start() {
        application {
            Window(
                onCloseRequest = {
                    state.getController().onClose()
                    exitApplication()
                },
                state = WindowState(size = DpSize(440.dp, 574.dp)) // TODO: avoid magic number
            ) {
                MaterialTheme(
                    colorScheme = lightColorScheme(
                        background = Color(200, 200, 200),
                        surface = Color(180, 180, 180),
                    )
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background),
                    ) {
                        Surface(
                            elevation = 3.dp,
                            shape = RoundedCornerShape(5.dp),
                            color = MaterialTheme.colorScheme.surface,
                        ) {
                            state.getController().view()
                        }
                    }
                }
            }
        }
    }
}
