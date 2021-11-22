package com.monadx.othello.ai.evaluate;

import com.monadx.othello.chess.Board;
import com.monadx.othello.chess.ChessColor;

public abstract class Evaluator {
    public abstract int evaluateColor(Board board, ChessColor color, int progress);

    public abstract Result correctForfeit(Board board, int progress, Result result, ChessColor color);

    public Result evaluate(Board board, int progress) {
        return new Result(
                evaluateColor(board, ChessColor.BLACK, progress),
                evaluateColor(board, ChessColor.WHITE, progress));
    }

    protected Result correctResult(Result score, ChessColor color, int correction) {
        if (color == ChessColor.BLACK) {
            return new Result(score.black() + correction, score.white());
        } else {
            return new Result(score.black(), score.white() + correction);
        }
    }

    public record Result(int black, int white) {}
}
