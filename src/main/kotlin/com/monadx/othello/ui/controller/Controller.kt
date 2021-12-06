package com.monadx.othello.ui.controller

import androidx.compose.runtime.Composable

import com.monadx.othello.ui.AppState

abstract class Controller(val appState: AppState) {
    @Composable
    abstract fun view()

    open fun onClose() {}
}
