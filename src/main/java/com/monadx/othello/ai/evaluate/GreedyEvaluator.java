package com.monadx.othello.ai.evaluate;

import org.jetbrains.annotations.NotNull;

import com.monadx.othello.chess.Board;
import com.monadx.othello.chess.ChessColor;

public class GreedyEvaluator extends Evaluator {
    @Override
    public int evaluateColor(@NotNull Board board, @NotNull ChessColor color, int progress) {
        final ChessColor opposite = color.getOpposite();
        int value = 0;
        for (int x = 0; x < 8; x++)
            for (int y = 0; y < 8; y++) {
                if (board.getBoard()[x][y] == color) {
                    value++;
                } else if (board.getBoard()[x][y] == opposite) {
                    value--;
                }
            }

        return value;
    }

    @Override
    @NotNull
    public Result correctForfeit(@NotNull Board board, int progress, @NotNull Result result, @NotNull ChessColor color) {
        return correctResult(result, color, 0);
    }
}
