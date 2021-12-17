package com.monadx.othello.ai.utils;

import org.jetbrains.annotations.NotNull;

import com.monadx.othello.chess.Board;
import com.monadx.othello.chess.ChessColor;
import com.monadx.othello.chess.Coordinate;
import com.monadx.othello.chess.Utils;

public class BoardExtends {
    @NotNull private final Board board;
    @NotNull private final ChessColor[][] table;

    @NotNull private final int[][] squareScore;

    @NotNull private static final int[] DX8 = {-1, -1, -1, 0, 0, 1, 1, 1};
    @NotNull private static final int[] DY8 = {-1, 0, 1, -1, 1, -1, 0, 1};
    @NotNull private static final int[] DX4 = {-1, 0, 1, 0};
    @NotNull private static final int[] DY4 = {0, -1, 0, 1};

    public BoardExtends(@NotNull Board board) {
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

    public int corner(@NotNull ChessColor player) {
        int count = 0;
        if (table[0][0] == player) count++;
        if (table[0][7] == player) count++;
        if (table[7][0] == player) count++;
        if (table[7][7] == player) count++;
        return count;
    }

    public int stable(@NotNull ChessColor player) {
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

    public int mobility(@NotNull ChessColor player) {
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

    public int weightedCellScore(@NotNull ChessColor player) {
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

    public int frontier(@NotNull ChessColor player) {
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

    public int disc(@NotNull ChessColor player) {
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

    private void stableCorner(@NotNull ChessColor player, int x, int y, @NotNull boolean[][] stable) {
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
