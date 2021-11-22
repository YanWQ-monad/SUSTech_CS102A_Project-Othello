package com.monadx.othello.ai.evaluate;

import com.monadx.othello.chess.Board;
import com.monadx.othello.chess.ChessColor;

public class GreedyEvaluator extends Evaluator {
    public GreedyEvaluator() {}

    @Override
    public int evaluateColor(Board board, ChessColor color, int progress) {
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

    public Result correctForfeit(Board board, int progress, Result result, ChessColor color) {
        return correctResult(result, color, 0);
    }
}
