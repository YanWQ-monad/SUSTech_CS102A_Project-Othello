package com.monadx.othello.ui.components.menu

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

import com.monadx.othello.chess.ChessColor
import com.monadx.othello.ui.Config.DIALOG_LINE_SPACER_BIG
import com.monadx.othello.ui.Config.DIALOG_TITLE_SIZE
import com.monadx.othello.ui.Config.DIALOG_WIDTH
import com.monadx.othello.ui.components.DialogContent

@Composable
fun ServerConfigDialog(
    serverName: String,
    onServerNameChange: (String) -> Unit,
    password: String,
    onPasswordChange: (String) -> Unit,
    color: ChessColor,
    onColorChange: (ChessColor) -> Unit,
    onConfirm: () -> Unit,
    onCloseRequest: () -> Unit,
) {
    DialogContent(onCloseRequest = onCloseRequest) {
        Column {
            Text(
                "Server Configuration",
                textAlign = TextAlign.Center,
                modifier = Modifier.width(DIALOG_WIDTH),
                fontSize = DIALOG_TITLE_SIZE,
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.height(DIALOG_LINE_SPACER_BIG))

            OutlinedTextField(
                value = serverName,
                modifier = Modifier.width(DIALOG_WIDTH),
                label = { Text("Server Name") },
                onValueChange = onServerNameChange,
                singleLine = true,
            )

            Spacer(Modifier.height(DIALOG_LINE_SPACER_BIG))

            OutlinedTextField(
                value = password,
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.width(DIALOG_WIDTH),
                label = { Text("Password (Optional)") },
                onValueChange = onPasswordChange,
                singleLine = true,
            )

            Spacer(Modifier.height(DIALOG_LINE_SPACER_BIG))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Your Color: ")

                Spacer(Modifier.width(3.dp))

                ChessChooser(color, onColorChange)
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
