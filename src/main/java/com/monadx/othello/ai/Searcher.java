package com.monadx.othello.ai;

import com.monadx.othello.chess.Board;
import com.monadx.othello.chess.ChessColor;
import com.monadx.othello.chess.Coordinate;

public class Searcher {
    private final Evaluator evaluator;

    public Searcher(Evaluator evaluator) {
        this.evaluator = evaluator;
    }

    public Collector search(Board board, ChessColor color, Collector collector, Accounter accounter) {
        Board new_board = board.copy();

        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                Coordinate coordinate = new Coordinate(x, y);
                if (!new_board.place(coordinate, color)) {
                    continue;
                }

                if (accounter.hasQuota()) {
                    Collector child_collector = search(
                            new_board, color.getOpposite(), collector.createNextLayer(), accounter.nextLayer());
                    collector.tryUpdate(coordinate, child_collector.getScore());
//                    String space = new String(new char[2 - accounter.getQuota()]).replace("\0", " ");
//                    System.out.printf(" %s %s: %d\n", space, coordinate, result);
                } else {
                    collector.tryUpdate(
                            coordinate,
                            evaluator.evaluate(new_board));
                }

                new_board = board.copy(); // revert to original board
            }
        }

//        String space = new String(new char[2 - accounter.getQuota()]).replace("\0", " ");
//        System.out.printf("%s best: %s: %d (%s)\n", space, collector.getBest(), collector.getScore(), color);
        return collector;
    }

    public Collector search(Board board, ChessColor color, int depth) {
        int maxQuota = (64 - 1) - depth;
        return search(board, color, new Collector(depth), new Accounter(Math.min(maxQuota, 6)));
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
