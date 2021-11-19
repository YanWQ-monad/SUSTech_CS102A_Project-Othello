package com.monadx.othello.chess;

public record Coordinate(int x, int y) {
    public int component1() {
        return x;
    }

    public int component2() {
        return y;
    }
}
