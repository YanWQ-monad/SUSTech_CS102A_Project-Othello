package com.monadx.othello.chess;

import org.jetbrains.annotations.NotNull;

public record Coordinate(int x, int y) {
    public static Coordinate of(int x, int y) {
        if (!Utils.coordinateInside(x, y)) {
            throw new IllegalArgumentException("Coordinate is not inside the board");
        }
        return COORDINATES[x][y];
    }

    public int component1() {
        return x;
    }

    public int component2() {
        return y;
    }

    @NotNull private static final Coordinate[][] COORDINATES;
    @NotNull public static final Coordinate NULL_PLACEHOLDER = new Coordinate(-1, -1);

    static {
        COORDINATES = new Coordinate[8][8];
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                COORDINATES[i][j] = new Coordinate(i, j);
            }
        }
    }
}
