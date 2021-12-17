package com.monadx.othello.ai.evaluate;

import org.jetbrains.annotations.NotNull;

import com.monadx.othello.ai.utils.BoardExtends;
import com.monadx.othello.chess.Board;
import com.monadx.othello.chess.ChessColor;

public class DynamicEvaluator extends Evaluator {
    @Override
    public int evaluateColor(@NotNull Board board, @NotNull ChessColor color, int progress) {
        BoardExtends boardExtends = new BoardExtends(board);
        Weight weight = Weight.getPeriodAt(progress);

        int disc = boardExtends.disc(color);
        int cell = boardExtends.weightedCellScore(color);
        int mobility = boardExtends.mobility(color);
        int corner = boardExtends.corner(color);
        int stable = boardExtends.stable(color);

        return disc * weight.disc
                + cell * weight.cell
                + mobility * weight.mobility
                + corner * weight.corner
                + stable * weight.stable;
    }

    @Override
    @NotNull
    public Result correctForfeit(@NotNull Board board, int progress, @NotNull Result result, @NotNull ChessColor color) {
        return result;
    }

    public enum Weight {
        PERIOD_0(1, 5, 30, 1000, 50),
        PERIOD_1(2, 4, 25, 900, 50),
        PERIOD_2(3, 3, 20, 800, 40),
        PERIOD_3(4, 2, 20, 600, 40),
        PERIOD_4(50, 1, 15, 400, 30),
        PERIOD_5(200, 0, 10, 100, 20);

        Weight(int disc, int cell, int mobility, int corner, int stable) {
            this.disc = disc;
            this.cell = cell;
            this.mobility = mobility;
            this.corner = corner;
            this.stable = stable;
        }

        @NotNull
        public static Weight getPeriodAt(int progress) {
            int period = (progress - 5) / 10;
            return values()[period];
        }

        public final int disc;
        public final int cell;
        public final int mobility;
        public final int corner;
        public final int stable;
    }
}
