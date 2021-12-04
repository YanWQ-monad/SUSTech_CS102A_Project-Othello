package com.monadx.othello.ui.components.menu

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.monadx.othello.chess.ChessColor
import com.monadx.othello.ui.Config.STATUS_ICON_BORDER_WIDTH
import com.monadx.othello.ui.Config.STATUS_ICON_SIZE
import com.monadx.othello.ui.components.board.graphicsColor

object ChessChooserConfig {
    val ICON_BORDER_WIDTH = STATUS_ICON_BORDER_WIDTH
    val ICON_SIZE = STATUS_ICON_SIZE
    val ICON_CHOSEN_BORDER_COLOR = Color.Yellow
    val ICON_SPACER = 8.dp
}

@Composable
fun ChessChooser(
    color: ChessColor,
    onColorChange: (ChessColor) -> Unit,
) {
    Row {
        ChessPiece(
            ChessColor.BLACK,
            isChosen = color == ChessColor.BLACK,
            onClick = { onColorChange(ChessColor.BLACK) }
        )

        Spacer(Modifier.width(ChessChooserConfig.ICON_SPACER))

        ChessPiece(
            ChessColor.WHITE,
            isChosen = color == ChessColor.WHITE,
            onClick = { onColorChange(ChessColor.WHITE) }
        )
    }
}

@Composable
fun ChessPiece(color: ChessColor, isChosen: Boolean, onClick: () -> Unit) {
    val borderModifier = if (isChosen) {
        Modifier
            .border(
                shape = CircleShape,
                color = ChessChooserConfig.ICON_CHOSEN_BORDER_COLOR,
                width = ChessChooserConfig.ICON_BORDER_WIDTH,
            )
    } else {
        Modifier
    };

    Box(
        Modifier
            .then(borderModifier)
            .padding(ChessChooserConfig.ICON_BORDER_WIDTH)
            .clip(CircleShape)
            .clickable(onClick = onClick)
    ) {
        Box(
            Modifier
                .size(ChessChooserConfig.ICON_SIZE)
                .background(color.graphicsColor())
        )
    }
}
