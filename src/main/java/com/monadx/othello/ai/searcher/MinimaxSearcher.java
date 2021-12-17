package com.monadx.othello.ai.searcher;

import java.util.*;
import java.util.stream.Collectors;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.monadx.othello.ai.evaluate.Evaluator;
import com.monadx.othello.ai.utils.SearchResult;
import com.monadx.othello.chess.Board;
import com.monadx.othello.chess.ChessColor;
import com.monadx.othello.chess.Coordinate;
import com.monadx.othello.chess.Utils;

public class MinimaxSearcher extends Searcher {
    private final Evaluator evaluator;
    private final Map<Integer, Evaluator.Result> hash;

    public MinimaxSearcher(Evaluator evaluator) {
        this.evaluator = evaluator;
        this.hash = new HashMap<>();
    }

    public Collector recursiveSearch(Board board, ChessColor color, Collector collector, int quota, int progress) {
        int hashCode = board.hashCode();
        if (hash.containsKey(hashCode)) {
            Evaluator.Result result = hash.get(hashCode);
            // since the second and later layers do not need coordinate,
            // and the first layer won't hit the hash,
            // so we can make up an arbitrary coordinate
            collector.tryUpdate(new Coordinate(-1, -1), result);
            return collector;
        }

        List<PossibleMove> moves = Arrays.stream(Utils.POSITION_LIST)
                .filter(coordinate -> board.checkPlaceable(coordinate, color))
                .map(coordinate -> {
                    Board new_board = board.copy();
                    new_board.place(coordinate, color);
                    Evaluator.Result result = evaluator.evaluate(new_board, progress);
                    return new PossibleMove(new_board, coordinate, result);
                })
                .sorted(Collections.reverseOrder(
                        Comparator.comparing(move -> collector.getComparator().valueOf(move.score()))))
                .collect(Collectors.toList());

        if (moves.isEmpty()) {
            Evaluator.Result result;

            boolean opponentHasMove = Arrays.stream(Utils.POSITION_LIST)
                    .anyMatch(coordinate -> board.checkPlaceable(coordinate, color.getOpposite()));

            if (!opponentHasMove) {
                result = evaluator.getEndedValue(board);
            }
            else if (quota > 0) {
                Collector child_collector = recursiveSearch(
                        board,
                        color.getOpposite(),
                        collector.createNextLayer(),
                        quota,
                        progress);
                result = child_collector.getScore();
            } else {
                result = evaluator.evaluate(board, progress);
            }

            collector.tryUpdate(new Coordinate(-1, -1), evaluator.correctForfeit(board, progress, result, color));
        }

        for (PossibleMove move : moves) {
            Board new_board = move.board();

            Evaluator.Result result;
            if (quota > 0) {
                Collector child_collector = recursiveSearch(
                        new_board,
                        color.getOpposite(),
                        collector.createNextLayer(),
                        quota - 1,
                        progress + 1);
                result = child_collector.getScore();
            } else {
                result = move.score();
            }

            collector.tryUpdate(move.coordinate(), result);
            if (collector.shouldCutOff()) {
                break;
            }
        }

        hash.put(hashCode, collector.getScore());

        return collector;
    }

    @Override
    public SearchResult search(Board board, ChessColor color, int progress) {
        hash.clear();
        int maxQuota = (64 - 1) - progress;
        Collector collector = recursiveSearch(board, color, Collector.fromChessColor(color), Math.min(maxQuota, 8), progress);
        return collector.toSearchResult();
    }

    private static record PossibleMove(Board board, Coordinate coordinate, Evaluator.Result score) {}

    private static class Collector {
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
                case EMPTY -> throw new IllegalArgumentException("Empty color is not allowed");
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

        public SearchResult toSearchResult() {
            return new SearchResult(best, score);
        }

        public enum Comparator {
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
}
