package com.monadx.othello.ai;

import java.util.*;
import java.util.stream.Collectors;

import com.monadx.othello.ai.evaluate.Evaluator;
import com.monadx.othello.chess.Board;
import com.monadx.othello.chess.ChessColor;
import com.monadx.othello.chess.Coordinate;
import com.monadx.othello.chess.Utils;

public class Searcher {
    private final Evaluator evaluator;
    private final Map<Integer, Evaluator.Result> hash;

    public Searcher(Evaluator evaluator) {
        this.evaluator = evaluator;
        this.hash = new HashMap<>();
    }

    public Collector search(Board board, ChessColor color, Collector collector, int quota, int progress) {
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

            if (quota > 0) {
                Collector child_collector = search(
                        board,
                        color.getOpposite(),
                        collector.createNextLayer(),
                        quota - 1,
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
                Collector child_collector = search(
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

//        String space = new String(new char[2 - accounter.getQuota()]).replace("\0", " ");
//        System.out.printf("%s best: %s: %d (%s)\n", space, collector.getBest(), collector.getScore(), color);
        return collector;
    }

    public Collector search(Board board, ChessColor color, int progress) {
        hash.clear();
        int maxQuota = (64 - 1) - progress;
        return search(board, color, Collector.fromChessColor(color), Math.min(maxQuota, 8), progress);
    }

    private static record PossibleMove(Board board, Coordinate coordinate, Evaluator.Result score) {}
}
