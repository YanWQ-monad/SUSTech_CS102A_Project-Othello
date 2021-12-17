package com.monadx.othello.ai.evaluate;

import com.monadx.othello.chess.Board;
import com.monadx.othello.chess.ChessColor;

public class NaiveEvaluator extends DynamicEvaluator {
    @Override
    public int evaluateColor(Board board, ChessColor color, int progress) {
        return -super.evaluateColor(board, color, progress);
    }

    @Override
    public Result correctForfeit(Board board, int progress, Result result, ChessColor color) {
        return correctResult(result, color, 100);
    }
}
