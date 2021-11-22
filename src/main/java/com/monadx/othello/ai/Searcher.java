package com.monadx.othello.ai;

import java.util.HashMap;
import java.util.Map;

import com.monadx.othello.ai.evaluate.Evaluator;
import com.monadx.othello.chess.Board;
import com.monadx.othello.chess.ChessColor;
import com.monadx.othello.chess.Coordinate;

public class Searcher {
    private final Evaluator evaluator;
    private Map<Integer, Evaluator.Result> hash;

    public Searcher(Evaluator evaluator) {
        this.evaluator = evaluator;
    }

    public Collector search(Board board, ChessColor color, Collector collector, Accounter accounter, int progress) {
        int hashCode = board.hashCode();
        if (hash.containsKey(hashCode)) {
            Evaluator.Result result = hash.get(hashCode);
            // since the second and later layers do not need coordinate,
            // and the first layer won't hit the hash,
            // so we can make up an arbitrary coordinate
            collector.tryUpdate(new Coordinate(-1, -1), result);
            return collector;
        }

        Board new_board = board.copy();

        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                Coordinate coordinate = new Coordinate(x, y);
                if (!new_board.place(coordinate, color)) {
                    continue;
                }

                Evaluator.Result result;
                if (accounter.hasQuota()) {
                    Collector child_collector = search(
                            new_board,
                            color.getOpposite(),
                            collector.createNextLayer(),
                            accounter.nextLayer(),
                            progress + 1);
                    result = child_collector.getScore();
                } else {
                    result = evaluator.evaluate(new_board, progress);
                }

                collector.tryUpdate(coordinate, result);
                if (collector.shouldCutOff()) {
                    x = 8; // trick, to break the outer for
                    break;
                }

                new_board = board.copy(); // revert to original board
            }
        }

        hash.put(hashCode, collector.getScore());

//        String space = new String(new char[2 - accounter.getQuota()]).replace("\0", " ");
//        System.out.printf("%s best: %s: %d (%s)\n", space, collector.getBest(), collector.getScore(), color);
        return collector;
    }

    public Collector search(Board board, ChessColor color, int progress) {
        hash = new HashMap<>();
        int maxQuota = (64 - 1) - progress;
        return search(board, color, Collector.fromChessColor(color), new Accounter(Math.min(maxQuota, 8)), progress);
    }

    private static class Accounter {
        int quota;

        Accounter(int quota) {
            this.quota = quota;
        }

        Accounter nextLayer() {
            return new Accounter(quota - 1);
        }

        boolean hasQuota() {
            return quota > 0;
        }

        int getQuota() {
            return quota;
        }
    }
}
