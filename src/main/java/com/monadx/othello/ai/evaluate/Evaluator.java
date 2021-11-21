package com.monadx.othello.ai.evaluate;

import com.monadx.othello.chess.Board;
import com.monadx.othello.chess.ChessColor;

public abstract class Evaluator {
    public abstract int evaluateColor(Board board, ChessColor color, int progress);

    public Evaluator.Result evaluate(Board board, int progress) {
        return new Evaluator.Result(
                evaluateColor(board, ChessColor.BLACK, progress),
                evaluateColor(board, ChessColor.WHITE, progress));
    }

    public record Result(int black, int white) {}
}
