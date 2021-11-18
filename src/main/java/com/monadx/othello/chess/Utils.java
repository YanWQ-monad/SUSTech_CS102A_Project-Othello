package com.monadx.othello.chess;

import org.jetbrains.annotations.NotNull;

public class Utils {
    @NotNull public static final Coordinate[] POSITION_LIST = constructPositionList();

    public static boolean coordinateInside(int x, int y) {
        return x >= 0 && x < 8 && y >= 0 && y < 8;
    }

    // Static helper class, disable constructor
    private Utils() {}

    @NotNull
    private static Coordinate[] constructPositionList() {
        Coordinate[] list = new Coordinate[8 * 8];

        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                list[x + y * 8] = new Coordinate(x, y);
            }
        }

        return list;
    }
}
