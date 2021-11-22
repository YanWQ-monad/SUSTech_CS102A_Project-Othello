package com.monadx.othello.ai;

import com.monadx.othello.chess.ChessColor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.monadx.othello.ai.evaluate.Evaluator;
import com.monadx.othello.chess.Coordinate;

public class Collector {
    private Coordinate best = null;
    private Evaluator.Result score = null;
    private final Comparator originalComparator;

    private int alpha;
    private int beta;

    private final Comparator comparator;

    Collector(Comparator comparator, Comparator originalComparator, int alpha, int beta) {
        this.comparator = comparator;
        this.originalComparator = originalComparator;
        this.alpha = alpha;
        this.beta = beta;
    }

    Collector(Comparator comparator) {
        this(comparator, comparator, Integer.MIN_VALUE, Integer.MAX_VALUE);
    }

    public static Collector fromChessColor(ChessColor color) {
        return switch (color) {
            case BLACK -> new Collector(Comparator.BLACK);
            case WHITE -> new Collector(Comparator.WHITE);
            case EMPTY -> null;
        };
    }

    public Collector createNextLayer() {
        return new Collector(comparator.getOpposite(), originalComparator, alpha, beta);
    }

    public boolean tryUpdate(Coordinate coordinate, Evaluator.Result score) {
        if (comparator.isBetter(this.score, score)) {
            this.best = coordinate;
            this.score = score;

            if (originalComparator == comparator) { // maximize
                alpha = Math.max(alpha, originalComparator.valueOf(score));
            } else {
                beta = Math.min(beta, originalComparator.valueOf(score));
            }

            return true;
        } else {
            return false;
        }
    }

    public boolean shouldCutOff() {
        return alpha >= beta;
    }

    public Comparator getComparator() {
        return comparator;
    }

    public Coordinate getBest() {
        return best;
    }

    public Evaluator.Result getScore() {
        return score;
    }

    enum Comparator {
        BLACK {
            @Override
            public int valueOf(@NotNull Evaluator.Result result) {
                return result.black() * 2 - result.white();
            }

            @Override
            public Comparator getOpposite() {
                return Comparator.WHITE;
            }
        },
        WHITE {
            @Override
            public int valueOf(@NotNull Evaluator.Result result) {
                return result.white() * 2 - result.black();
            }

            @Override
            public Comparator getOpposite() {
                return Comparator.BLACK;
            }
        };

        boolean isBetter(@Nullable Evaluator.Result origin, @Nullable Evaluator.Result current) {
            return current != null && (origin == null || valueOf(current) > valueOf(origin));
        }

        abstract int valueOf(@NotNull Evaluator.Result result);
        abstract Comparator getOpposite();
    }
}
