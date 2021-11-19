package com.monadx.othello.ui.components.board

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

import com.monadx.othello.ui.Config.BLACK_CHESS_COLOR
import com.monadx.othello.ui.Config.STATUS_ICON_BORDER_WIDTH
import com.monadx.othello.ui.Config.STATUS_ICON_SIZE
import com.monadx.othello.ui.Config.STATUS_SCORE_BAR_HEIGHT
import com.monadx.othello.ui.Config.STATUS_SCORE_SIZE
import com.monadx.othello.ui.Config.WHITE_CHESS_COLOR

object StatusConfig {
    val STATUS_PADDING = PaddingValues(horizontal = 8.dp, vertical = 3.dp)
    val STATUS_DIVIDE_SPACER = 4.dp

    val STATUS_SCORE_SPACER = 10.dp
    val STATUS_SCORE_PADDING_BASELINE = 7.dp
}

class GameStatusState {
    val black = PlayerState()
    val white = PlayerState()
}

class PlayerState {
    var score = mutableStateOf(0)
    var status = mutableStateOf(Status.IDLE)

    enum class Status {
        PLAYING,
        IDLE,
        WIN,
        ERROR,
    }
}

@Composable
fun GameStatus(state: GameStatusState) {
    Column(
        Modifier
            .padding(StatusConfig.STATUS_PADDING)
    ) {
        GameScore(state)
        Spacer(Modifier.height(StatusConfig.STATUS_DIVIDE_SPACER))
        GameScoreBar(state)
    }
}

@Composable
fun GameScore(state: GameStatusState) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Row(
            verticalAlignment = Alignment.Bottom,
        ) {
            PlayerIcon(state.black.status.value, BLACK_CHESS_COLOR)
            Text(
                text = "${state.black.score.value}",
                fontSize = STATUS_SCORE_SIZE,
                modifier = Modifier
                    .paddingFromBaseline(bottom = StatusConfig.STATUS_SCORE_PADDING_BASELINE)
                    .offset(x = StatusConfig.STATUS_SCORE_SPACER)
            )
        }
        Row(
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.End,
        ) {
            Text(
                text = "${state.white.score.value}",
                fontSize = STATUS_SCORE_SIZE,
                modifier = Modifier
                    .paddingFromBaseline(bottom = StatusConfig.STATUS_SCORE_PADDING_BASELINE)
                    .offset(x = -StatusConfig.STATUS_SCORE_SPACER)
            )
            PlayerIcon(state.white.status.value, WHITE_CHESS_COLOR)
        }
    }
}

@Composable
fun GameScoreBar(state: GameStatusState) {
    val FULL_SCORE = 64

    Row(
        Modifier
            .fillMaxWidth()
    ) {
        if (state.black.score.value > 0) {
            Box(
                Modifier
                    .weight(state.black.score.value.toFloat())
                    .height(STATUS_SCORE_BAR_HEIGHT)
                    .background(Color.Black)
            )
        }
        if (FULL_SCORE > state.black.score.value + state.white.score.value) {
            Box(
                Modifier
                    .weight((FULL_SCORE - state.black.score.value - state.white.score.value).toFloat())
                    .height(STATUS_SCORE_BAR_HEIGHT)
                    .background(Color.Transparent)
            )
        }
        if (state.white.score.value > 0) {
            Box(
                Modifier
                    .weight(state.white.score.value.toFloat())
                    .height(STATUS_SCORE_BAR_HEIGHT)
                    .background(Color.White)
            )
        }
    }
}

@Composable
fun PlayerIcon(status: PlayerState.Status, color: Color) {
    if (status != PlayerState.Status.IDLE) {
        Box (
            Modifier
                .border(
                    shape = CircleShape,
                    color = when (status) {
                        PlayerState.Status.PLAYING -> Color.Green
                        PlayerState.Status.WIN -> Color.Yellow
                        PlayerState.Status.ERROR -> Color.Red
                        PlayerState.Status.IDLE -> throw IllegalStateException("Unreachable Code")
                    },
                    width = STATUS_ICON_BORDER_WIDTH,
                )
                .clip(CircleShape)
        ) {
            Box(
                Modifier
                    .size(STATUS_ICON_SIZE + STATUS_ICON_BORDER_WIDTH * 2)
                    .background(color)
            )
        }
    } else {
        Box (
            Modifier
                .padding(STATUS_ICON_BORDER_WIDTH)
                .clip(CircleShape)
        ) {
            Box(
                Modifier
                    .size(STATUS_ICON_SIZE)
                    .background(color)
            )
        }
    }
}
