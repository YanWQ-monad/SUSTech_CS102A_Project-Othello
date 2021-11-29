package com.monadx.othello.ui.components.menu

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

import com.monadx.othello.chess.ChessColor
import com.monadx.othello.ui.Config.DIALOG_LINE_SPACER_BIG
import com.monadx.othello.ui.Config.DIALOG_TITLE_SIZE
import com.monadx.othello.ui.Config.DIALOG_WIDTH
import com.monadx.othello.ui.Config.STATUS_ICON_BORDER_WIDTH
import com.monadx.othello.ui.Config.STATUS_ICON_SIZE
import com.monadx.othello.ui.components.board.graphicsColor
import com.monadx.othello.ui.controller.menu.ChessChooseController

object ChessChooseConfig {
    val ICON_BORDER_WIDTH = STATUS_ICON_BORDER_WIDTH
    val ICON_SIZE = STATUS_ICON_SIZE
    val ICON_CHOSEN_BORDER_COLOR = Color.Yellow
    val ICON_SPACER = 8.dp
}

@Composable
fun ChessChooseDialog(
    controller: ChessChooseController,
    onCloseRequest: () -> Unit,
    onConfirm: () -> Unit,
) {
    DialogContent(onCloseRequest = onCloseRequest) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                "Your Color",
                textAlign = TextAlign.Center,
                modifier = Modifier.width(DIALOG_WIDTH),
                fontSize = DIALOG_TITLE_SIZE,
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.height(DIALOG_LINE_SPACER_BIG))

            Row {
                ChessPiece(
                    ChessColor.BLACK,
                    isChosen = controller.color.value == ChessColor.BLACK,
                    onClick = { controller.color.value = ChessColor.BLACK }
                )

                Spacer(Modifier.width(ChessChooseConfig.ICON_SPACER))

                ChessPiece(
                    ChessColor.WHITE,
                    isChosen = controller.color.value == ChessColor.WHITE,
                    onClick = { controller.color.value = ChessColor.WHITE }
                )
            }

            Spacer(Modifier.height(DIALOG_LINE_SPACER_BIG))

            Button(
                onClick = onConfirm,
                modifier = Modifier.width(DIALOG_WIDTH)
            ) {
                Text("Start")
            }
        }
    }
}

@Composable
fun ChessPiece(color: ChessColor, isChosen: Boolean, onClick: () -> Unit) {
    val borderModifier = if (isChosen) {
        Modifier
            .border(
                shape = CircleShape,
                color = ChessChooseConfig.ICON_CHOSEN_BORDER_COLOR,
                width = ChessChooseConfig.ICON_BORDER_WIDTH,
            )
    } else {
        Modifier
    };

    Box(
        Modifier
            .then(borderModifier)
            .padding(ChessChooseConfig.ICON_BORDER_WIDTH)
            .clip(CircleShape)
            .clickable(onClick = onClick)
    ) {
        Box(
            Modifier
                .size(ChessChooseConfig.ICON_SIZE)
                .background(color.graphicsColor())
        )
    }
}
