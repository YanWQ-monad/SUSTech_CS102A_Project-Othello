package com.monadx.othello.ui.components.menu

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign

import com.monadx.othello.ui.Config.DIALOG_LINE_SPACER_BIG
import com.monadx.othello.ui.Config.DIALOG_TITLE_SIZE
import com.monadx.othello.ui.Config.DIALOG_WIDTH
import com.monadx.othello.ui.controller.menu.ChessChooseController

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

            ChessChooser(
                color = controller.color.value,
                onColorChange = { color -> controller.color.value = color }
            )

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
