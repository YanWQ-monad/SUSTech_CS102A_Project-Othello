package com.monadx.othello.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.monadx.othello.ui.Config.DIALOG_LINE_SPACER_BIG

@Composable
fun ConfirmBox(
    message: String,
    onConfirm: (Boolean) -> Unit,
) {
    DialogContent(onCloseRequest = { onConfirm(false) }) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                message,
                textAlign = TextAlign.Center,
                modifier = Modifier.width(200.dp),
            )

            Spacer(Modifier.height(DIALOG_LINE_SPACER_BIG))

            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                TextButton(onClick = { onConfirm(true) }) {
                    Text("Yes")
                }
                Spacer(Modifier.width(10.dp))
                TextButton(onClick = { onConfirm(false) }) {
                    Text("No")
                }
            }
        }
    }
}
