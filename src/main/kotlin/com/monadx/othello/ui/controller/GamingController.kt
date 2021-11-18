package com.monadx.othello.ui.controller

import com.monadx.othello.ui.components.board.GameBoardPiece

abstract class GamingController: Controller() {
    abstract fun onClick(piece: GameBoardPiece, x: Int, y: Int)

    abstract fun isTurn(): Boolean
}
