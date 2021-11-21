package com.monadx.othello.ai.utils;

import com.monadx.othello.chess.Board;
import com.monadx.othello.chess.ChessColor;
import com.monadx.othello.chess.Coordinate;
import com.monadx.othello.chess.Utils;

public class BoardExtends {
    private final Board board;
    private final ChessColor[][] table;

    private final int[][] squareScore;

    private static final int[] DX8 = {-1, -1, -1, 0, 0, 1, 1, 1};
    private static final int[] DY8 = {-1, 0, 1, -1, 1, -1, 0, 1};
    private static final int[] DX4 = {-1, 0, 1, 0};
    private static final int[] DY4 = {0, -1, 0, 1};

    public BoardExtends(Board board) {
        this.board = board;
        this.table = board.getBoard();

        this.squareScore = new int[][]{
                {100, -10, 8, 6, 6, 8, -10, 100},
                {-10, -25, -4, -4, -4, -4, -25, -10},
                {8, -4, 6, 4, 4, 6, -4, 8},
                {6, -4, 4, 0, 0, 4, -4, 6},
                {6, -4, 4, 0, 0, 4, -4, 6},
                {8, -4, 6, 4, 4, 6, -4, 8},
                {-10, -25, -4, -4, -4, -4, -25, -10},
                {100, -10, 8, 6, 6, 8, -10, 100}};
    }

    public int corner(ChessColor player) {
        int count = 0;
        if (table[0][0] == player) count++;
        if (table[0][7] == player) count++;
        if (table[7][0] == player) count++;
        if (table[7][7] == player) count++;
        return count;
    }

    public int stable(ChessColor player) {
        boolean[][] stable = new boolean[8][8];
        stableCorner(player, 0, 0, stable);
        stableCorner(player, 0, 7, stable);
        stableCorner(player, 7, 0, stable);
        stableCorner(player, 7, 7, stable);

        int count = 0;
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                if (stable[x][y]) {
                    count++;
                }
            }
        }
        return count;
    }

    public int mobility(ChessColor player) {
        int count = 0;
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                if (board.checkPlaceable(new Coordinate(x, y), player)) {
                    count++;
                }
            }
        }
        return count;
    }

    public int weightedCellScore(ChessColor player) {
        int score = 0;
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                if (table[x][y] == player) {
                    score += squareScore[x][y];
                }
            }
        }
        return score;
    }

    public int frontier(ChessColor player) {
        final ChessColor opponent = player.getOpposite();
        int count = 0;
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                if (table[x][y] != player) {
                    continue;
                }
                for (int k = 0; k < 8; k++) {
                    int nx = x + DX8[k];
                    int ny = y + DY8[k];
                    if (Utils.coordinateInside(nx, ny) && table[nx][ny] == opponent) {
                        count++;
                        break;
                    }
                }
            }
        }
        return count;
    }

    public int disc(ChessColor player) {
        int count = 0;
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                if (table[x][y] == player) {
                    count++;
                }
            }
        }
        return count;
    }

    private void stableCorner(ChessColor player, int x, int y, boolean[][] stable) {
        for (int k = 0; k < 4; k++) {
            int nx = x;
            int ny = y;
            while (Utils.coordinateInside(nx, ny) && table[nx][ny] == player) {
                stable[nx][ny] = true;
                nx += DX4[k];
                ny += DY4[k];
            }
        }
    }
}
