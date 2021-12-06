package com.monadx.othello.ui.components.menu

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

import com.monadx.othello.network.broadcast.MulticastClient
import com.monadx.othello.ui.Config

@Composable
fun ServerListDialog(
    list: MutableList<MulticastClient.Packet>,
    onChoose: (MulticastClient.Packet) -> Unit,
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
                for (packet in list) {
                    ServerListItem(packet, onClick = { onChoose(packet) })
                }
            }
        }
    }
}

@Composable
fun ServerListItem(packet: MulticastClient.Packet, onClick: () -> Unit) {
    Box(
        Modifier
            .clickable { onClick() }
    ) {
        Column(Modifier.padding(8.dp)) {
            Text(
                text = packet.message.serverName,
                style = MaterialTheme.typography.h6,
            )

            Text(
                text = "${packet.sender.hostAddress}:${packet.message.port}",
                color = Color.Gray,
                modifier = Modifier.width(Config.DIALOG_WIDTH),
            )
        }
    }
}
