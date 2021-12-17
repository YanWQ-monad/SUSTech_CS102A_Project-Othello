package com.monadx.othello.ui.components.menu

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

import com.monadx.othello.ui.Config.DIALOG_LINE_SPACER_BIG
import com.monadx.othello.ui.Config.DIALOG_TITLE_SIZE
import com.monadx.othello.ui.Config.DIALOG_WIDTH
import com.monadx.othello.ui.components.DialogContent
import com.monadx.othello.ui.controller.menu.AiConfigController
import com.monadx.othello.ui.model.Difficulties

object AiConfigConfig {
    val DIFFICULTY_OPTION_HEIGHT = 36.dp
    val DIFFICULTY_OPTION_WIDTH = 120.dp
}

@Composable
fun AiConfigDialog(
    controller: AiConfigController,
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

            val selectedBackgroundMask = Modifier.background(Color(0, 0, 0, 20))

            Column {
                Difficulties.values().forEach { difficulty ->
                    val selected = controller.difficulties.value == difficulty
                    Box(
                        Modifier
                            .align(Alignment.CenterHorizontally)
                            .height(AiConfigConfig.DIFFICULTY_OPTION_HEIGHT)
                            .width(AiConfigConfig.DIFFICULTY_OPTION_WIDTH)
                            .then(if (selected) selectedBackgroundMask else Modifier)
                            .selectable(
                                selected = selected,
                                onClick = { controller.difficulties.value = difficulty }
                            )
                    ) {
                        Text(
                            text = difficulty.displayText,
                            modifier = Modifier.align(Alignment.Center),
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
            }

            Spacer(Modifier.height(DIALOG_LINE_SPACER_BIG))

            Button(
                onClick = onConfirm,
                modifier = Modifier.width(DIALOG_WIDTH),
                enabled = controller.difficulties.value != null,
            ) {
                Text("Start")
            }
        }
    }
}
