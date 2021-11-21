package com.monadx.othello.ai;

import com.monadx.othello.chess.Board;
import com.monadx.othello.chess.ChessColor;

public class Evaluator {
    public Evaluator() {}

    public int evaluateColor(Board board, ChessColor color) {
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

    public Result evaluate(Board board) {
        return new Result(
                evaluateColor(board, ChessColor.BLACK),
                evaluateColor(board, ChessColor.WHITE));
    }

    public record Result(int black, int white) {}
}
