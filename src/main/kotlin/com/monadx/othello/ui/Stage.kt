package com.monadx.othello.ui

import com.monadx.othello.ui.controller.AiController
import com.monadx.othello.ui.controller.Controller
import com.monadx.othello.ui.controller.MenuController
import com.monadx.othello.ui.controller.VersusController

enum class Stage {
    MENU {
        override fun getController() = MenuController()
    },
    VERSUS {
        override fun getController() = VersusController()
    },
    AI {
        override fun getController() = AiController()
    };

    abstract fun getController(): Controller
}
