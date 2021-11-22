package com.monadx.othello.save;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

import com.monadx.othello.chess.ChessColor;
import com.monadx.othello.chess.Step;

public record GameRecord(
        List<Step> stepList,
        int boardHash,
        ChessColor currentPlayer
) implements Serializable {
    @Serial private static final long serialVersionUID = 1L;
}
