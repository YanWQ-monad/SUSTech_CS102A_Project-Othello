package com.monadx.othello.ui.components.menu

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

import com.monadx.othello.network.broadcast.MulticastClient
import com.monadx.othello.ui.Config
import com.monadx.othello.ui.components.DialogContent

object ServerListConfig {
    val LIST_PADDING_SIZE = 8.dp

    val PASSWORD_FIELD_ROUNDED_CORNER_RADIUS = 3.dp
    val PASSWORD_FIELD_INNER_PADDING = 8.dp
    const val PASSWORD_FIELD_BACKGROUND_ALPHA = 0.05f
    const val PASSWORD_FIELD_PLACEHOLDER_ALPHA = 0.3f
}

@Composable
fun ServerListDialog(
    list: MutableList<MulticastClient.Packet>,
    onChoose: (MulticastClient.Packet, String) -> Unit,
    onCloseRequest: () -> Unit,
) {
    DialogContent(onCloseRequest = onCloseRequest) {
        Column {
            Text(
                "Server List",
                textAlign = TextAlign.Center,
                modifier = Modifier.width(Config.DIALOG_WIDTH),
                fontSize = Config.DIALOG_TITLE_SIZE,
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.height(Config.DIALOG_LINE_SPACER_BIG))

            Column {
                if (list.isEmpty()) {
                    Text(
                        "No server found",
                        textAlign = TextAlign.Center,
                        modifier = Modifier.width(Config.DIALOG_WIDTH),
                        color = Color.Gray,
                        fontStyle = FontStyle.Italic
                    )
                } else {
                    for (packet in list) {
                        val expanded = remember { mutableStateOf(false) }
                        ServerListItem(packet, expanded.value, onConfirm = { password ->
                            if (packet.message.isPasswordRequired) {
                                if (!expanded.value) {
                                    expanded.value = true
                                }
                                if (password?.isNotEmpty() == true) {
                                    onChoose(packet, password)
                                }
                            } else {
                                onChoose(packet, "")
                            }
                        })
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ServerListItem(packet: MulticastClient.Packet, passwordExpanded: Boolean, onConfirm: (password: String?) -> Unit) {
    Box(
        Modifier
            .clickable { onConfirm(null) }
    ) {
        Column(Modifier.padding(ServerListConfig.LIST_PADDING_SIZE)) {
            if (packet.message.serverName.isEmpty()) {
                Text(
                    text = "Unnamed Server",
                    style = MaterialTheme.typography.h6,
                    color = Color.Gray,
                    fontStyle = FontStyle.Italic,
                    fontWeight = FontWeight.Normal,
                )
            } else {
                Text(
                    text = packet.message.serverName,
                    style = MaterialTheme.typography.h6,
                )
            }

            Text(
                text = "${packet.sender.hostAddress}:${packet.message.port}",
                color = Color.Gray,
                modifier = Modifier.width(Config.DIALOG_WIDTH),
            )

            if (passwordExpanded) {
                val password = remember { mutableStateOf("") }

                Spacer(Modifier.height(Config.DIALOG_LINE_SPACER_SMALL))

                BasicTextField(
                    value = password.value,
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier
                        .clip(RoundedCornerShape(ServerListConfig.PASSWORD_FIELD_ROUNDED_CORNER_RADIUS))
                        .background(Color.Black.copy(alpha = ServerListConfig.PASSWORD_FIELD_BACKGROUND_ALPHA))
                        .width(Config.DIALOG_WIDTH)
                        .onKeyEvent { keyEvent ->
                            if (keyEvent.key == Key.Enter) {
                                onConfirm(password.value)
                                return@onKeyEvent true
                            }
                            false
                        },
                    onValueChange = { password.value = it },
                    singleLine = true,
                    decorationBox = { innerTextField ->
                        Box(
                            Modifier.padding(ServerListConfig.PASSWORD_FIELD_INNER_PADDING)
                        ) {
                            if (password.value.isEmpty()) Text(
                                "Password",
                                style = MaterialTheme.typography.body2.copy(
                                    color = MaterialTheme.colors.onSurface.copy(alpha = ServerListConfig.PASSWORD_FIELD_PLACEHOLDER_ALPHA),
                                )
                            )
                            innerTextField()
                        }
                    }
                )
            }
        }
    }
}
