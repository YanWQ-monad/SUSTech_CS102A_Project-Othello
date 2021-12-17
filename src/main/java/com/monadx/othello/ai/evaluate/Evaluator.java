package com.monadx.othello.ai.evaluate;

import org.jetbrains.annotations.NotNull;

import com.monadx.othello.ai.utils.BoardExtends;
import com.monadx.othello.chess.Board;
import com.monadx.othello.chess.ChessColor;

public abstract class Evaluator {
    public abstract int evaluateColor(@NotNull Board board, @NotNull ChessColor color, int progress);

    @NotNull
    public abstract Result correctForfeit(@NotNull Board board, int progress, @NotNull Result result, @NotNull ChessColor color);

    @NotNull
    public Result getEndedValue(@NotNull Board board) {
        BoardExtends boardExtends = new BoardExtends(board);
        int blackDisc = boardExtends.disc(ChessColor.BLACK);
        int whiteDisc = boardExtends.disc(ChessColor.WHITE);

        int whiteValue = whiteDisc > blackDisc ? 1000000 : 0;
        int blackValue = blackDisc > whiteDisc ? 1000000 : 0;
        return new Result(blackValue, whiteValue);
    }

    @NotNull
    public Result evaluate(@NotNull Board board, int progress) {
        return new Result(
                evaluateColor(board, ChessColor.BLACK, progress),
                evaluateColor(board, ChessColor.WHITE, progress));
    }

    @NotNull
    protected Result correctResult(@NotNull Result score, @NotNull ChessColor color, int correction) {
        if (color == ChessColor.BLACK) {
            return new Result(score.black() + correction, score.white());
        } else {
            return new Result(score.black(), score.white() + correction);
        }
    }

    public record Result(int black, int white) {}
}
