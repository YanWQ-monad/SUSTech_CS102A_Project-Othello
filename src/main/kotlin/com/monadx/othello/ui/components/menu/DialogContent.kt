package com.monadx.othello.ui.components.menu

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.window.Popup

import com.monadx.othello.ui.Config.DIALOG_BACKGROUND_COLOR
import com.monadx.othello.ui.Config.DIALOG_PADDING
import com.monadx.othello.ui.Config.DIALOG_ROUNDED_CORNER_SIZE
import com.monadx.othello.ui.Config.DIALOG_SHADOW_ELEVATION

@Composable
fun DialogContent(
    onCloseRequest: () -> Unit,
    content: @Composable () -> Unit,
) {
    Popup(
        alignment = Alignment.Center,
        onDismissRequest = onCloseRequest,
        focusable = true,
    ) {
        Surface(
            modifier = Modifier.shadow(DIALOG_SHADOW_ELEVATION),
            shape = RoundedCornerShape(DIALOG_ROUNDED_CORNER_SIZE),
            color = DIALOG_BACKGROUND_COLOR,
        ) {
            Box(
                modifier = Modifier.padding(DIALOG_PADDING),
            ) {
                content()
            }
        }
    }
}
