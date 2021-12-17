package com.monadx.othello.ai.evaluate;

import org.jetbrains.annotations.NotNull;

import com.monadx.othello.chess.Board;
import com.monadx.othello.chess.ChessColor;

public class NaiveEvaluator extends DynamicEvaluator {
    @Override
    public int evaluateColor(@NotNull Board board, @NotNull ChessColor color, int progress) {
        return -super.evaluateColor(board, color, progress);
    }

    @Override
    @NotNull
    public Result correctForfeit(@NotNull Board board, int progress, @NotNull Result result, @NotNull ChessColor color) {
        return correctResult(result, color, 100);
    }
}
