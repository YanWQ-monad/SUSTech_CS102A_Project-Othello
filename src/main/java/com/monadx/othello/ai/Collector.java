package com.monadx.othello.ai;

import com.monadx.othello.chess.Coordinate;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Collector {
    private Coordinate best = null;
    private Evaluator.Result score = null;

    private final int depth;
    private final Comparator comparator;

    Collector(int depth, Comparator comparator) {
        this.depth = depth;
        this.comparator = comparator;
    }

    Collector(int depth) {
        this(depth, Comparator.BLACK);
    }

    public Collector createNextLayer() {
        return new Collector(depth + 1, comparator.getOpposite());
    }

    public boolean tryUpdate(Coordinate coordinate, Evaluator.Result score) {
        if (comparator.isBetter(this.score, score)) {
            this.best = coordinate;
            this.score = score;
            return true;
        } else {
            return false;
        }
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


