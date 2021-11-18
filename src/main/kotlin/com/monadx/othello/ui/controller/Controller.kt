package com.monadx.othello.ui.controller

import androidx.compose.runtime.Composable

import com.monadx.othello.ui.AppState

abstract class Controller {
    @Composable
    abstract fun view(state: AppState)
}
